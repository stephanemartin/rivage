/*
 * Created on Jul 18, 2004
 */
package fr.inria.rivage.engine.manager;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.ConcurrencyChooser;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.manager.groupcontroller.JoinReplyPacket;
import fr.inria.rivage.engine.manager.groupcontroller.JoinRequestPacket;
import fr.inria.rivage.net.queues.InputQueue;
import fr.inria.rivage.net.queues.OutputQueue;
import fr.inria.rivage.users.User;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Yves
 */
public class GroupController2 extends Thread implements GroupController {

    IConcurrencyController concurrencyController;
    FileController fileController;
    InputQueue inputQ;
    OutputQueue outputQ;
    BooleanObject groupJoined;
    private static final Logger log = Logger.getLogger(GroupController2.class.getName());
    
    HashMap<Long, User> userHash;

    public GroupController2(FileController fc) {
        super("GroupController " + fc.getId() + " at site "
                + /*Application.getApplication().getNetwork().getSiteID()*/0);
       
        this.fileController = fc;

        /*
         * Register the input and output queues.
         */

      /*  try {
            Application.getApplication().getNetwork().registerInputQueue(
                    (inputQ = new InputQueue("GroupController"
                    + fileController.getId())));
            Application.getApplication().getNetwork().registerOutputQueue(
                    (outputQ = new OutputQueue("GroupController"
                    + fileController.getId())));

        } catch (NetworkException e) {
            log.fatal("Could not register input or output queues.", e);
        }*/

        userHash = new HashMap<Long, User>();

        initWaitObjects();

        log.info("GroupController initialized");
    }

    /**
     * Creates objects which can later be used for synchronization.
     */
    private void initWaitObjects() {
        groupJoined = new BooleanObject(false);
    }

    /**
     * Sends the message needed to tell the other sites that one wants to join a
     * group.
     */
    private void joinGroup() {
        JoinRequestPacket jp = null;//new JoinRequestPacket(Application.getApplication().getNetwork().getSiteID(), fileController.getId(), Application.getApplication().getUser());

        outputQ.enqueue(jp);
        log.info("JoinRequestPacket send.");
    }

    /**
     * Collects all the replies send after receving the request until the
     * timeout arrives. May return an empty array if no reply is received.
     *
     * @return an array containing all the replies received before timeout.
     */
    private JoinReplyPacket[] getJoinReplies() {
        /*JoinReplyPacket[] packets = null;
        ArrayList<JoinReplyPacket> arlist = new ArrayList<JoinReplyPacket>();
        long waituntil = 10000 + System.currentTimeMillis();

        while (System.currentTimeMillis() < waituntil) {
            try {
                Serializable packet = inputQ.dequeue(500);
                if (packet instanceof JoinReplyPacket) {
                    JoinReplyPacket jrp = (JoinReplyPacket) packet;
                    if (jrp.getToSiteID() == Application.getApplication().getNetwork().getSiteID()) {
                        arlist.add((JoinReplyPacket) packet);
                    }
                }
            } catch (InterruptedException ie) {
            }
        }
        if (arlist.size() > 0) {
            packets = new JoinReplyPacket[arlist.size()];
        }
        for (int i = 0; i < arlist.size(); i++) {
            packets[i] = arlist.get(i);
        }
        return packets;*/
        return null;
    }

    @Override
    public void run() {
        log.info("GroupController thread started.");

        try {
            joinGroup();

            runJoinProtocol();

            runMainProtocol();
        } catch (InterruptedException ie) {
            log.warning("The GroupController was interrupted.");
        }
    }

    /**
     * @throws InterruptedException
     */
    private void runMainProtocol() throws InterruptedException {
        log.info("Starting main GroupController loop.");
        while (!interrupted()) {
            Serializable s = inputQ.dequeue();

            if (s instanceof JoinRequestPacket) {
                /**
                 * Somebody wants to join the group. First, do the
                 * ConcurrencyController and block all the WorkAreas, then
                 * reply.
                 */
                fileController.disableWorkAreas();
                fileController.getConcurrencyController().syncAndPause();

                /**
                 * Now, reply.
                 */
                JoinRequestPacket jreq = (JoinRequestPacket) s;

                log.log(Level.INFO, "Received a message from site {0}", jreq.getSiteID());

                JoinReplyPacket jrp =null;// new JoinReplyPacket(Application.getApplication().getNetwork().getSiteID(), jreq.getSiteID(), fileController.getId(),
                  //      extractSiteIDFromHash(userHash), Application.getApplication().getUser(), fileController.getConcurrencyController().getClass().getName(), (Serializable) fileController.getConcurrencyController().getProperties());
                outputQ.enqueue(jrp);

                userHash.put(jreq.getSiteID(), jreq.getUser());

                fileController.getConcurrencyController().goOn();

                Application.getApplication().getMainFrame().getUserToolBar().addUserToList(jreq.getUser());
            }
        }
    }

    /**
     *
     */
    private void runJoinProtocol() {
        JoinReplyPacket[] packets;
        ArrayList<User> userList;
        if ((packets = getJoinReplies()) == null) {
            log.info("I am the first one to join the group.");
            /**
             * I'm the first one, let the user choose a ConcurrencyController
             * let it run.
             */
            ConcurrencyChooser cc = new ConcurrencyChooser(Application.getApplication().getMainFrame(), fileController);
            cc.setVisible(true);

            fileController.setConcurrencyController(cc.getConcurrencyController());

          //  userHash.put(new Long(Application.getApplication().getNetwork().getSiteID()), Application.getApplication().getUser());

            userList = new ArrayList<User>();
            userList.add(Application.getApplication().getUser());
            Application.getApplication().getMainFrame().getUserToolBar().setUserList(userList);

        } else {
            log.info("Some other people are already in the group.");
            /**
             * Start by analyzng packets. Initialize a ConcurrencyController,
             * start it and tell it to sync & pause.
             */
            userList = analyzePackets(packets);

            /**
             * Request sync, ...
             */
            Application.getApplication().getMainFrame().getUserToolBar().setUserList(userList);
        }
        synchronized (groupJoined) {
            groupJoined.setValue(true);
            groupJoined.notifyAll();
        }
    }

    /**
     * Analyzes the packets, takes needed steps (informing others of some
     * failures, ...) and returns a list of all the valid users, including the
     * local one.
     *
     * @param packets the packets received and that have to be analyzed.
     * @return an ArrayList <User>containing the list of users in contained in
     * the packet list and the local user.
     */
    @SuppressWarnings("unchecked")
    private ArrayList<User> analyzePackets(JoinReplyPacket[] packets) {

        /*
         * Add this site in the user hash before we go on, as it has to be there
         * anyway.
         */
     //   userHash.put(new Long(Application.getApplication().getNetwork().getSiteID()), Application.getApplication().getUser());

        /**
         * All the user stuff
         */
        ArrayList<User> userlist = new ArrayList<User>();
        for (JoinReplyPacket rp : packets) {
            userlist.add(rp.getUser());
            userHash.put(rp.getFromSiteID(), rp.getUser());
        }
        userlist.add(Application.getApplication().getUser());

        /**
         * ConcurrencyController
         */
        String CCname = packets[0].getCcName();

        Object[] CCops = new Object[packets.length];
        int i = 0;
        for (JoinReplyPacket o : packets) {
            CCops[i++] = o.getCcProps();
        }

        try {
            Class c = Class.forName(CCname);
            Constructor cons = c.getConstructor(new Class[]{FileController.class});
            concurrencyController = (IConcurrencyController) cons.newInstance(new Object[]{fileController});
            concurrencyController.putProperties(CCops);
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error happened while creating the ConcurrencyController.{0}", e);
            concurrencyController = null;
        }

        fileController.setConcurrencyController(concurrencyController);

        return userlist;
    }

    /**
     * This method makes the calling thread block until the group has been
     * joined.
     *
     * @throws InterruptedException if another thread interrupted the current
     * thread.
     */
    @Override
    public void groupJoined() throws InterruptedException {
        synchronized (groupJoined) {
            while (!groupJoined.getValue()) {
                groupJoined.wait();
            }
        }
    }

    private long[] extractSiteIDFromHash(HashMap<Long, User> userHash) {
        long[] ar = new long[userHash.keySet().size()];
        int c = 0;
        for (long l : userHash.keySet()) {
            ar[c++] = l;
        }
        return ar;
    }

    /**
     * Returns the hash conaining the current siteIDs in this group and the User
     * class for each site.
     *
     * @return a hash with siteIDs as key and User class as data.
     */
    @SuppressWarnings("unchecked")
    @Override
    public HashMap<Long, User> getMembers() {
        return (HashMap<Long, User>) userHash.clone();
    }
}
/**
 * A boolean object just needed for synchronization.
 *
 * @author yves
 */
class BooleanObject {

    private boolean value;

    public BooleanObject(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}

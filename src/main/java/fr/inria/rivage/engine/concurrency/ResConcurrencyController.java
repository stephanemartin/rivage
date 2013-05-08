/*
 * Created on Mar 11, 2005
 */
package fr.inria.rivage.engine.concurrency;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.exceptions.CCRuntimeException;
import fr.inria.rivage.engine.concurrency.resconcurrency.History;
import fr.inria.rivage.engine.concurrency.resconcurrency.OpWrapper;
import fr.inria.rivage.engine.concurrency.resconcurrency.ResMainThread;
import fr.inria.rivage.engine.concurrency.tools.FactoryParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.concurrency.utils.BlockingQueue;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.GroupController2;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.net.queues.InputQueue;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

public class ResConcurrencyController
        implements IConcurrencyController {

    @Override
    public ID getNextID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FactoryParameter getFactoryParameter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void assignIDs(GObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void recieveOperation(Operation op) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param id the value of id
     */
    public Position getFirstPosition(ID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendOperation(Operation op) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DocumentSync getSyncInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSyncInfo(DocumentSync ds) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOurID(ID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private enum STATES {

        NEW, RUNNING, WAITING, PAUSED, HALTED
    }
    private STATES state;
    private Logger log;
    private FileController fileController;
    private GroupController2 groupController;
    private InputQueue<OpWrapper> netinput;
    private BlockingQueue<OpWrapper> toMain;
    private ResMainThread mainThread;
    private Collector colThread;
    private long siteID;
    private History history;

    public ResConcurrencyController(FileController fileController) {
        this.log = Logger.getLogger(ResConcurrencyController.class);

        this.fileController = fileController;
        //this.groupController = fileController.getGroupController();
        //this.siteID = Application.getApplication().getNetwork().getSiteID();

        this.toMain = new BlockingQueue<OpWrapper>();


        this.mainThread = new ResMainThread(toMain, fileController);
        this.colThread = new Collector();

        netinput = new InputQueue<OpWrapper>("Concurrency"
                + fileController.getId(), true);

        /*try {
         Application.getApplication().getNetwork().registerInputQueue(
         netinput);
         } catch (NetworkException ne) {
         log.fatal("Could not register input or output queues.", ne);
         }*/

        state = STATES.NEW;
    }

    @Override
    public void startNew() {
        if (state != STATES.NEW) {
            throw new CCRuntimeException("Called startNew() from wrong state.");
        }

        this.history = new History(groupController.getMembers(), siteID, fileController);
        mainThread.setHistory(history);

        mainThread.start();
        colThread.start();

        state = STATES.RUNNING;
    }

    @Override
    public void syncAndPause() {
        if (state != STATES.RUNNING) {
            throw new CCRuntimeException(
                    "Called syncAndPause() from wrong state.");
        }

        /*
         * Stop passing on input to the mainThred (lock workarea). Synchornize
         * with all sites. In the first version, we'll just sleep for 2 seconds
         * and then it should be good everywhere -- stop the mainThread and wait
         * for instructions. We can let the colThread run as it'll only collect
         * the missing messages.
         */

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainThread.pause();

        state = STATES.PAUSED;
    }

    @Override
    public void goOn() {
        if (state != STATES.PAUSED) {
            throw new CCRuntimeException("Called goOn() from wrong state.");
        }

        /*
         * Tell the main thread to go on working. Recreate a history.
         */

        history = new History(groupController.getMembers(), siteID, fileController);
        mainThread.setHistory(history);
        mainThread.goOn();

        // history = new History(groupController.getMembers(), siteID);

        state = STATES.RUNNING;
    }

    @Override
    public void halt() {
        /*if (state != STATES.PAUSED)
         throw new CCRuntimeException("Called goOn() from wrong state.");
         */
        mainThread.halt();
        colThread.interrupt();

        state = STATES.HALTED;
    }

    @Override
    public void undoLastLocalOp() {
        OpWrapper opw = new OpWrapper(null, history.vectorForNextLOP(), siteID,
                OpWrapper.OpType.UNDOLOCAL);
        synchronized (toMain) {
            toMain.insertFront(opw);
        }
    }

    @Override
    public void undoLastGlobalOp() {
        OpWrapper opw = new OpWrapper(null, history.vectorForNextLOP(), siteID,
                OpWrapper.OpType.UNDOGLOBAL);
        synchronized (toMain) {
            toMain.insertFront(opw);
        }
    }

    @Override
    public void redoLastLocalOp() {
        OpWrapper opw = new OpWrapper(null, history.vectorForNextLOP(), siteID,
                OpWrapper.OpType.REDOLOCAL);
        synchronized (toMain) {
            toMain.insertFront(opw);
        }
    }

    @Override
    public void redoLastGlobalOp() {
        OpWrapper opw = new OpWrapper(null, history.vectorForNextLOP(), siteID,
                OpWrapper.OpType.REDOGLOBAL);
        synchronized (toMain) {
            toMain.insertFront(opw);
        }
    }

    @Override
    public void doAndSendOperation(Operation op) {
        if (!(state == STATES.RUNNING || state == STATES.WAITING)) {
            throw new CCRuntimeException(
                    "Called doOperation() from wrong state.");
        }

        OpWrapper opw = new OpWrapper(op, history.vectorForNextLOP(), siteID,
                OpWrapper.OpType.DO);
        opw.setLocal(true);
        synchronized (toMain) {
//			toMain.insertFront(opw);
            toMain.enqueue(opw);

        }

        mainThread.goOn();

        state = STATES.RUNNING;
    }

    @Override
    public JPanel getPropPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putProperties(Object[] props) {
        // TODO Auto-generated method stub
    }

    @Override
    public void startLocalOp() {
        if (state != STATES.RUNNING) {
            throw new CCRuntimeException(
                    "Called startLocalOp() from wrong state.");
        }

        /*
         * Pause the main thread. When do operation is called, insert the
         * operation at the beginning of the input queue and resume the main
         * thread. It is important that the local operation is done first.
         */

        mainThread.pause();

        state = STATES.WAITING;
    }

    class Collector extends Thread {

        public Collector() {
        }

        @Override
        public void run() {
            while (!interrupted()) {
                OpWrapper o;
                try {
                    o = netinput.dequeue();
                    o.setLocal(false);
                    synchronized (toMain) {
                        toMain.enqueue(o);
                    }
                } catch (InterruptedException e) {
                    /*
                     * This is not a problem, will be caught at next loop start
                     */
                    e.printStackTrace();
                    System.out.println(e);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

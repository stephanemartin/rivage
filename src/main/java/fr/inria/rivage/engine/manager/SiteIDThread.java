/*
 * Created on Aug 27, 2004
 */
package fr.inria.rivage.engine.manager;

import fr.inria.rivage.engine.manager.siteid.SiteIDDemand;
import fr.inria.rivage.engine.manager.siteid.SiteIDReply;
import fr.inria.rivage.net.overlay.MulticastNetwork;
import fr.inria.rivage.net.queues.InputQueue;
import fr.inria.rivage.net.queues.OutputQueue;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * This class is responsible for calculating the site ID and distributing it to
 * others that ask for it. It works as an independent thread. <br> This class
 * cannot use the refuseLocal functionality of the networking layer, as that
 * part uses this class itself to identify packets.
 *
 * @author Yves
 */
public class SiteIDThread extends Thread {

    private static SiteIDThread siteIDThread;

    public static SiteIDThread getSiteThread(MulticastNetwork mn) {
        if (siteIDThread == null) {
            siteIDThread = new SiteIDThread(mn);
        }
        return siteIDThread;
    }
    private Logger log;
    private InputQueue inputQ;
    private OutputQueue outputQ;
    private long siteID;
    private long maxKnownID;
    private byte[] random;
    private SecureRandom sr;

    private SiteIDThread(MulticastNetwork mn) {
        log = Logger.getLogger(SiteIDThread.class);

        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            log.error("The random generator could not be started.", e);
            sr = new SecureRandom();
        }

        siteID = sr.nextInt(20);
        maxKnownID = siteID;
        byte[] bytes = new byte[50];
        random = new byte[50];
        new Random(System.currentTimeMillis()).nextBytes(bytes);
        sr.setSeed(bytes);

        inputQ = new InputQueue("SiteID");
        outputQ = new OutputQueue("SiteID");

        /*	try {
         *		mn.registerInputQueue(inputQ);
         mn.registerOutputQueue(outputQ);*
         } catch (NetworkException ne) {
         log
         .fatal("SiteID generator could not register input and output queues.");
         Application.getApplication().fatalTermination(ne);
         }*/

        sr.nextBytes(random);

        SiteIDDemand sip = new SiteIDDemand(random);

        log.debug("Sending demand for siteID.");

        outputQ.enqueue(sip);

    }

    public long getSiteID() {
        return siteID;
    }

    @Override
    public void run() {
        log.debug("SiteIDThread started.");
        while (!isInterrupted()) {
            try {

                Object sip = inputQ.dequeue();
                log.debug("Received package : " + sip);

                if (sip instanceof SiteIDDemand) {
                    log.debug("Received a demand for siteID.");
                    SiteIDDemand demand = (SiteIDDemand) sip;
                    if (!Arrays.equals(demand.random, random)) {
                        SiteIDReply reply = new SiteIDReply(siteID, maxKnownID,
                                random, demand.random);
                        outputQ.enqueue(reply);
                        log.debug("Send Reply.");
                    }
                } else if (sip instanceof SiteIDReply) {
                    log.debug("Received a reply for SiteID");
                    SiteIDReply reply = (SiteIDReply) sip;
                    if (Arrays.equals(random, reply.targetRandom)) {
                        if (reply.maxKnownID >= siteID) {
                            siteID = reply.maxKnownID + sr.nextInt(20);
                            log.info("Local siteID is:" + siteID);
                        }
                    }
                } else {
                    log
                            .info("A message of unknown type was received by the SiteIDThread.");
                }

            } catch (InterruptedException ie) {
                log.debug("SiteIDThread was interrupted.", ie);
            }
        }
    }
}
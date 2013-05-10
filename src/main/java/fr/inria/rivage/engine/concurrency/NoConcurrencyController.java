/*
 * Created on Jun 23, 2004
 */
package fr.inria.rivage.engine.concurrency;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.noconcurrencycontroller.MainThread;
import fr.inria.rivage.engine.concurrency.noconcurrencycontroller.OpObjectGen;
import fr.inria.rivage.engine.concurrency.tools.FactoryParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.concurrency.utils.BlockingQueue;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.net.queues.InputQueue;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * This is a simple concurrency Controller that should do nothing else, but
 * receive some operations from the net and from local. Remote operations should
 * be applied to the tree. Eventually, a primitive undoLocal and undoGlobal
 * should be implemented.
 *
 * @author Yves
 */
public class NoConcurrencyController extends Thread implements
        IConcurrencyController {

    private FileController fileController;
    private InputQueue input;
    private MainThread mainthread;
    private BlockingQueue<OpObjectGen.OpPack> tomain;
    private OpObjectGen opgen;
    private static final Logger log = Logger.getLogger(NoConcurrencyController.class.getName());
    

    public NoConcurrencyController(FileController fileController) {
        this.fileController = fileController;
        input = new InputQueue("Concurrency" + fileController.getId(), true);

        opgen = new OpObjectGen();
        tomain = new BlockingQueue<OpObjectGen.OpPack>();


        /*try {
            Application.getApplication().getNetwork().registerInputQueue(input);
        } catch (NetworkException ne) {
            log.fatal("Error in NoConcurrencyController.", ne);
        }*/
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#syncandpause()
     */
    @Override
    public void syncAndPause() {
        /**
         * This algorithm doesn't really need much sync, because it only applies
         * what arrives, so waiting a small amout of time is okay here.
         */
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#resume()
     */
    @Override
    public void goOn() {
        /**
         * We haven't stopped the mainthread, so nothing to do here.
         */
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#stop()
     */
    @Override
    public void halt() {
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#undoLastLocaleOp()
     */
    @Override
    public void undoLastLocalOp() {
        synchronized (tomain) {
            tomain.enqueue(opgen.getUndoLocalOperationPack());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#undoLastGlobalOp()
     */
    @Override
    public void undoLastGlobalOp() {
        synchronized (tomain) {
            tomain.enqueue(opgen.getUndoGlobalOperationPack());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * geditor.engine.concurrency.IConcurrencyController#doAndSendOperation(geditor.engine.operations.Operation)
     */
    @Override
    public void doAndSendOperation(Operation op) {

        synchronized (tomain) {
            tomain.enqueue(opgen.getLocalOperationPack(op));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#startNew()
     */
    @Override
    public void startNew() {
        mainthread = new MainThread(fileController, tomain);
        mainthread.start();
        start();
        log.fine("Controller started.");
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#getPropPanel()
     */
    @Override
    public JPanel getPropPanel() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#getProperties()
     */
    @Override
    public Object getProperties() {
        return null;
    }

    @Override
    public void putProperties(Object[] props) {
    }

    @Override
    public void run() {
        /**
         * The main loop of the algorithm. This loop should read the queue and
         * put the messages in the algorithm's queue
         */
        while (!interrupted()) {
            Operation op;
            try {
                op = (Operation) input.dequeue();
                synchronized (tomain) {
                    tomain.enqueue(opgen.getGlobalOperationPack(op));
                }
            } catch (InterruptedException e) {
                interrupt();
            }

        }

    }

    @Override
    public void redoLastLocalOp() {
    }

    @Override
    public void redoLastGlobalOp() {
    }

    @Override
    public void startLocalOp() {
    }

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
}

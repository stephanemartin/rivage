/*
 * Created on Jun 23, 2004
 */
package fr.inria.rivage.engine.concurrency.crdt;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.DocumentSync;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.Factory;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameter;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.engine.operations.UnableToApplyException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
//import org.apache.log4j.Logger;

/**
 * This is a simple concurrency Controller that should do nothing else, but
 * receive some operations from the net and from local. Remote operations should
 * be applied to the tree. Eventually, a primitive undoLocal and undoGlobal
 * should be implemented.
 *
 * @author Yves
 */
public class ConcurrencyControllerCRDT extends Thread implements
        IConcurrencyController {

    private FileController fileController;
    /* private InputQueue input;
     private OutputQueue output;*/
    //private MainThread mainthread;
    //private BlockingQueue<OpObjectGen.OpPack> tomain;
    //private OpObjectGen opgen;
    private static final Logger log = Logger.getLogger(Class.class.getName());
    private final Factory<Parameter> factoryParameter;
    private StateVector stateVector;
    private final LinkedList<Operation> waitingList;
    //FileController fc;
    private final CRDTIDFactory crdtID;

    synchronized public void recieveOperation(Operation op) {
        waitingList.addFirst(op);
        if (this.fileController.getDocument() == null) {
            return;
        }
        boolean mod;
        do {
            mod = false;
            Iterator<Operation> it = waitingList.iterator();
            while (it.hasNext()) {
                Operation opl = it.next();
                switch (stateVector.getStatus((CRDTID) opl.getId())) {
                    case Ready:
                        stateVector.setSiteID((CRDTID) opl.getId());
                        doOperation((Operation) op);
                        mod = true;
                    case Already:
                        it.remove();
                        break;
                    default:
                }
            }
        } while (mod);
        //if (waitingList.size()>0){
        log.log(Level.INFO, "The waiting list contains {0} element(s)", waitingList.size());
        //         }
    }

    public ConcurrencyControllerCRDT(FileController fileController) {
        this.fileController = fileController;
        this.stateVector = new StateVector();
        crdtID = new CRDTIDFactory(this);
        this.waitingList = new LinkedList();
        //opgen = new OpObjectGen();
        //   tomain = new BlockingQueue<OpObjectGen.OpPack>();



        //this.log = Logger.getLogger(ConcurrencyControllerCRDT.class);
        factoryParameter = new FactoryParameter(/*fileController.getDocument()*/);
    }

    public CRDTIDFactory getCrdtID() {
        return crdtID;
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
            log.log(Level.SEVERE, "", e);
        }
    }

    public StateVector getStateVector() {
        return stateVector;
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
        /* synchronized (tomain) {
         tomain.enqueue(opgen.getUndoLocalOperationPack());
         }*/
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#undoLastGlobalOp()
     */
    @Override
    public void undoLastGlobalOp() {
        /*synchronized (tomain) {
         tomain.enqueue(opgen.getUndoGlobalOperationPack());
         }*/
    }

    synchronized public void doOperation(Operation op) {

        try {
            op.apply(fileController);
            fileController.getCurrentWorkArea().repaint();

            //tomain.enqueue(opgen.getLocalOperationPack(op));
        } catch (UnableToApplyException ex) {
            java.util.logging.Logger.getLogger(ConcurrencyControllerCRDT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * geditor.engine.concurrency.IConcurrencyController#doAndSendOperation(geditor.engine.operations.Operation)
     */
    @Override
    synchronized public void doAndSendOperation(Operation op) {
       // System.out.println("op:" + op);
        sendOperation(op);
        doOperation(op);
    }

    /*
     * (non-Javadoc)
     *
     * @see geditor.engine.concurrency.IConcurrencyController#startNew()
     */
    @Override
    public void startNew() {
        /*  input = new InputQueue("Concurrency" +fileController.getFileID(), true);
         output = new OutputQueue("Concurrency"+ fileController.getFileID());
         opgen = new OpObjectGen();*/
        //   tomain = new BlockingQueue<OpObjectGen.OpPack>();
        //this.log = Logger.getLogger(ConcurrencyControllerCRDT.class);

        /*try {
         Application.getApplication().getNetwork().registerInputQueue(input);
         } catch (NetworkException ne) {
         log.fatal("Error in NoConcurrencyController.", ne);
         }*/
        /*  System.out.println("Controller started.");
         mainthread = new MainThread(fileController, tomain);
         mainthread.start();
         start();
         log.debug("Controller started.");*/
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
        /*while (!interrupted()) {
         Operation op;
         try {
         op = (Operation) input.dequeue();
         synchronized (this) {
         recieveOperation((FCOperation) op);
         }
         /*synchronized (tomain) {
                    
         tomain.enqueue(opgen.getGlobalOperationPack(op));
         }*
         } catch (InterruptedException e) {
         interrupt();
         }

         }*/
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
        return this.crdtID.getNewId();
    }

    @Override
    public Factory<Parameter> getFactoryParameter() {
        return factoryParameter;
    }

    @Override
    public void assignIDs(GObject obj) {
        obj.setId(this.getNextID());
    }

    public Position getFirstPosition(ID id) {
        return new FCPosition(id);
    }

    synchronized public void sendOperation(Operation op) {
        if (op.getId() == null) {
            op.setId(this.getNextID());
        }


        
        //log.info("Sendo "+ this.stateVector.getBySite((CRDTID) op.getId()));
        fileController.getFileControleurManager().sendMessage(fileController, op);

    }

    synchronized public DocumentSync getSyncInfo() {
        return new DocumentSyncCRDT(this.stateVector, this.fileController.getDocument());

    }

    synchronized public void setSyncInfo(DocumentSync ds) {
        if (this.fileController.getDocument() == null) {
            DocumentSyncCRDT ds2 = (DocumentSyncCRDT) ds;
            this.stateVector = ds2.getStateVector();
            this.fileController.setDocument(ds2.getGDocument());
        }
    }

    public boolean isOurID(ID id) {
        return ((CRDTID) id).siteID.equals(this.crdtID.siteIDc);
    }
}

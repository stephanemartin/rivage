/*
 * Created on Jun 23, 2004
 */
package fr.inria.rivage.engine.concurrency;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.tools.FactoryParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.operations.Operation;
import javax.swing.JPanel;

/**
 * @author Yves
 */
public interface IConcurrencyController {

    /**
     * Starts the concurrency controller in a new group.
     */
    public void startNew();

    /**
     * This method should only return when all the controllers in the group are
     * synchornized and not before. This should work for all concurrency
     * methods, be it nop based, priotiry based, ... using locking, ... After
     * the method's return, the concurrency contoller should be in a sleeping
     * mode, not working anymore until
     * <code>goOn()</code> is called.
     */
    public void syncAndPause();

    /**
     * Resumes the concurrency controller after a
     * <code>syncAndPause()</code>
     */
    public void goOn();

    /**
     * Stops and terminates the concurrency controller. Upon execution of this
     * method, the concurrency controller stops working and cannot be resumed.
     * All resources hold are released. Should only be called when the program
     * is terminated and after
     * <code>syncAndPause()</code> has been called.
     */
    public void halt();

    /**
     * Undoes the last global operation. If used more than once, the operations
     * should be undone one after another in opposite order they were done.
     */
    public void undoLastLocalOp();

    /**
     * Undoes the last local operation. If used more than once, the operations
     * should be undone one after another in opposite order they were done.
     */
    public void undoLastGlobalOp();

    /**
     * Redo the last local operation undone.
     */
    public void redoLastLocalOp();

    /**
     * Redo the last global operation undone.
     */
    public void redoLastGlobalOp();

    /**
     * Every time a new local operation is started, this method should be called
     * first. It makes sure that the concurrency controller knows the context on
     * which this operation will apply and not the state in which the operation
     * finishes.
     */
    public void startLocalOp();

    /**
     * Sets this operation locally as done and send it to the other sites.
     */
    public void doAndSendOperation(Operation op);

    public void sendOperation(Operation op);

    public void recieveOperation(Operation op);
    //public void recieveOperation(Operation op);

    /**
     * Returns a panel where the user can select some properties of the
     * controller. This is for when the controller starts. You can only call
     * this before starting the controller. At start, the controller will read
     * the properties from the panel. Keep in mind that not all the properties
     * may be respected if the group already existed.
     */
    public JPanel getPropPanel();

    /**
     * This should returns the settings of the current ConcurrencyController.
     *
     * @return an object containing all the properties.
     */
    public Object getProperties();

    /**
     * When you get the properties from the other ConcurrencyControllers, you
     * have to pass them to this one. Like that, no need to ask one more time
     * all the participants.
     *
     * @param props the properties
     */
    public void putProperties(Object[] props);

    /*
     * Generate nextUniqueID for the document
     */
    public ID getNextID();

    public boolean isOurID(ID id);
    
    public void assignIDs(GObject obj);

    /*
     * Parameter Factory
     */
    public FactoryParameter getFactoryParameter();

    /**
     * get the first position on the docuement.
     *
     * @param id is identifier for new position.
     */
    public Position getFirstPosition(ID id);

    public DocumentSync getSyncInfo();

    public void setSyncInfo(DocumentSync ds);
}

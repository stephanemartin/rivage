/*
 * Created on Jun 14, 2004
 */
package fr.inria.rivage.engine.operations;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.listener.LayerChangeListener;
import fr.inria.rivage.gui.listener.PageChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Elements can only be created in the root class, so we don't need to pass a
 * parent or something like that.
 *
 * @author Yves
 */
public class CreateOperation extends Operation {

    private ColObject object;
    //private ID parentID;
    //private boolean validZ = false;

    public CreateOperation(ColObject object) {
        try {
            this.object = object.clone();
            this.object.resetParent();

        } catch (CloneNotSupportedException ex) {
       
            this.object = object;
            Logger.getLogger(CreateOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.id = object.getId();

    }

    /**
     * @param initiatorID
     * @param priority
     * @param stateVect
     */
    /*public CreateOperation(GObject object) {
     this.object = object;
     //this.object = Cloner.clone(object);
     //this.object.setParent(null);
     //this.parentID = parentID;
     }*/
    @Override
    protected void doApply(FileController fc) throws UnableToApplyException {
       /* if (true){
            return;
        }*/


        try {
           
            ColObject objectAppli;
            try {
                objectAppli = this.object.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CreateOperation.class.getName()).log(Level.SEVERE, null, ex);
                //System.exit(0);
                objectAppli = this.object;
            }

            objectAppli.getParameters().setDocument(fc.getDocument());
            objectAppli.setParentFromID(fc.getDocument());
            fc.getDocument().addObject(objectAppli);
            
            if (objectAppli instanceof Page) {
                int index = fc.getDocument().getIndex((Page) objectAppli);
                fc.getInnerWindow().pageChanged(PageChangeListener.Event.NEW_PAGE, id, index);
            } else if (objectAppli instanceof GLayer) {
                Application.getApplication().getMainFrame().getLayersToolBar().layerChanged(LayerChangeListener.Type.NEW_LAYER);
            } else {
                fc.getCurrentWorkArea().treeChanged();
            }

          
        } catch (ClassCastException ex) {
            throw new UnableToApplyException(
                    "Parent is not a group.",
                    UnableToApplyException.CRITICAL_ERROR);
        }

    }

    @Override
    protected void doUnapply(FileController fc) throws UnableToApplyException {
        System.out.println("unapply create id = " + object.getId() + "\n");
        /*try {
         fc.getTreeManager().delete(object.getId());
         } catch (IllegalTreeOpException ex) {
         throw new UnableToApplyException(
         "Cannot undo create: " + ex.toString(),
         UnableToApplyException.INFORMATION);
         }*/
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CreateOperation{" + id + "object=" + object + '}';
    }

    @Override
    public List<ID> dependOf() {
        return Arrays.asList(this.object.getParentId());
    }
}

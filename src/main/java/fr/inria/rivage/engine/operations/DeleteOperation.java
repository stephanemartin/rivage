package fr.inria.rivage.engine.operations;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.listener.LayerChangeListener;
import fr.inria.rivage.gui.listener.PageChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteOperation extends Operation {
    private static final Logger LOG = Logger.getLogger(DeleteOperation.class.getName());

    ID deleteId;
    // undo information:
    private transient ColObject deletedObject;

    public DeleteOperation(ID id) {
        this.deleteId = id;
    }

    @Override
    protected void doApply(FileController fc) throws UnableToApplyException {

        // try {
        ColObject o = (ColObject) fc.getDocument().getObjectById(deleteId);
        if(o==null){
            LOG.log(Level.WARNING, "Object id {0} not found this object could be deleted in concurency", deleteId);
        }
        deletedObject = o;
        WorkArea wa=fc.getCurrentWorkArea();
        fc.getDocument().remove(deleteId);
        if (o instanceof GObject) {
            GObject object = (GObject) o;
            fc.getCurrentWorkArea().getSelectionManager().removeSelObject(object);



            if (object instanceof Page) {
                int index = fc.getDocument().getIndex((Page) object);
                fc.getInnerWindow().pageChanged(PageChangeListener.Event.NEW_PAGE, id, index);
            } else if (object instanceof GLayer) {
                Application.getApplication().getMainFrame().getLayersToolBar().layerChanged(LayerChangeListener.Type.NEW_LAYER);
                if(wa.getActiveLayer()==o){
                    wa.setActiveLayer(null);
                    wa.getSelectionManager().clearSelection();
                }
            } else {
                fc.getCurrentWorkArea().treeChanged();
            }
        }
        fc.getCurrentWorkArea().treeChanged();

    }

    @Override
    protected void doUnapply(FileController fc) throws UnableToApplyException {
        //try {
        fc.getDocument().addObject(deletedObject);
        /*  } catch (IllegalTreeOpException ex) {
         throw new UnableToApplyException(
         "Cannot undo delete: " + ex.toString(),
         UnableToApplyException.CRITICAL_ERROR);
         }*/
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        DeleteOperation clone = (DeleteOperation) super.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "DeleteOperation{" + "id=" + id + ", deleted=" + deleteId + '}';
    }

    @Override
    public List<ID> dependOf() {
        return Arrays.asList(deleteId);
    }
}

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

public class DeleteOperation extends Operation {

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
        deletedObject = o;

        fc.getDocument().remove(deleteId);
        if (o instanceof GObject) {
            GObject object = (GObject) o;
            fc.getCurrentWorkArea().getSelectionManager().removeSelObject(object);



            if (object instanceof Page) {
                int index = fc.getDocument().getIndex((Page) object);
                fc.getInnerWindow().pageChanged(PageChangeListener.Event.NEW_PAGE, id, index);
            } else if (object instanceof GLayer) {
                Application.getApplication().getMainFrame().getLayersToolBar().layerChanged(LayerChangeListener.Type.NEW_LAYER);
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

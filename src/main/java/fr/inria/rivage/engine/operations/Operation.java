package fr.inria.rivage.engine.operations;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import java.io.Serializable;
import java.util.List;

public abstract class Operation implements Serializable, Cloneable {

    private volatile boolean applied = false;
    private volatile boolean canceled = false;
    private volatile boolean dirty = false;
    //private Set<Long> children;
   // protected ID pageId;
    protected ID id;
    
    public abstract List<ID> dependOf();
    /**
     * Returns the current state of the operation. If it has already been
     * applied or not.
     *
     * @return a boolean value telling if it was already applied or not.
     */
    public boolean isApplied() {
        return applied;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void reject() {
        if (canceled) {
            return;
        }
        canceled = true;
        dirty = !dirty;
    }

    public void accept() {
        if (!canceled) {
            return;
        }
        canceled = false;
        dirty = !dirty;
    }

    public ID getId() {
        return id;
    }
    public void setId(ID id){
        this.id=id;
    }
    
    /**
     * Should run the operation on the tree whose root is passed to the
     * operation. It should also remember the previous state, in order to be
     * able to make an undo later.
     *
     * @param root the object representing the root of the tree.
     * @exception UnableToApplyException this happens if the operation couldn't
     * be done correctly.
     */
    public void apply(FileController fileController) throws UnableToApplyException {

        //	System.out.println ("apply cancel = " + canceled + "\n");
        if (canceled) {
            return;
        }

        applied = true;
        //children = new HashSet<Long>();

       /* Page page = fileController.getDocument().getPage(getId());
        if (page != null) {
            pageId = page.getId();
        } else {

            GLayer layer = fileController.getDocument().getLayer(getId());

            if (layer != null) {
                pageId = layer.getPage().getId();
                for (GObjectShape oo : layer.getChildren()) {
                    if (oo instanceof IGroup) {
                        for (GObjectShape o : ((IGroup) oo).getObjects()) {
                          //  children.add(o.getId());
                        }
                    } else {
                        //children.add(oo.getId());
                    }
                }
            } else {
                GObjectShape obj;

                try {
                    obj = (GObjectShape) fileController.getDocument().getObjectById(getId());
                } catch (Exception e) {
                    obj = null;
                }

                if (obj != null) {
                    try {

                        Page p = fileController.getDocument().getPageFromObj(obj);
                        if (p != null) {
                            pageId = p.getId();
                        }
                        if (obj instanceof IGroup) {
                            for (GObjectShape o : ((IGroup) obj).getObjects()) {
                                //children.add(o.getId());
                            }
                        } else {
                            //children.add(getId());
                        }
                    } catch (Exception ex) {;
                    }
                } else {
                    Page p = fileController.getDocument().getPage(getId());
                    if (p != null) {
                        pageId = p.getId();
                    }
                }
            }
        }*/


        //fileController.getDocument().getPage(pageID)

//		if (!canceled)
        doApply(fileController);

        //fileController.getTreeManager().fireTreeChange();

    }
   
    public void unapply(FileController fileController) throws UnableToApplyException {
        applied = false;
//		System.out.println ("unapply cancel = " + canceled + " dirty = " + dirty + "\n");
        if ((!canceled && !dirty) || (canceled && dirty)) {
            doUnapply(fileController);
        }
      //  fileController.getTreeManager().fireTreeChange();
    }

    protected abstract void doApply(FileController fileController) throws UnableToApplyException;

    protected abstract void doUnapply(FileController fileController) throws UnableToApplyException;

    /**
     * Sets the current state of the operation, for example, a locally used
     * operation may have a set state without having been run.
     *
     * @param state true if it should assume it has been applied, false else.
     */
    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    /**
     * Clones the object.
     *
     * @return a clone of the current object.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @returns the id of the object modified / created / deleted by this
     * operation
     */
    //public abstract long getId();

    /*public boolean childOfID(ID id) {
        if (children == null) {
            return false;
        }
        return children.contains(id);
    }*/

    public boolean isDirty() {
        return dirty;
    }

    public void clean() {
        dirty = false;
    }

    public void dirty() {
        dirty = true;
    }

    
}
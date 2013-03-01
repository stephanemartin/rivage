package fr.inria.rivage.engine.operations;


import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import java.util.List;

public class MoveToLayerOperation extends Operation {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ID id;
	private ID layerID;
	
	// undo information
	private ID oldParentID;
	private int oldIndex;

	public MoveToLayerOperation(ID id, ID layerID) {
		this.id = id;
		this.layerID = layerID;
	}

    @Override
	public void doApply(FileController fileController) throws UnableToApplyException {
		GDocument doc = fileController.getDocument();
		GLayer layer =(GLayer) doc.getObjectById(layerID);
		
		if (layer == null) {
			throw new UnableToApplyException(
					"Layer not found with id: " + layerID,
					UnableToApplyException.CRITICAL_ERROR);
		}
		
			GObjectShape obj;
			try {
				obj = (GObjectShape) doc.getObjectById(id);
			
			} catch (ClassCastException ex) {
				throw new UnableToApplyException(
						"Illegal object.",
						UnableToApplyException.CRITICAL_ERROR);
			}
			oldParentID = obj.getParent()[0].getId();	
			//obj.getParent()[0].remove(obj);
			layer.add(obj);
                        throw new UnsupportedOperationException("not yet !");
			//obj.setParent(layer);
	}
	
    @Override
	public void doUnapply(FileController fileController) throws UnableToApplyException {
		GDocument doc = fileController.getDocument();
		
			GObjectShape obj;
			try {
				obj = (GObjectShape) doc.getObjectById(id);
			
			} catch (ClassCastException ex) {
				throw new UnableToApplyException(
						"Illegal object.",
						UnableToApplyException.CRITICAL_ERROR);
			}
			
			GObjectContainer parent;
//			long parentID = oldParentIDs.get(ids.indexOf(id));
	//		int index = oldIndexes.get(ids.indexOf(id));
			try {
				parent = (GObjectContainer) doc.getObjectById(oldParentID);
			
			} catch (ClassCastException ex) {
				throw new UnableToApplyException(
						"Illegal parent.",
						UnableToApplyException.CRITICAL_ERROR);
			}
		
			//obj.getParent()[0].remove(obj);
			parent.add(obj);
			obj.setParent(parent);
                        throw new UnsupportedOperationException("not yet !");
	}

    @Override
	public Object clone() 
	{
		return super.clone();
	}

	
	public ID getLayerId ()
	{
		return layerID;
	}

    @Override
    public List<ID> dependOf() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

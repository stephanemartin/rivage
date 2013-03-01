package fr.inria.rivage.engine.operations;


import fr.inria.rivage.elements.GSnapPoint;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.interfaces.ISnappable;
import fr.inria.rivage.elements.interfaces.ISnapper;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import java.util.List;


public class UnsnapOperation extends Operation {

	private ID idSnapper;
	private int idSnapPoint;
	
	// undo information:
	private transient ISnappable snappable;
	private transient PointDouble snapPos;
	
	public UnsnapOperation(ID idSnapper, int idSnapPoint) {
		this.idSnapper = idSnapper;
		this.idSnapPoint = idSnapPoint;
	}
	
    @Override
	protected void doApply(FileController fc) throws UnableToApplyException {
		ISnapper snapper = getSnapper(fc);
		GSnapPoint snapPoint = getSnapPoint(snapper);
		
		snappable = snapPoint.getSnappedObject();
		snapPos = new PointDouble(snapPoint);
		
		snapPoint.setSnappedPoint(null);
	}
	
    @Override
	protected void doUnapply(FileController fc) throws UnableToApplyException {
		ISnapper snapper = getSnapper(fc);
		GSnapPoint snapPoint = getSnapPoint(snapper);
		
		snappable.getSnapManager().snap(snapPoint, snapPos, 0, 0);
	}
	
	private ISnapper getSnapper(FileController fc) throws UnableToApplyException {
		ISnapper snapper;
		try {
			snapper = (ISnapper) fc.getDocument().getObjectById(idSnapper);
		
		} catch (ClassCastException ex) {
			throw new UnableToApplyException(
					"Object is not a snapper.",
					UnableToApplyException.CRITICAL_ERROR);
		}
		return snapper;
	}
	
	private GSnapPoint getSnapPoint(ISnapper snapper) throws UnableToApplyException {
		GSnapPoint snapPoint = (GSnapPoint) snapper.getSnapPoint(idSnapPoint);
		if (snapPoint == null) {
			throw new UnableToApplyException(
					"Invalid snap point index.",
					UnableToApplyException.CRITICAL_ERROR);
		}
		return snapPoint;
	}
	
	@Override
	public Object clone() {
		UnsnapOperation clone = new UnsnapOperation(idSnapper, idSnapPoint);
		clone.setApplied(this.isApplied());
		clone.snappable = snappable;
		if (snapPos != null) {
			clone.snapPos = new PointDouble(snapPos);
		}
		return clone;
	}

	@Override
	public ID  getId() 
	{
		return null;
	}

    @Override
    public List<ID> dependOf() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

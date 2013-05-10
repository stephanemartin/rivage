package fr.inria.rivage.engine.operations;

import fr.inria.rivage.elements.GSnapPoint;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.interfaces.ISnappable;
import fr.inria.rivage.elements.interfaces.ISnapper;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.geom.Point2D;
import java.util.List;


public class SnapOperation extends Operation {

	private ID idSnapper, idSnappable;
	private int idSnapPoint;
	private PointDouble pos;
        private boolean snapOrUnsnap;

    public SnapOperation() {
    }
	private double pointTolerance, objectTolerance;
	
	// undo information:
	private transient PointDouble oldPos;
	private transient ISnappable oldSnappable;
	
	public SnapOperation(ID idSnappable, ID idSnapper, int idSnapPoint, Point2D pos, double pointTolerance, double objectTolerance) {
		this.idSnappable = idSnappable;
		this.idSnapper = idSnapper;
		this.idSnapPoint = idSnapPoint;
		this.pos = new PointDouble(pos);
		this.pointTolerance = pointTolerance;
		this.objectTolerance = objectTolerance;
	}
	
    @Override
	protected void doApply(FileController fc) throws UnableToApplyException {
		ISnapper snapper = getSnapper(fc);
		GSnapPoint snapPoint = getSnapPoint(snapper);
		ISnappable snappable = getSnappable(fc);
		
		oldPos = new PointDouble(snapPoint);
		oldSnappable = snapPoint.getSnappedObject();
		
		snappable.getSnapManager().snap(snapPoint, pos, pointTolerance, objectTolerance);
	}
	
    @Override
	protected void doUnapply(FileController fc) throws UnableToApplyException {
		ISnapper snapper = getSnapper(fc);
		GSnapPoint snapPoint = getSnapPoint(snapper);
		
		if (oldSnappable == null) {
			snapPoint.setSnappedPoint(null);
			snapPoint.setLocation(oldPos);
		} else {
			oldSnappable.getSnapManager().snap(snapPoint, oldPos, 0, 0);
		}
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
	
	private ISnappable getSnappable(FileController fc) throws UnableToApplyException {
		ISnappable snappable;
		try {
			snappable = (ISnappable) fc.getDocument().getObjectById(idSnappable);
		
		} catch (ClassCastException ex) {
			throw new UnableToApplyException(
					"Object is not snappable.",
					UnableToApplyException.CRITICAL_ERROR);
		}
		return snappable;
	}
	
	@Override
	public Object clone() {
		SnapOperation clone = new SnapOperation(idSnappable, idSnapper, idSnapPoint, pos, pointTolerance, objectTolerance);
		clone.setApplied(this.isApplied());
		if (oldPos != null) {
			clone.oldPos = new PointDouble(oldPos);
		}
		clone.oldSnappable = oldSnappable;
		return clone;
	}

	@Override
	public ID getId() {
			return null;
	}

    @Override
    public List<ID> dependOf() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

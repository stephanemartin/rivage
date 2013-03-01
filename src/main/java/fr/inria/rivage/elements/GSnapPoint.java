package fr.inria.rivage.elements;

import fr.inria.rivage.elements.interfaces.IHasTransform;
import fr.inria.rivage.elements.interfaces.ISnappable;
import fr.inria.rivage.elements.interfaces.ISnapper;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;


/**
 * This class describes a point that can snap an object. This class
 * should get used by snapper objects.
 * @author Tobias Kuhn
 * @see geditor.elements.interfaces.ISnapper
 */
public class GSnapPoint extends Point2D implements Serializable {
	
	private SnapManager.SnappablePoint snappedPoint;
	private ISnapper snapper;
	private double x, y;
	
	/**
	 * Creates a new snap point.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public GSnapPoint(ISnapper snapper, double x, double y) {
		this.snapper = snapper;
		setLocation(x, y);
	}
	
	/**
	 * Creates a new snap point.
	 * @param p the position
	 */
	
        public GSnapPoint(ISnapper snapper, Point2D p) {
		this(snapper, p.getX(), p.getY());
	}
	
	/**
	 * Creates a new snap point at the position (0, 0).
	 */
	public GSnapPoint(ISnapper snapper) {
		this(snapper, 0, 0);
	}
	
	/**
	 * Sets the point which this snap point should snap. It stops this
	 * snap point snapping if <code>point</code> is null.
	 * @param point the new point to snap or null for unsnapping
	 */
	public void setSnappedPoint(SnapManager.SnappablePoint point) {
		if (snappedPoint != null) {
			snappedPoint.released();
			if (point == null) {
				setLocation(snappedPoint.getX(), snappedPoint.getY());
			}
		}
		snappedPoint = point;
	}
	
	/**
	 * Returns true if this snap point is currently snapping.
	 * @return true if this snap point is currently snapping.
	 */
	public boolean isSnapping() {
		return (snappedPoint != null);
	}
	
	@Override
	public double getX() {
		if (snappedPoint == null) return x;
		if (snapper instanceof IHasTransform) {
			try {
				return ((IHasTransform) snapper).getTransform().inverseTransform(snappedPoint, null).getX();
			} catch (NoninvertibleTransformException ex) {}
		} else {
			return snappedPoint.getX();
		}
		return 0;
	}
	
	@Override
	public double getY() {
		if (snappedPoint == null) return y;
		if (snapper instanceof IHasTransform) {
			try {
				return ((IHasTransform) snapper).getTransform().inverseTransform(snappedPoint, null).getY();
			} catch (NoninvertibleTransformException ex) {}
		} else {
			return snappedPoint.getY();
		}
		return 0;
	}
	
	@Override
	public void setLocation(double x, double y) {
		/*if (snapper instanceof IHasTransform) {
			PointDouble p = new PointDouble();
			try {
				((IHasTransform) snapper).getTransform().inverseTransform(new PointDouble(x, y), p);
			} catch (NoninvertibleTransformException ex) {}
			this.x = p.x;
			this.y = p.y;
		} else {
			this.x = x;
			this.y = y;
		}
		if (snappedPoint != null) {
			snappedPoint.released();
			snappedPoint = null;
		}*/
	}
	
	/**
	 * Returns the object that is snapped or null if this snap point is not
	 * snapping.
	 * @return the object that is snapped by this snap point.
	 */
	public ISnappable getSnappedObject() {
		if (snappedPoint == null) return null;
		return snappedPoint.getSnappableObject();
	}
	
	public PointDouble getRelPos() {
		if (snappedPoint == null) return null;
		return snappedPoint.getRelPos();
	}
	
}

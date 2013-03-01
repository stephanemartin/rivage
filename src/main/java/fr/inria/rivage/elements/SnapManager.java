package fr.inria.rivage.elements;

import fr.inria.rivage.elements.interfaces.ISnappable;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class manages the snap points for a snappable object. It registers all
 * the snapping points and calculates for them their current position.
 *
 * @author Tobias Kuhn
 * @see geditor.elements.interfaces.ISnappable
 */
public class SnapManager implements Serializable {

    public static final int NO_POINTS = 0x00;
    public static final int CENTER_POINT = 0x01;
    public static final int DIAMOND_POINTS = 0x02;
    public static final int CORNER_POINTS = 0x04;
    public static final int CIRCLE_INTER_POINTS = 0x08;
    public static final int RECTANGLE_POINTS = DIAMOND_POINTS | CORNER_POINTS;
    public static final int CIRCLE_POINTS = DIAMOND_POINTS | CIRCLE_INTER_POINTS;
    private ISnappable snappable;
    private ArrayList<GSnapPoint> snapPoints = new ArrayList<GSnapPoint>();
    private ArrayList<PointDouble> predefSnappablePoints = new ArrayList<PointDouble>();
    private boolean freeSnappable = false;

    /**
     * This class gets used by
     * <code>GSnapPoint</code> for getting the current position of a snap point
     * and for telling if the snap point stops snapping.
     *
     * @author Tobias Kuhn
     */
    public class SnappablePoint extends Point2D implements Serializable {

        private GSnapPoint snapPoint;
        private PointDouble relPosition;

        private SnappablePoint(GSnapPoint snapPoint, PointDouble relPosition) {
            this.snapPoint = snapPoint;
            this.relPosition = relPosition;
        }

        /**
         * Returns the current x coordinate of this point.
         *
         * @return the x coordinate
         */
        @Override
        public double getX() {
            return getCoordinateTransform().transform(relPosition, null).getX();
        }

        /**
         * Returns the current y coordinate of this point.
         *
         * @return the y coordinate
         */
        @Override
        public double getY() {
            return getCoordinateTransform().transform(relPosition, null).getY();
        }

        /**
         * Tells this point, that the snap point has released.
         */
        public void released() {
            snapPoints.remove(snapPoint);
        }

        @Override
        public void setLocation(double x, double y) {
        }

        /**
         * Returns the snappable object it belongs to.
         *
         * @return the snappable object.
         */
        public ISnappable getSnappableObject() {
            return snappable;
        }

        public PointDouble getRelPos() {
            return new PointDouble(relPosition);
        }
    }

    /**
     * Creates a new SnapManager for the snappable object.
     *
     * @param snappable
     */
    public SnapManager(ISnappable snappable) {
        this.snappable = snappable;
    }

    /**
     * Creates a new SnapManager for the snappable object.
     *
     * @param snappable the snappable object
     * @param isFreeSnappable set true if every point inside should be snappable
     * @param predefPoints indicates which predefined snappable points to set
     */
    public SnapManager(ISnappable snappable, boolean isFreeSnappable, int predefPoints) {
        this.snappable = snappable;
        setFreeSnappable(isFreeSnappable);
        if ((predefPoints & CENTER_POINT) != 0) {
            addPredefinedSnappablePoint(new PointDouble(0.5, 0.5));
        }
        if ((predefPoints & DIAMOND_POINTS) != 0) {
            addPredefinedSnappablePoint(new PointDouble(0, 0.5));
            addPredefinedSnappablePoint(new PointDouble(1, 0.5));
            addPredefinedSnappablePoint(new PointDouble(0.5, 0));
            addPredefinedSnappablePoint(new PointDouble(0.5, 1));
        }
        if ((predefPoints & CORNER_POINTS) != 0) {
            addPredefinedSnappablePoint(new PointDouble(0, 0));
            addPredefinedSnappablePoint(new PointDouble(0, 1));
            addPredefinedSnappablePoint(new PointDouble(1, 0));
            addPredefinedSnappablePoint(new PointDouble(1, 1));
        }
        if ((predefPoints & CIRCLE_INTER_POINTS) != 0) {
            double d = (1 / Math.sqrt(2)) / 2;
            addPredefinedSnappablePoint(new PointDouble(.5 - d, .5 - d));
            addPredefinedSnappablePoint(new PointDouble(.5 - d, .5 + d));
            addPredefinedSnappablePoint(new PointDouble(.5 + d, .5 - d));
            addPredefinedSnappablePoint(new PointDouble(.5 + d, .5 + d));
        }
    }

    /**
     * Returns true if the object is snappable at the given position.
     *
     * @param position the position to snap
     * @param pointTolerance the tolerance for points
     * @return true if the object is snappable
     */
    public boolean isSnappableAtPosition(Point2D position, double pointTolerance, double objectTolerance) {
        if (freeSnappable && (((GObjectShape)snappable).getObjectByPoint(position, objectTolerance)!=null )){
            return true;
        }
        for (PointDouble p : predefSnappablePoints) {
            if (position.distance(relToAbs(p)) <= pointTolerance) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes the given snap point snapping the snappable object of this
     * SnapManager.
     *
     * @param snapPoint the snap point
     * @param position the position to snap (in absolute coordinates)
     * @param pointTolerance the tolerance for points
     */
    public void snap(GSnapPoint snapPoint, Point2D position, double pointTolerance, double objectTolerance) {
        for (PointDouble p : predefSnappablePoints) {
            if (position.distance(relToAbs(p)) <= pointTolerance) {
                snapPoint.setSnappedPoint(new SnappablePoint(snapPoint, p));
                snapPoints.add(snapPoint);
                return;
            }
        }

        if (freeSnappable && ((GObjectShape)snappable).getObjectByPoint(position, objectTolerance)!=null) {
            snapPoint.setSnappedPoint(new SnappablePoint(snapPoint, absToRel(position)));
            snapPoints.add(snapPoint);
            return;
        }
    }

    public void snap(GSnapPoint snapPoint, double relX, double relY) {
        snapPoint.setSnappedPoint(new SnappablePoint(snapPoint, new PointDouble(relX, relY)));
        snapPoints.add(snapPoint);
    }

    /**
     * Sets whether the object should be able to get snapped on every point
     * inside the object or not.
     *
     * @param freeSnappable true if every point inside should be snappable
     */
    public void setFreeSnappable(boolean freeSnappable) {
        this.freeSnappable = freeSnappable;
    }

    /**
     * Adds a predefined snappable point.
     *
     * @param relPosition the relative position of the new predefined snappable
     * point
     */
    public void addPredefinedSnappablePoint(Point2D relPosition) {
        predefSnappablePoints.add(new PointDouble(relPosition));
    }

    /**
     * Draws the selection marks for the predefined points.
     *
     * @param g the graphics device to draw on
     */
    public void drawPoints(Graphics2D g) {
        AffineTransform oldaf = g.getTransform();
        g.transform(getCoordinateTransform());
        for (Point2D p : predefSnappablePoints) {
            GraphicUtils.drawHandle(g, p.getX(), p.getY());
        }
        g.setTransform(oldaf);
    }

    /**
     * Removes all snap points that are currently snapping.
     */
    @SuppressWarnings("unchecked")
    public void removeAllSnapPoints() {
        for (GSnapPoint p : (ArrayList<GSnapPoint>) snapPoints.clone()) {
            p.setSnappedPoint(null);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GSnapPoint> getSnapPoints() {
        return (ArrayList<GSnapPoint>) snapPoints.clone();
    }

    private PointDouble relToAbs(Point2D relPoint) {
        PointDouble absPoint = new PointDouble();
        getCoordinateTransform().transform(relPoint, absPoint);
        return absPoint;
    }

    private PointDouble absToRel(Point2D absPoint) {
        PointDouble relPoint = new PointDouble();
        try {
            getCoordinateTransform().inverseTransform(absPoint, relPoint);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        return relPoint;
    }

    private AffineTransform getCoordinateTransform() {
        AffineTransform a = new AffineTransform(snappable.getTransform());
        /*Rectangle2D bounds =  ((GObjectShape)snappable).getBounds2D();
        a.concatenate(AffineTransform.getTranslateInstance(bounds.getX(), bounds.getY()));
        a.concatenate(AffineTransform.getScaleInstance(bounds.getWidth(), bounds.getHeight()));*/
        return a;
    }
}

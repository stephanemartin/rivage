/*
 * Created on May 6, 2004
 *
 */
package fr.inria.rivage.elements;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * @author Yves
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GBounds2D extends Rectangle2D.Double implements Serializable {
    public static class Center extends GBounds2D {
        public Center(Point2D p,double w,double h){
            super(p.getX()-w/2.0,p.getY()-h/2.0,w,h);
        }
    }
    /**
     * The x coordinate of this
     * <code>GBounds2D</code>.
     *
     * @since 1.2
     */
    //public double x;
    /**
     * The y coordinate of this
     * <code>GBounds2D</code>.
     *
     * @since 1.2
     */
    //public double y;
    /**
     * The width of this
     * <code>GBounds2D</code>.
     *
     * @since 1.2
     */
    //public double width;
    /**
     * The height of this
     * <code>GBounds2D</code>.
     *
     * @since 1.2
     */
    //public double height;
    /**
     * Constructs a new
     * <code>GBounds2D</code>, initialized to location (0,&nbsp;0) and size
     * (0,&nbsp;0).
     *
     * @since 1.2
     */
    /*public GBounds2D() {
     x = 0;
     y = 0;
     width = 0;
     height = 0;
     }*/
    public GBounds2D() {
        super();
    }

    /**
     * Constructs and initializes a
     * <code>GBounds2D</code> from the specified double coordinates.
     *
     * @param x the coordinates of the upper left corner of the newly
     * constructed <code>Rectangle2D</code>
     * @param y the coordinates of the upper left corner of the newly
     * constructed <code>Rectangle2D</code>
     * @param w the width of the newly constructed <code>Rectangle2D</code>
     * @param h the height of the newly constructed <code>Rectangle2D</code>
     * @since 1.2
     */
    public GBounds2D(double x, double y, double w, double h) {
        super(x, y, w, h);
    }
    /*public GBounds2D(double x, double y, double w, double h) {
     setRect(x, y, w, h);
     }*/

    /**
     * Constructs and initializes a
     * <code>GBounds2D</code> from the specified double coordinates and anchor
     * point.
     *
     * @param p the point at the upper left corner of the newly constructed
     * <code>Rectangle2D</code>
     * @param w the width of the newly constructed <code>Rectangle2D</code>
     * @param h the height of the newly constructed <code>Rectangle2D</code>
     * @since 1.2
     */
    public GBounds2D(Point2D p, double w, double h) {
        super(p.getX(), p.getY(), w, h);
    }

    public GBounds2D(Point2D p, Point2D dim) {
        super(p.getX(), p.getY(), dim.getX(), dim.getY());
    }

    /**
     * Constructs and initializes a
     * <code>GBounds2D</code> entirely enclosing the given shape.
     *
     * @param s the shape to be enclosed
     */
    public GBounds2D(Shape s) {
        super();
        this.setRect(s.getBounds2D());
    }

    /**
     * Returns the
     * <code>String</code> representation of this
     * <code>Rectangle2D</code>.
     *
     * @return a <code>String</code> representing this <code>Rectangle2D</code>.
     * @since 1.2
     */
    @Override
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + ",w=" + width + ",h=" + height + "]";
    }

    /**
     * Moves the bounds in direction
     * <code>xdirection</code> and
     * <code>ydirection</code>.
     *
     * @param xdirection the distance in x direction.
     * @param ydirection the distance in y direction.
     */
    public void translate(double xdirection, double ydirection) {
        x += xdirection;
        y += ydirection;
    }

    /**
     * Scales the bounds around the top-left corner, meaning the x,y of this
     * bound.
     *
     * @param xscale the factor in x direction.
     * @param yscale the factor in y direction.
     */
    public void scale(double xscale, double yscale) {
        width *= xscale;
        height *= yscale;
    }

    public void scale(Point2D center, double xscale, double yscale) {
        // 1) translate the rectangle to the coordinates of you point
        x = x - center.getX();
        y = y - center.getY();
        // 2) do the scaling
        x = x * xscale;
        y = y * yscale;
        width = xscale * width;
        height = yscale * height;
        // 3) translate back the rectangle
        x = x + center.getX();
        y = y + center.getY();
    }

    /**
     * @param height The height to set.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @param x The x to set.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y The y to set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Conserves width and height and sets the rectangle to a new center set by
     * the x and y coordinates
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     */
    public void setCenter(double x, double y) {
        setX(x - getWidth() / 2);
        setY(y - getHeight() / 2);
    }

    public void setToContain(Point2D p1, Point2D p2) {
        setX(p1.getX());
        setY(p1.getY());
        setHeight(0);
        setWidth(0);
        add(p2);
    }

    public PointDouble getCenter() {
        return new PointDouble(getCenterX(), getCenterY());
    }

    public PointDouble getDimension() {
        return new PointDouble(getWidth(), getHeight());
    }
    

    public PointDouble getTopLeftPoint() {
        return new PointDouble(getMinX(), getMinY());
    }

    public PointDouble getBottomRightPoint() {
        return new PointDouble(getMaxX(), getMaxY());
    }

    public boolean isInside(Point2D p, double tolerance) {
        return isInside(new Rectangle2D.Double(p.getX() - tolerance / 2.0, p.getY() - tolerance / 2.0, tolerance, tolerance));
    }

    public boolean isInside(Rectangle2D rec) {
        return this.intersects(rec);
    }
}
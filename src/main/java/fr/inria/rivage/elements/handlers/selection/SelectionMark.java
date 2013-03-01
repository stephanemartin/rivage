package fr.inria.rivage.elements.handlers.selection;

import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.tools.Configuration;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class represents a selection mark that can be used for moving or
 * resizing a graphical object.
 *
 * @author Tobias Kuhn
 */
public class SelectionMark {

    /**
     * Every selection mark has one of the following types: NW, N, NE, W, E, SW,
     * S, SE which are for resizing the object. And CENTER which is for moving
     * the object.
     */
    public static enum SelType {

        NW, N, NE, W, CENTER, E, SW, S, SE;
    }
    private final SelectedObject selObj;
    private final SelType type;

    /**
     * Creates a set of nine selection marks (one for each type) for the given
     * SelectedObject.
     *
     * @param selObj the object to generate the selection marks for
     * @return a list of nine selection marks
     */
    public static ArrayList<SelectionMark> createSelectionMarks(SelectedObject selObj) {
        ArrayList<SelectionMark> list = new ArrayList<SelectionMark>();
        list.add(new SelectionMark(selObj, SelType.CENTER));
        list.add(new SelectionMark(selObj, SelType.N));
        list.add(new SelectionMark(selObj, SelType.S));
        list.add(new SelectionMark(selObj, SelType.W));
        list.add(new SelectionMark(selObj, SelType.E));
        list.add(new SelectionMark(selObj, SelType.NW));
        list.add(new SelectionMark(selObj, SelType.NE));
        list.add(new SelectionMark(selObj, SelType.SW));
        list.add(new SelectionMark(selObj, SelType.SE));
        return list;
    }

    public SelectionMark(SelectedObject selObj, SelType type) {
        this.selObj = selObj;
        this.type = type;
    }

    /**
     * Returns the type of this selection mark.
     *
     * @return the type of this selection mark
     */
    public SelType getType() {
        return type;
    }

    /**
     * Returns the original object of the underlying SelectedObject.
     *
     * @return the original object of the underlying SelectedObject
     */
    public GObjectShape getOriginal() {
        return selObj.getOriginal();
    }

    /**
     * Returns the copy of the underlying SelectedObject.
     *
     * @return the copy of the underlying SelectedObject
     */
    public GObjectShape getCopy() {
        return selObj.getCopy();
    }

    /**
     * Returns the current position of this selection mark with respect to the
     * copy of the underlying SelectedObject.
     *
     * @return the current position with respect to the copy
     */
    public Point2D getPosition() {
        return getPos(selObj.getCopy().getScreenBounds2D());
    }

    /**
     * Returns the current position of this selection mark with respect to the
     * original object of the underlying SelectedObject.
     *
     * @return the current position with respect to the original object
     */
    public Point2D getOriginalPosition() {
        return getPos(selObj.getOriginal().getScreenBounds2D());
    }

    private Point2D getPos(Rectangle2D b) {
        switch (type) {
            case NW:
                return new PointDouble(b.getMinX(), b.getMinY());
            case N:
                return new PointDouble(b.getCenterX(), b.getMinY());
            case NE:
                return new PointDouble(b.getMaxX(), b.getMinY());
            case W:
                return new PointDouble(b.getMinX(), b.getCenterY());
            case CENTER:
                return new PointDouble(b.getCenterX(), b.getCenterY());
            case E:
                return new PointDouble(b.getMaxX(), b.getCenterY());
            case SW:
                return new PointDouble(b.getMinX(), b.getMaxY());
            case S:
                return new PointDouble(b.getCenterX(), b.getMaxY());
            case SE:
                return new PointDouble(b.getMaxX(), b.getMaxY());
        }
        return null;
    }

    /**
     * Draws this selection mark onto the given graphical device.
     *
     * @param g the graphical device to draw on
     */
    public void draw(Graphics2D g) {
        int s = Configuration.getConfiguration().SEL_MARK_SIZE;
        Point2D pos = getPosition();
        GraphicUtils.drawHandle(g, pos.getX(), pos.getY());
    }

    /**
     * Returns the cursor that should be shown when the mouse is moved onto this
     * selection mark.
     *
     * @return the cursor to show
     */
    public Cursor getCursor() {
        switch (type) {
            case NW:
                return Cursors.resizeNW;
            case N:
                return Cursors.resizeN;
            case NE:
                return Cursors.resizeNE;
            case W:
                return Cursors.resizeW;
            case CENTER:
                return Cursors.move;
            case E:
                return Cursors.resizeE;
            case SW:
                return Cursors.resizeSW;
            case S:
                return Cursors.resizeS;
            case SE:
                return Cursors.resizeSE;
        }
        return null;
    }

    /**
     * Returns true if this selection mark is currently visible. Depending on
     * the size and the properties of the underlying object, some selection
     * marks might not be visible.
     *
     * @return true if this selection mark is visible
     */
    public boolean isVisible() {
        /*	if (!selObj.getOriginal().isMovable()) {
         return false;
         }*/
        return true;/*
         if (type == SelType.CENTER) {
         return true;
         }
         /*if (!selObj.getOriginal().isResizable()) {
         return false;
         }*
         Rectangle2D b = selObj.getOriginal().getScreenBounds2D();
         switch (type) {
         case NW : return !b.isEmpty();
         case N : return b.getHeight() > 0;
         case NE : return !b.isEmpty();
         case W : return b.getWidth() > 0;
         case E : return b.getWidth() > 0;
         case SW : return !b.isEmpty();
         case S : return b.getHeight() > 0;
         case SE : return !b.isEmpty();
         }
         return false;*/
    }
}

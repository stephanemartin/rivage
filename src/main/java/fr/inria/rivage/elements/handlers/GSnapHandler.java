package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.GSnapPoint;
import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.interfaces.ISnappable;
import fr.inria.rivage.elements.interfaces.ISnapper;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.operations.SnapOperation;
import fr.inria.rivage.engine.operations.UnsnapOperation;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * This class handles the snapping and unsnapping of points. First the user has
 * to move onto an object that is a snapper. Then he has to click on a point an
 * drag the mouse onto a snappable object and release the mouse on a snappable
 * point. In that way the first point snaps the second one. If the first point
 * was already snapping and the mouse gets released on a position with no snap
 * point then the point gets unsnapped.
 *
 * @author Tobias Kuhn
 * @see geditor.elements.interfaces.ISnapper
 * @see geditor.elements.interfaces.ISnappable
 * @see geditor.elements.GSnapPoint
 */
public class GSnapHandler extends GHandler {

    private WorkArea wa;
    private ISnapper snapper;
    private ISnappable snappable;
    private Point2D p1, p2;
    private int pIndex = -1;

    GSnapHandler() {
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        snapper = null;
        snappable = null;
        p1 = null;
        p2 = null;
        pIndex = -1;
        wa.getSelectionManager().clearSelection();
        wa.setCursor(Cursors.normal);
    }

    @Override
    public void draw(Graphics2D g) {
        GraphicUtils.setSelectionColor(g);
        if (snapper != null) {
         //   ((GObjectShape) snapper).drawSelectionMark(g, wa.getMode());
        }
        if (snappable != null) {
           // ((GObjectShape) snappable).drawSelectionMark(g, wa.getMode());
        }
        g.setPaintMode();

        AffineTransform a = g.getTransform();
        g.setTransform(new AffineTransform());
        Point2D sp1 = new PointDouble();
        Point2D sp2 = new PointDouble();

        if (this.p1 != null) {
            a.transform(this.p1, sp1);
            g.setColor(Color.BLACK);
            g.drawOval((int) sp1.getX() - 5, (int) sp1.getY() - 5, 10, 10);
            if (this.p2 != null) {
                a.transform(this.p2, sp2);
                g.drawLine((int) sp1.getX(), (int) sp1.getY(), (int) sp2.getX(), (int) sp2.getY());
                if (snappable != null && snappable.getSnapManager().isSnappableAtPosition(this.p2, wa.getPointTolerance(), wa.getObjectTolerance())) {
                    g.drawOval((int) sp2.getX() - 5, (int) sp2.getY() - 5, 10, 10);
                }
            }
        }

        g.setTransform(a);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        Point2D point = wa.getDrawingPoint(e.getPoint());
        ISnapper oldSnapper = snapper;

        GObjectShape obj =(GObjectShape) wa.getPage().getObjectByPoint(point, wa.getObjectTolerance());
        if (obj instanceof ISnapper) {
            snapper = (ISnapper) obj;
            wa.setCursor(Cursors.hand);
        } else {
            snapper = null;
            wa.setCursor(Cursors.normal);
        }

        /*if (oldSnapper != null) {
            int index = ((GObjectShape)oldSnapper).getPointIndex(point, wa.getPointTolerance());
            if (oldSnapper.getSnapPoint(index) != null) {
                snapper = oldSnapper;
            }
        }*/

        wa.lightRepaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        Point2D point = wa.getDrawingPoint(e.getPoint());
        ISnappable oldSnappable = snappable;

        GObjectShape obj = (GObjectShape) wa.getPage().getObjectByPoint(point, wa.getObjectTolerance());
        if (obj instanceof ISnappable) {
            snappable = (ISnappable) obj;
        } else {
            snappable = null;
        }

        if (oldSnappable != null && oldSnappable.getSnapManager().isSnappableAtPosition(point, wa.getPointTolerance(), wa.getObjectTolerance())) {
            snappable = oldSnappable;
        }

        p2 = point;

        wa.lightRepaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (snapper == null) {
            exit();
        } else {
            p1 = wa.getDrawingPoint(e.getPoint());

            /*int i = ((GObjectShape)snapper).getPointIndex(p1, wa.getPointTolerance());
            if (snapper.getSnapPoint(i) != null) {
                pIndex = i;
            }

            if (pIndex == -1) {
                exit();
            }*/
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point2D point = wa.getDrawingPoint(e.getPoint());
        if (snapper == null || pIndex == -1) {
            return;
        }
        GSnapPoint s = snapper.getSnapPoint(pIndex);
        boolean isSnappable = false;
        if (snappable != null) {
            isSnappable = snappable.getSnapManager().isSnappableAtPosition(
                    point,
                    wa.getPointTolerance(),
                    wa.getObjectTolerance());
        }

        IConcurrencyController cc = wa.getFileController().getConcurrencyController();
        if (isSnappable) {
            cc.doAndSendOperation(new SnapOperation(
                    ((GObjectShape)snappable).getId(),
                   ((GObjectShape) snapper).getId(),
                    pIndex,
                    point,
                    wa.getPointTolerance(),
                    wa.getObjectTolerance()));
        } else if (s.isSnapping() && !isSnappable) {
            cc.doAndSendOperation(new UnsnapOperation(
                    ((GObjectShape)snapper).getId(),
                    pIndex));
        }
        exit();
    }

    private void exit() {
        wa.setMode(Handlers.SELECTION);
        wa.repaint();
    }
}

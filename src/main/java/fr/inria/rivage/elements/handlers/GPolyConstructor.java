package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.shapes.GPath;
import fr.inria.rivage.elements.shapes.GPointPath;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GPolyConstructor extends GHandler {

    private WorkArea wa;
    private ArrayList<PointDouble> points;
    private PointDouble lastPoint;
    private boolean closed = false;
    private int nbPoint = -1;

    public GPolyConstructor(boolean closed) {
        this.closed = closed;
    }

    public GPolyConstructor(boolean closed, int nbPoint) {
        this.closed = closed;
        this.nbPoint = nbPoint;
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        this.points = new ArrayList<PointDouble>();
        lastPoint = null;
        wa.getSelectionManager().clearSelection();
        wa.setCursor(Cursors.crosshair);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (lastPoint != null) {
            lastPoint = new PointDouble(wa.getDrawingPoint(e.getPoint()));
            wa.lightRepaint();
        }
    }

    /**
     * Handles the clicking events of the user. Left mouse button to plus a
     * segment to the clicked point. Right button to go back to the normal usage
     * mode.
     *
     * @param e MouseEvent the mousevent to handle
     *
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            PointDouble p = new PointDouble(wa.getDrawingPoint(e.getPoint()));
            points.add(p);
            lastPoint = p;
            wa.lightRepaint();
        } 
        if (e.getButton() == MouseEvent.BUTTON3 || nbPoint==points.size()) {
            if (points.size() > 1) {
                GPath gpl = new GPath(wa.getActiveLayer(),
                        (List) points.clone(),
                        wa.getCurrentFrtColor(),
                        wa.getCurrentBckColor(),
                        wa.getCurrentStroke(),
                        closed);


                wa.getFileController().getConcurrencyController().doAndSendOperation(
                        wa.getCreateOperation(gpl));
                Position lp = null;
                for (Point2D point : points) {
                    GPointPath gp = new GPointPath(gpl, point);
                    wa.getFileController().getConcurrencyController().doAndSendOperation(
                            wa.getCreateOperation(gp, lp));
                    //gpl.add(gp);
                    lp = gp.getParameters().getPosition(Parameters.ParameterType.Zpos);
                }
            }
            this.init(wa);
            //wa.setMode(Handlers.SELECTION);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            lastPoint = null;
            points.clear();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(wa.getCurrentStroke());
        g.setColor(wa.getCurrentFrtColor());
        PointDouble p1 = null;
        for (PointDouble p2 : points) {
            if (p1 != null) {
                g.draw(new Line2D.Double(p1, p2));
            }
            p1 = p2;
        }
        if (lastPoint != null && p1 != null) {
            g.draw(new Line2D.Double(p1, lastPoint));
        }
    }
}

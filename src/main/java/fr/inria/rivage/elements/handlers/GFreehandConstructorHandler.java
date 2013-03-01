/*
 * Created on May 26, 2004
 */
package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.shapes.GPath;
import fr.inria.rivage.elements.shapes.GPointPath;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yves
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GFreehandConstructorHandler extends GHandler {

    ArrayList<PointDouble> plist = null;
    WorkArea wa = null;

    GFreehandConstructorHandler() {
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        wa.getSelectionManager().clearSelection();
        wa.setCursor(Cursors.freehand);
        plist = new ArrayList<PointDouble>();
        wa.getSelectionManager().clearSelection();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(wa.getCurrentStroke());
        g.setColor(wa.getCurrentFrtColor());
        PointDouble p1 = null;
        for (PointDouble p2 : plist) {
            if (p1 != null) {
                g.draw(new Line2D.Double(p1, p2));
            }
            p1 = p2;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        plist.add(new PointDouble(wa.getDrawingPoint(e.getPoint())));
        if (plist.size() > 3) {
            double x0, x1, x2, y0, y1, y2, d;
            Point2D p1 = wa.getScreenPoint((Point2D) plist.get(plist.size() - 3));
            Point2D p0 = wa.getScreenPoint((Point2D) plist.get(plist.size() - 2));
            Point2D p2 = wa.getScreenPoint((Point2D) plist.get(plist.size() - 1));

            x0 = p0.getX();
            y0 = p0.getY();
            x1 = p1.getX();
            y1 = p1.getY();
            x2 = p2.getX();
            y2 = p2.getY();

            d = Math.abs((x2 - x1) * (y1 - y0) - (x1 - x0) * (y2 - y1))
                    / Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

            if (d < 1.5) {
                plist.remove(plist.size() - 2);
            }
        }
        wa.lightRepaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        plist.add(new PointDouble(wa.getDrawingPoint(e.getPoint())));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (plist.size() < 1) {
            return;
        }
        GPath gpl = new GPath(wa.getActiveLayer(),
                (List) plist.clone(),
                wa.getCurrentFrtColor(),
                wa.getCurrentBckColor(),
                wa.getCurrentStroke(),
                false);

        wa.getFileController().getConcurrencyController().doAndSendOperation(
                wa.getCreateOperation(gpl));
        Position lp = null;
        for (Point2D point : plist) {
            GPointPath gp = new GPointPath(gpl, point);
            wa.getFileController().getConcurrencyController().doAndSendOperation(
                    wa.getCreateOperation(gp, lp));
          //  gpl.add(gp);
            lp = gp.getParameters().getPosition(Parameters.ParameterType.Zpos);
        }

        plist = new ArrayList<PointDouble>();
        wa.repaint();
    }
}

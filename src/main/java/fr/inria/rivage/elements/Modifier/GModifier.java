/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package fr.inria.rivage.elements.Modifier;

import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GModifier extends GHandler implements IModifier {

    public static GModifier generateBox(Parameters param) {
        List<GMutablePoint> ret = new ArrayList();
        ret.add(new GMovablePoint(param, -1, -1));
        ret.add(new GMovablePoint(param, -1, 1));
        ret.add(new GMovablePoint(param, 1, 1));
        ret.add(new GMovablePoint(param, 1, -1));

        List<GMutablePoint> extra = new ArrayList();
       // extra.add(new GRotatePoint(param));
        /* ret.get(0).setOppositePoint(ret.get(2));
         ret.get(2).setOppositePoint(ret.get(0));
         ret.get(1).setOppositePoint(ret.get(3));
         ret.get(3).setOppositePoint(ret.get(1));*/


        return new GModifier(ret, extra, Color.blue, param);
    }
    
    public static GModifier getLine(Parameters param) {
        List<GMutablePoint> ret = new LinkedList();
        ret.add(new GMovablePoint(param, -1, -1));
        ret.add(new GMovablePoint(param, 1, 1));
        return new GModifier(ret, null, null, param);
    }
    List<GMutablePoint> mutable;
    List<GMutablePoint> extra;
    Color boxed;
    WorkArea wa;
    Parameters param;
    Point2D origin;
    GeneralPath shape;

    public GModifier() {
    }

    public GModifier(List<GMutablePoint> mutable, Color boxed, Parameters param) {
        this.mutable = mutable;
        this.boxed = boxed;
        this.param = param;
    }

    public void addExtra(GMutablePoint mut) {
        this.extra.add(mut);
    }

    public GModifier(List<GMutablePoint> mutable, List<GMutablePoint> extra, Color boxed, Parameters param) {
        this(mutable, boxed, param);
        this.extra = extra;
    }

    private boolean inShape(Point2D pc, double tolerance) {
        shape = new GeneralPath();
        Rectangle2D rec = new Rectangle2D.Double(pc.getX() - tolerance / 2.0, pc.getY() - tolerance / 2.0, tolerance, tolerance);
        if (mutable.size() > 0) {
            Point2D p2 = mutable.get(0).getRotatedPoint();
            shape.moveTo(p2.getX(), p2.getY());
        }
        for (int i = 1; i < mutable.size(); i++) {
            Point2D p2 = mutable.get(i).getRotatedPoint();
            shape.lineTo(p2.getX(), p2.getY());
        }
        shape.closePath();
        return shape.intersects(rec);
    }

    @Override
    public void draw(Graphics2D g) {
        //System.out.println("===draw");
        Point2D po = null;
        for (GMutablePoint p : mutable) {
            p.draw(g);
            if (po != null && boxed != null) {
                GraphicUtils.drawColor(g, boxed, new Line2D.Double(po, p.getRotatedPoint()));
            }
            po = p.getRotatedPoint();
        }
        if (po != null && boxed != null) {
            GraphicUtils.drawColor(g, boxed, new Line2D.Double(po, mutable.get(0).getRotatedPoint()));
        }
        if (extra != null) {
            for (GMutablePoint p : extra) {
                p.draw(g);
            }
        }
    }

    /**
     *
     * @param p the value of p
     * @param tolerance the value of tolerance
     * @param mod the value of mod
     */
    @Override
    public GHandler getHandlerByPoint(Point2D p, double tolerance, int mod) {
        Point2D p2=p;//this.getAffineTransformInverse().transform(p, new PointDouble ());
        GHandler ret = getGHandlerByPointFromList(p, mutable);
        ret = ret == null ? getGHandlerByPointFromList(p, extra) : ret;
        return ret == null && inShape(p2, tolerance) ? this : ret;

    }

    private GHandler getGHandlerByPointFromList(Point2D p, List<GMutablePoint> mut) {
        if (mut == null) {
            return null;
        }
        for (GMutablePoint point : mut) {
            GHandler gh = point.getHandlerByPoint(p);
            if (gh != null) {
                return gh;
            }
        }
        return null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (origin == null) {
            return;
        }
        PointDouble p =this.getPointFromMouseEvent(wa,e);
        PointDouble diffp = new PointDouble(p.getX() - origin.getX(), p.getY() - origin.getY());
        this.param.addPoint(TopLeft, diffp);
        origin = p;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        origin= this.getPointFromMouseEvent(wa,e);
        //origin = wa.getDrawingPoint(e.getPoint());

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.param.sendMod();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        for (GMutablePoint gm : this.mutable) {
            gm.init(wa);
        }
        if (extra != null) {
            for (GMutablePoint gm : this.extra) {
                gm.init(wa);
            }
        }
    }

    @Override
    public String toString() {
        return "GModifier{" + "mutable=" + mutable + ", extra=" + extra + '}';
    }

    @Override
    public void setAffineTransform(AffineTransform af) {
        super.setAffineTransform(af);
        for (GMutablePoint gm : this.mutable) {
            gm.setAffineTransform(af);
        }
        if (extra != null) {
            for (GMutablePoint gm : this.extra) {
                gm.setAffineTransform(af);
            }
        }
    }

}

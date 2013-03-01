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

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.shapes.GPath;
import fr.inria.rivage.elements.shapes.GPointPath;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import fr.inria.rivage.engine.operations.DeleteOperation;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GPathModifier extends GHandler implements IModifier {

    List<GMovableAnchor> pointList;
    WorkArea wa;
    GPath gpath;
    SubPoint selected = null;
    int nbPoint;

    public GPathModifier(GPath gp) {
        this.gpath = gp;

        init();

    }

    private void init() {
        nbPoint=gpath.size();
        pointList = new LinkedList();
        GPointPath back = null;
        for (GPointPath gpp : gpath.getCollection()) {
            if (back != null) {
                pointList.add(new GMovableAnchor(new CurvePoint(back, gpp, ParameterType.Curve1), GMovableAnchor.ShapePoint.Circle, Color.GREEN));
                pointList.add(new GMovableAnchor(new CurvePoint(back, gpp, ParameterType.Curve2), GMovableAnchor.ShapePoint.Circle, Color.RED));
            }
            pointList.add(new GMovableAnchor(new SubPoint(gpp)));
            back = gpp;
        }

        if (gpath.isClosed() && back != null && gpath.size() > 1) {
            pointList.add(new GMovableAnchor(new CurvePoint(back, gpath.first(), ParameterType.Curve1), GMovableAnchor.ShapePoint.Circle, Color.GREEN));
            pointList.add(new GMovableAnchor(new CurvePoint(back, gpath.first(), ParameterType.Curve2), GMovableAnchor.ShapePoint.Circle, Color.RED));
        }
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        for (GMovableAnchor gm : pointList) {
            gm.init(wa);
        }
    }

    /**
     *
     * @param p the value of p
     * @param tolerance the value of tolerance
     * @param mod the value of mod
     */
    public GHandler getHandlerByPoint(Point2D p, double tolerance, int mod) {
        if (gpath.size() < 2) {
            return null;
        }
        AffineTransform af = gpath.getgRendreres().getGlobal();
        PointDouble tp = null;
        try {
            tp = (PointDouble) af.inverseTransform(p, new PointDouble());
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(GPathModifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (mod == MouseEvent.BUTTON1) {


            for (GMovableAnchor gm : pointList) {
                GHandler g = gm.getHandlerByPoint(p, tolerance);
                if (g != null) {
                    return g;
                }
            }

        } else {

            

            Iterator<GPointPath> it = gpath.iterator();
            GPointPath bak = it.next();
            GBounds2D gb = new GBounds2D.Center(p, tolerance, tolerance);
            while (it.hasNext()) {
                GPointPath gp = it.next();
                Shape sh = gpath.curveed(gp.getParameters(), bak.getPoint());
                sh = af.createTransformedShape(sh);
                if (sh.intersects(gb)) {

                    return new mokHandler(gp, bak, tp);

                }
                //i++;
                bak = gp;
            }
            if (gpath.isClosed() && bak != gpath.first()) {
                GPointPath gp = gpath.first();
                Shape sh = gpath.curveed(gp.getParameters(), bak.getPoint());
                sh = af.createTransformedShape(sh);
                if (sh.intersects(gb)) {
                    return new mokHandler(bak, null, tp);

                }
            }
        }
        return null;
    }

    int whereAreaPointed(PointDouble p, double tolerence) {

        return 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_DELETE && this.selected != null) {
            wa.getFileController().doAndSendOperation(new DeleteOperation(selected.pPath.getId()));
            //pointList.remove(selected.getgMovableAnchor());
            init();// could be optimized
            init(wa);
            selected = null;
            wa.lightRepaint();
            
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (nbPoint!=gpath.size()){
            init();
            init(wa);
        }
        
        for (GHandler gh : this.pointList) {
            gh.draw(g);
        }
    }

    class mokHandler extends GHandler {

        GPointPath current;
        GPointPath bak;
        Point2D p;

        public mokHandler(GPointPath bak, GPointPath current, Point2D p) {
            this.current = current;
            this.bak = bak;
            this.p = p;
        }

        @Override
        public void init(WorkArea wa) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() > MouseEvent.BUTTON1) {
                GPointPath gp = new GPointPath(gpath, p);
                wa.getFileController().getConcurrencyController().doAndSendOperation(
                        wa.getCreateOperation(gp, bak, current));
                /*Position p1=bak.getParameters().getPosition(ParameterType.Zpos);
                Position p2=gp.getParameters().getPosition(ParameterType.Zpos);
                Position p3=current==null?null:current.getParameters().getPosition(ParameterType.Zpos);
               /* System.out.println("p1:"+p1);
                System.out.println("p2:"+p2);
                System.out.println("p3:"+p3);
                
                
                System.out.print("1,-1 : "+p2.compareTo(p3)+" ");
                System.out.println(""+p2.compareTo(p1));*/
                
                
                GPathModifier.this.init();// could be optimized

                GPathModifier.this.init(wa);
                wa.lightRepaint();
            }
        }
    }

    class CurvePoint extends GMovableAnchor.PointSetterGetter {

        GPointPath g1;
        GPointPath g2;
        ParameterType param;

        public CurvePoint(GPointPath g1, GPointPath g2, ParameterType param) {
            this.g1 = g1;
            this.g2 = g2;
            this.param = param;
        }

        @Override
        public void draw(Graphics2D g) {
            PointDouble p;
            if (param == ParameterType.Curve1) {
                p = g1.getPoint();
            } else {
                p = g2.getPoint();
            }
            p = (PointDouble) GPathModifier.this.getAffineTransform().transform(p, new PointDouble());
            PointDouble p2 = this.getPoint();
            g.draw(new Line2D.Double(p, p2));
            //super.draw(g);
        }

        @Override
        public PointDouble getPoint() {
            PointDouble p = g2.getParameters().getPoint(param);
            if (p == null) {
                p = g1.getPoint().center(g2.getPoint());
            }
            return (PointDouble) GPathModifier.this.getAffineTransform().transform(p, new PointDouble());
            //return p;
        }

        @Override
        public void setPoint(PointDouble Origine, PointDouble p) {
            Point2D point = GPathModifier.this.getAffineTransformInverse().transform(p, new PointDouble());
            g2.getParameters().setObject(param, point);
        }

        @Override
        public void released(MouseEvent e) {
            g2.getParameters().sendMod();
        }
    }

    class SubPoint extends GMovableAnchor.PointSetterGetter {

        GPointPath pPath;

        public SubPoint(GPointPath p) {
            this.pPath = p;
        }

        @Override
        public void pressed(MouseEvent e, PointDouble origin) {
            boolean modified = false;
            if (selected != this) {
                selected = this;
                modified = true;
            }
            if (e.getClickCount()>1) {
                pPath.getParameters().setObject(ParameterType.Curve1, null);
                pPath.getParameters().setObject(ParameterType.Curve2, null);
                pPath.getParameters().sendMod();
                modified = true;
            }
            if (modified) {
                wa.repaint();
            }
        }

        @Override
        public void draw(Graphics2D g) {
            if (selected == this) {
                //System.out.println("ezaijfzeoijfzeoijfozeifoziejfez!!");
                g.setColor(Color.PINK);
            }

        }

        @Override
        public PointDouble getPoint() {
            return (PointDouble) GPathModifier.this.getAffineTransform().transform(pPath.getPoint(), new PointDouble());
        }

        @Override
        public void setPoint(PointDouble Origine, PointDouble p) {
            Point2D point = GPathModifier.this.getAffineTransformInverse().transform(p, new PointDouble());
            pPath.getParameters().setObject(Parameters.ParameterType.TopLeft, point);
        }

        @Override
        public void released(MouseEvent e) {
            pPath.getParameters().sendMod();
            //super.released(e);
        }
    }
}

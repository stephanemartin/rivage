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
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.renderer.AffineTransformRenderer;
import fr.inria.rivage.elements.renderer.GRenderersFeuille;
import fr.inria.rivage.elements.renderer.Renderer;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import fr.inria.rivage.gui.WorkArea;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GAffineTransformer extends GHandler implements IModifier {

    static enum RatioKind {

        noRatio, originRatio, lastRatio
    };
    RatioKind ratio = RatioKind.noRatio;
    boolean ourRendrer = false;
    LinkedList<GMovableAnchor> points;
    GObject go;
    PointDouble origin;
    WorkArea wa;
    GBounds2D bound;

    public GAffineTransformer(GObject go) {
        this.go = go;

        bound = go.getEuclidBounds();
        points = new LinkedList<GMovableAnchor>();
        points.add(new GMovableAnchor(new PointCenter(), GMovableAnchor.ShapePoint.Cross, Color.BLACK));
        points.add(new GMovableAnchor(new PointScale(ParameterType.Scale, 0, 0)));
        points.add(new GMovableAnchor(new PointScale(ParameterType.Scale, 0, 1.0)));
        points.add(new GMovableAnchor(new PointScale(ParameterType.Scale, 1.0, 0)));
        points.add(new GMovableAnchor(new PointScale(ParameterType.Scale, 1.0, 1.0)));
        points.add(new GMovableAnchor(new PointRot(), GMovableAnchor.ShapePoint.Circle, Color.CYAN));
        points.add(new GMovableAnchor(new PointShear(1.0, 0), GMovableAnchor.ShapePoint.Losange, Color.PINK));
        points.add(new GMovableAnchor(new PointShear(0, 1.0), GMovableAnchor.ShapePoint.Losange, Color.PINK));
    }

    @Override
    public void draw(Graphics2D g) {
        bound = go.getEuclidBounds();
        for (GMovableAnchor mo : points) {
            mo.draw(g);
        }

        Color bak = g.getColor();
        // g.setColor(Color.black);
        g.setColor(Color.yellow);

        g.setXORMode(Color.red);
        g.setStroke(new BasicStroke(1));
        g.draw(bound);

        //       g.setXORMode(bakXor);
        g.setColor(bak);
        //g.setTransform(a);
        g.setPaintMode();

    }

    AffineTransformRenderer getRenderer() {
        GRenderersFeuille r = go.getgRendreres();
        return (AffineTransformRenderer) r.last();
    }

    /**
     *
     * @param p the value of p
     * @param tolerance the value of tolerance
     * @param button the value of button
     */
    public GHandler getHandlerByPoint(Point2D p, double tolerance, int button) {
        bound = go.getEuclidBounds();
        for (GMovableAnchor mo : points) {
            GHandler g = mo.getHandlerByPoint(p, tolerance);
            if (g != null) {
                return g;
            }
        }

        if (bound.contains(p.getX() - tolerance / 2.0, p.getY() - tolerance / 2.0, tolerance, tolerance)) {
            return this;
        } else {
            return null;
        }
    }

    public boolean newNeeded(Parameters.ParameterType t, AffineTransformRenderer atr) {
        if (atr == null) {
            atr = this.getRenderer();
            if (atr == null) {
                return true;
            }
        }
        if (ourRendrer && !wa.getConcurrencyController().isOurID(atr.getId())) {
            return true;
        }
        return this.getRenderer().newNeeded(t, go.getId());
    }

    public AffineTransformRenderer checkAtr(Parameters.ParameterType t) {
        AffineTransformRenderer atr = this.getRenderer();
        PointDouble center = getCenter();
        if (newNeeded(t, atr)) {
            atr = go.getgRendreres().newAffineRenderer(wa.getFileController(), go.getId(),center);
        }
        /*if (/*t != Parameters.ParameterType.Center
                &&* atr.getParameters().getParameter(Parameters.ParameterType.Center) == null) {
            atr.getParameters().setObject(Parameters.ParameterType.Center, center);
            atr.getParameters().acceptMod();
            //Wishme : Send or not ?
        }*/
        return atr;

    }

    public PointDouble getCenter() {
        Renderer r = getRenderer();
        if (r == null) {
            return bound.getCenter();
        }
        PointDouble p = r.getParameters().getPoint(ParameterType.Center);
        return p == null ? bound.getCenter() : p;
    }

    public void setParameter(Parameters.ParameterType t, Object p) {

        AffineTransformRenderer atr = checkAtr(t);
        atr.getParameters().setObject(t, p);
        atr.updateTranformation();
    }

    public PointDouble getParameterPoint(Parameters.ParameterType t) {
        Renderer r = this.getRenderer();
        if (r == null || r.getParameters() == null) {
            return null;
        }
        return r.getParameters().getPoint(t);
    }

    public void multParameter(Parameters.ParameterType t, PointDouble p) {
        AffineTransformRenderer atr = checkAtr(t);
        if (atr.getParameters().getPoint(t) == null) {
            atr.getParameters().setPoint(t, 1.0, 1.0);
        }
        atr.getParameters().multPoint(t, p);
        atr.updateTranformation();
    }

    public void setParameter(Parameters.ParameterType t, double b) {
        setParameter(t, new Double(b));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() > MouseEvent.BUTTON1) {
            setParameter(ParameterType.Center, this.bound.getCenter());
        }
        origin = wa.getDrawingPoint(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getRenderer().getParameters().sendMod();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        PointDouble newpt = wa.getDrawingPoint(e.getPoint());
        PointDouble p = null;
        if (getRenderer() != null && getRenderer().getParameters() != null) {
            p = getRenderer().getParameters().getPoint(ParameterType.Translate);
        }
        if (p == null) {
            p = new PointDouble(0, 0);
        }

        //setParameter(ParameterType.Translate, e);
        setParameter(ParameterType.Translate, newpt.minus(origin).plus(p));
        getRenderer().getParameters().addPoint(ParameterType.Center, newpt.minus(origin));
        origin = newpt;
    }

    @Override
    public void init(WorkArea wa) {
        // throw new UnsupportedOperationException("Not supported yet.");
       // System.out.println("----------------------------------------yeah");
        this.wa = wa;
        for (GMovableAnchor gma : this.points) {
            gma.init(wa);
        }
        ratio = RatioKind.noRatio;
    }
    
    
     @Override
        public void keyPressed(KeyEvent e) {
            
            if (e.isControlDown()) {
                if (e.isShiftDown()) {
                    this.ratio = RatioKind.originRatio;
                } else {
                    ratio = RatioKind.lastRatio;
                }
                
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            ratio = RatioKind.noRatio;
        }

    class PointScale extends GMovableAnchor.PointSetterGetter {

        Parameters.ParameterType type = Parameters.ParameterType.Scale;
        PointDouble factor;
        PointDouble originRatio;
        

        public PointScale(ParameterType type, double factx, double facty) {
            this(type, new PointDouble(factx, facty));
        }

        public PointScale(ParameterType type, PointDouble factor) {
            this.type = type;
            this.factor = factor;
        }

        public PointDouble getPoint() {
            return bound.getTopLeftPoint().plus(bound.getDimension().mult(factor));
        }

        public void setPoint(PointDouble Origine, PointDouble p) {

            PointDouble p1 = getCenter().minus(Origine);
            PointDouble p2 = getCenter().minus(p);
            PointDouble fin= p2.div(p1).mult(originRatio);
            switch(ratio){
                case lastRatio:
                    double r=(fin.getX()+fin.getY())/2.0;
                    fin=new PointDouble(r,r);
                    break;
                case originRatio:
                    
            }
            setParameter(type, fin);
        }

        @Override
        public void released(MouseEvent e) {
            getRenderer().getParameters().sendMod();

        }

        /**
         *
         * @param e the value of e
         * @param origin the value of origin
         */
        @Override
        public void pressed(MouseEvent e, PointDouble origin) {
            originRatio = getParameterPoint(type);
            //  this.originRatio=getCenter().minus(origin);
        }

       
    }

    class PointShear extends GMovableAnchor.PointSetterGetter {

        PointDouble factor;
        PointDouble old;
        static final double SEPARATOR = 5.0;

        public PointShear(PointDouble factor) {
            this.factor = factor;
        }

        public PointShear(double factX, double factY) {
            this.factor = new PointDouble(factX, factY);
        }

        public PointDouble getPoint() {
            //return new PointDouble(10,10);
            //return getCenter();
            // PointDouble ret=bound.getTopLeftPoint().plus(bound.getDimension().mult(factor));
            //System.out.println(""+ret);
            //return ret;

            PointDouble factIntervert = factor.intervert();
            return bound.getCenter().plus(bound.getDimension().mult(factIntervert.mult(0.5, 0.5)).plus(factIntervert.mult(SEPARATOR, SEPARATOR)));
        }

        public void setPoint(PointDouble Origine, PointDouble p) {
            PointDouble diff = p.minus(Origine).mult(factor).div(bound.getDimension().intervert()).mult(2.0, 2.0);
            setParameter(ParameterType.Shear, diff.plus(old));
        }

        @Override
        public void released(MouseEvent e) {
            getRenderer().getParameters().sendMod();
        }

        /**
         *
         * @param e the value of e
         * @param origin the value of origin
         */
        @Override
        public void pressed(MouseEvent e, fr.inria.rivage.elements.PointDouble origin) {
            old = getParameterPoint(ParameterType.Shear);
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class PointCenter extends GMovableAnchor.PointSetterGetter {

        public PointCenter() {
        }

        public PointDouble getPoint() {
            return getCenter();

        }

        public void setPoint(PointDouble Origine, PointDouble p) {
            setParameter(ParameterType.Center, p);
        }

        @Override
        public void released(MouseEvent e) {
            getRenderer().getParameters().sendMod();

        }

        /**
         *
         * @param e the value of e
         * @param origin the value of origin
         */
        @Override
        public void clicked(MouseEvent e, fr.inria.rivage.elements.PointDouble origin) {
            if (e.getButton() > 1) {
                setPoint(origin, bound.getCenter());
            }
            // throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class PointRot extends GMovableAnchor.PointSetterGetter {

        public PointDouble getPoint() {
            PointDouble center = getCenter();

            if (getRenderer() != null) {
                AffineTransform af = new AffineTransform();
                af.setToRotation(getRenderer().getParameters().getDouble(ParameterType.Angular), center.x, center.y);
                return (PointDouble) af.transform(center.plus(0, -20), new PointDouble());
            }
            return center.plus(0, -20);

        }

        public void setPoint(PointDouble Origine, PointDouble p) {

            Point2D center = getCenter();
            double x1 = p.getX() - center.getX();
            double y1 = p.getY() - center.getY();
            /* if (y1 == 0) {
             if (x1 > 0) {
             setParameter(ParameterType.Angular, new Double(Math.PI / 2.0));
             } else {
             setParameter(ParameterType.Angular, new Double(3.0 * Math.PI / 2.0));
             }
             } else {*/
            setParameter(ParameterType.Angular, new Double((Math.atan2(y1, x1) + Math.PI / 2.0)) % (2.0 * Math.PI));
            //}
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void released(MouseEvent e) {
            getRenderer().getParameters().sendMod();
        }
    }
}

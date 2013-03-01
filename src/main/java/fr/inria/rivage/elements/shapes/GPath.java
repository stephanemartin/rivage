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
package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.Modifier.GPathModifier;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GEditFormModifier;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GPath extends GObjectContainer<GPointPath> {

    private static final Logger log = Logger.getLogger(Class.class.getName());
    protected boolean closed;
    Shape transformedShape = null;
    Shape shapeCache = null;

    public GPath(GObjectContainer p, List<Point2D> points, Color frtColor, Color bckColor, Stroke stroke, boolean closed) {
        super(p);
        this.parameters.setObject(FgColor, frtColor);
        if (bckColor != null) {
            this.parameters.setObject(BgColor, bckColor);
        }
        this.parameters.setObject(Stroke, stroke);
        this.closed = closed;
        parameters.acceptMod();
        //this.getParameters().addZeroType(Curve1,Curve2);

    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public synchronized GObject getObjectByPoint(Point2D p, double tolerance) {
        if (transformedShape == null) {
            logger.warning("Shape has not transformed");
            return null;
        }
        return transformedShape.intersects(p.getX() - tolerance, p.getY() - tolerance, tolerance * 2, tolerance * 2) ? this : null;
    }

    @Override
    public List<GObject> getObjectsByRectangle(Rectangle2D r) {
        LinkedList ret = new LinkedList();
        /*Shape shape = makeShape();
         shape = af.createTransformedShape(shape);*/
        if (transformedShape != null && transformedShape.intersects(r)) {
            ret.add(this);
        }
        return ret;
    }

    @Override
    public synchronized void add(GPointPath o) {
        super.add(o);
        shapeCache = null;
    }

    @Override
    public synchronized void addAll(Collection<GPointPath> l) {
        super.addAll(l);
        shapeCache = null;
    }

    @Override
    public synchronized void remove(GPointPath o) {
        super.remove(o);
        shapeCache = null;
    }

    @Override
    public void remove(ID o) {
        super.remove(o);
        shapeCache = null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (size() < 2) {
            return;
        }
        Shape sh = this.makeShape();
        transformedShape = GObjectShape.draw(g2, parameters, this.gRendreres, sh, new GBounds2D(sh).getCenter());
        //g2.draw(this.makeShape());
    }

    
    public Shape curveed(Parameters par, PointDouble back) {
        PointDouble p = par.getPoint(Parameters.ParameterType.TopLeft);
        PointDouble c1 = par.getPoint(Parameters.ParameterType.Curve1);
        PointDouble c2 = par.getPoint(Parameters.ParameterType.Curve2);
        // if (c1 != null && c2 != null) {
        if (c1 == null) {
            c1 = p.center(back);
        }
        if (c2 == null) {
            c2 = p.center(back);
        }
        CubicCurve2D ret=new CubicCurve2D.Double();
        ret.setCurve(back, c1, c2, p);
        return ret;
    }
    
    PointDouble curve(Parameters par, GeneralPath polygon, PointDouble back) {
        PointDouble p = par.getPoint(Parameters.ParameterType.TopLeft);
        PointDouble c1 = par.getPoint(Parameters.ParameterType.Curve1);
        PointDouble c2 = par.getPoint(Parameters.ParameterType.Curve2);
        // if (c1 != null && c2 != null) {
        if (c1 == null) {
            c1 = p.center(back);
        }
        if (c2 == null) {
            c2 = p.center(back);
        }

        polygon.curveTo(c1.getX(), c1.getY(), c2.getX(), c2.getY(), p.getX(), p.getY());
        return p;
    }

    public Shape makeShape() {
        //if (shapeCache == null) {
        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, this.size());
        Iterator<GPointPath> ito = contain.iterator();
        PointDouble back = null;
        if (ito.hasNext()) {
            Parameters par = ito.next().getParameters();
            PointDouble p = par.getPoint(Parameters.ParameterType.TopLeft);
            //PointDouble c1 = par.getPoint(Parameters.ParameterType.Curve1);
            //PointDouble c2 = par.getPoint(Parameters.ParameterType.Curve2);
            polygon.moveTo(p.getX(), p.getY());
            back = p;
            //polygon.curveTo(c1.getX(), c1.getY(), c2.getX(), c1.getY(), p.getX(), p.getY());
        }
        //

        while (ito.hasNext()) {
            Parameters par = ito.next().getParameters();
            back = curve(par, polygon, back);

            /*} else {
             polygon.lineTo(p.getX(), p.getY());
             }*/
            //
        }
        if (closed) {
            //polygon.closePath();
            Parameters par = first().getParameters();
            curve(par, polygon, back);
            /*PointDouble p = par.getPoint(Parameters.ParameterType.TopLeft);
             PointDouble c1 = par.getPoint(Parameters.ParameterType.Curve1);
             PointDouble c2 = par.getPoint(Parameters.ParameterType.Curve2);
             if (c1 != null && c2 != null) {
             polygon.curveTo(c1.getX(), c1.getY(), c2.getX(), c2.getY(), p.getX(), p.getY());
             } else {
             polygon.lineTo(p.getX(), p.getY());
             }*/
        }
        shapeCache = polygon;
        //}
        return shapeCache;

    }

    @Override
    public void activateOption(WorkArea wa) {
        super.activateOption(wa);
    }

    public Shape getTransformedShape() {
        return transformedShape;
    }

    @Override
    public GHandler getModifier() {
        return new GEditFormModifier(this, new GPathModifier(this));
    }

    @Override
    public GBounds2D getEuclidBounds() {
        Shape sh = makeShape();
        if (gRendreres != null) {
            sh = gRendreres.transform(sh);
        }
        return new GBounds2D(sh.getBounds2D());
    }

    @Override
    public List<GObject> getRealObjects() {
        List<GObject> ret = new LinkedList();
        ret.add(this);
        return ret;
    }

    @Override
    public String toString() {
        return "GPath{" +contain+ '}';
    }
    
}

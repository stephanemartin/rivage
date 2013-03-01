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

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.tools.Configuration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class GMutablePoint extends GHandler {

    WorkArea wa;
    Parameters param;
    PointDouble sizeDraw = new PointDouble(Configuration.getConfiguration().SEL_MARK_SIZE, Configuration.getConfiguration().SEL_MARK_SIZE);
    Color color = Color.RED;
    Color XorColor = Color.GREEN;
    AffineTransform af = null;
    Point2D OrigineCenter = null;
    Point2D OriginePressed = null;
    PointDouble OrigineDim = null;
    PointDouble OriginePos = null;
    private GMutablePoint oppositePoint = null;

    public GMutablePoint(Parameters param) {

        this.param = param;
    }
    PointDouble po;

    public void setOppositePoint(GMutablePoint oppositePoint) {
        this.oppositePoint = oppositePoint;
    }

    @Override
    public void setAffineTransform(AffineTransform af) {
        super.setAffineTransform(af);
        //sizeDraw=new PointDouble(Configuration.getConfiguration().SEL_MARK_SIZE,Configuration.getConfiguration().SEL_MARK_SIZE);
        //sizeDraw=sizeDraw.div(af.getScaleX(),af.getScaleY());
    }

    @Override
    public void draw(Graphics2D g) {
        //super.draw(g);
        //GraphicUtils.setSelectionColor(g);
        Color bak = g.getColor();
        g.setColor(Color.black);
        /*if (OrigineCenter != null) {
         g.fillOval((int) OrigineCenter.getX() - 5, (int) OrigineCenter.getY() - 5, 10, 10);
         }
         g.setColor(Color.MAGENTA);
         g.fillOval((int) getCenter().getX() - 5, (int) getCenter().getY() - 5, 10, 10);
         g.fillOval((int) param.getPoint(ParameterType.P1coord).getX() - 5, (int) (int) param.getPoint(ParameterType.P1coord).getY() - 5, 10, 10);
         g.fillOval((int) param.getPoint(ParameterType.P2coord).getX() - 5, (int) (int) param.getPoint(ParameterType.P2coord).getY() - 5, 10, 10);
         */
        g.setColor(color);

        g.setXORMode(XorColor);
        g.setStroke(new BasicStroke(0));
        //AffineTransform a = g.getTransform();
        //g.setTransform(new AffineTransform());


        g.fill(this.getRotatedShape());
        //       g.setXORMode(bakXor);
        g.setColor(bak);
        //g.setTransform(a);
        g.setPaintMode();
    }

    /**
     *
     * @param size the value of size
     */
    abstract public Shape makeShape();

    protected PointDouble getCenter() {
        PointDouble ret = param.getBounds().getCenter();
        AffineTransform af2 = this.getAffineTransform();
        if (af2 != null) {
            ret = (PointDouble) af2.transform(ret, new PointDouble());
        }
        return ret;
        //return param.getPoint(ParameterType.TopLeft).plus(param.getPoint(ParameterType.Dimension).div(2.0, 2.0));
        // if (OrigineCenter == null) {
        /*return new PointDouble(Math.abs(param.getPoint(ParameterType.P1coord).getX() + param.getPoint(ParameterType.P2coord).getX()) / 2,
         Math.abs(param.getPoint(ParameterType.P1coord).getY() + param.getPoint(ParameterType.P2coord).getY()) / 2);*/
        /*}
         return OrigineCenter;*/
    }

    protected AffineTransform getAf() {
        if (true || af == null) {

            af = new AffineTransform();
            PointDouble center = getCenter();
            /*new PointDouble(Math.abs(param.getPoint(ParameterType.P1coord).getX() + param.getPoint(ParameterType.P2coord).getX()) / 2,
             Math.abs(param.getPoint(ParameterType.P1coord).getY() + param.getPoint(ParameterType.P2coord).getY()) / 2);*/

            af.setToRotation(param.getDouble(ParameterType.Angular), center.getX(), center.getY());
        }
        return af;
    }

    public PointDouble rotatedPoint(PointDouble p, Point2D c, boolean trig) {
        double angle = param.getDouble(Parameters.ParameterType.Angular);
        if (angle == 0) {
            return p;
        } else {
            PointDouble ret = (PointDouble) p.clone();
            AffineTransform af = new AffineTransform();
            af.setToRotation(param.getDouble(ParameterType.Angular), c.getX(), c.getY());
            if (trig) {
                ret = (PointDouble) af.transform(p, ret);
            } else {
                try {
                    // af = new AffineTransform();
                    //af.setToRotation(2.0 * Math.PI - param.getDouble(ParameterType.Angular), c.getX(), c.getY());


                    ret = (PointDouble) af.inverseTransform(p, ret);
                    //af.transform(p, ret);
                } catch (NoninvertibleTransformException ex) {
                    Logger.getLogger(GMutablePoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //System.out.println("before " + p.getX() + "," + p.getY() + " After" + ret.getX() + "," + ret.getY() + " " + param.getDouble(ParameterType.Angular));
            return ret;
        }
    }

    public Shape getRotatedShape() {
        //AffineTransform af=this.getAf();
        Shape ret = getAf().createTransformedShape(makeShape());
        /*AffineTransform af = this.getAffineTransform();
        if (af != null) {
            ret = af.createTransformedShape(ret);
        }*/
        return ret;

    }

    abstract protected PointDouble getPoint();

    public PointDouble getRotatedPoint() {
        PointDouble ret = rotatedPoint(getPoint(), getCenter(), true);
        AffineTransform af = this.getAffineTransform();
        if (af != null) {
            ret = (PointDouble) af.transform(ret, new PointDouble());
        }
        return ret;
    }

    /*public void setRotatedPoint(Point2D p) {
     /*   Point2D o = getRotatedPoint();
     Point2D c = getCenter();*
     //c=new PointDouble(c.getX()+(p.getX()-o.getX())/2.0,c.getY()+(p.getY()-o.getY())/2.0);
     PointDouble pd=new PointDouble(rotatedPoint(p, getCenter(), false));
     AffineTransform af2=this.getAffineTransform();
     if(af2!=null){
     pd=(PointDouble)af2.transform(pd,new PointDouble());
     }
     setPoint(pd);
     }*/
    abstract public void setPoint(Point2D p);

//    }
    @Override
    public void mouseDragged(MouseEvent e) {
        PointDouble p = this.getPointFromMouseEvent(wa, e);
        ///PointDouble p = wa.getDrawingPoint(e.getPoint());
        //Point2D newCenter = po == null ? OrigineCenter : new PointDouble((po.getX() + p.getX()) / 2.0, (po.getY() + p.getY()) / 2.0);
        //AffineTransform af2 = this.getAffineTransform();
        /*if (af2 != null) {
         af2.transform(newCenter, newCenter);
         }*/
        setPoint(rotatedPoint(p, this.getCenter(), false));
        if (po != null) {
            oppositePoint.setPoint(rotatedPoint(po, this.getCenter(), false));
        }
        //this.setRotatedPoint(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // super.mousePressed(e);
        OrigineCenter = this.getCenter();
        OriginePressed = rotatedPoint(this.getPointFromMouseEvent(wa, e), OrigineCenter, false);
        //OriginePressed = rotatedPoint(wa.getDrawingPoint(e.getPoint()), OrigineCenter, false);

        po = oppositePoint != null ? oppositePoint.getRotatedPoint() : null;

        OrigineDim = this.param.getPoint(ParameterType.Dimension);
        OriginePos = this.param.getPoint(ParameterType.TopLeft);
        AffineTransform af2 = null;// this.getAffineTransform();
        if (af2 != null) {

            OriginePos = (PointDouble) af2.transform(OriginePos, new PointDouble());
            //af2.transform(OrigineDim, OrigineDim);
            if (po != null) {
                po = (PointDouble) af2.transform(po, new PointDouble());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //this.setPoint(e.getPoint());
        af = null;
        OrigineCenter = null;
        param.sendMod();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
    }

    public GHandler getHandlerByPoint(Point2D p) {
        Point2D p2 = p;//this.getAffineTransformInverse().transform(p, new PointDouble ());
        return (getRotatedShape().contains(p2)) ? this : null;
    }
}

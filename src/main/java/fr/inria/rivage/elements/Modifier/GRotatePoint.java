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
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GRotatePoint extends GMutablePoint {

    public static final double SEPARATION = 10;
    public ParameterType rot = ParameterType.Angular;

    public GRotatePoint(Parameters param, ParameterType rot) {
        super(param);
        this.rot = rot;
    }

    public GRotatePoint(Parameters param) {
        super(param);

        this.color = Color.BLUE;
    }

    @Override
    public void setPoint(Point2D p) {
        Point2D center = getCenter();
        /*AffineTransform af2=this.getAffineTransform();
         System.out.println("af2"+af2);
        
         if (af2!=null){
         //   p=(PointDouble)af2.transform(p, new PointDouble());
         center = af2.transform(center, center);
         }*/
        double old = this.param.getDouble(rot);

        double x1 = p.getX() - center.getX();
        double y1 = p.getY() - center.getY();
        /*if (y1 == 0) {
         this.param.setObject(rot, new Double(old + Math.PI / 2.0));
         } else {*/
        this.param.setObject(rot, new Double((old + Math.atan2(y1, x1) + Math.PI / 2.0)) % (2.0 * Math.PI));
        //}
        af = null;
    }

    /**
     *
     * @param size the value of size
     */
    @Override
    public Shape makeShape() {
        Point2D p = this.getPoint();
        AffineTransform af = this.getAffineTransform();
        if (af != null) {
            p =  af.transform(p, new Point2D.Double());
        }
        
        return new Ellipse2D.Double(p.getX() - sizeDraw.getX() / 2.0, p.getY() - sizeDraw.getY() / 2.0, sizeDraw.getX(), sizeDraw.getY());
    }

    /* @Override
     public void setRotatedPoint(Point2D p) {

     setPoint(rotatedPoint(p, getCenter(), false));
     }*/
    @Override
    public PointDouble getPoint() {
        // af=null;
        Parameters.ParameterBounds b = param.getBounds();
        PointDouble ret = b.getTopLeft().plus(b.getWidth() / 2.0,-SEPARATION);

        //PointDouble ret = new PointDouble(center.getX(),center.getY()- dim.getY()/2.0 - SEPARATION);
        return ret;
        /*   Point2D center=getCenter();
         Point2D point=new PointDouble(center.getX(),center.getY()-Math.abs(param.getPoint(x).getY()-param.getPoint(y).getY())/2-SEPARATION);
         Point2D ret=new PointDouble();
        
         AffineTransform af=new AffineTransform();
        
         af.setToRotation(param.getDouble(rot), center.getX(), center.getY());
         ret=af.transform(point, ret);
         return new PointDouble(ret);*/
    }
}

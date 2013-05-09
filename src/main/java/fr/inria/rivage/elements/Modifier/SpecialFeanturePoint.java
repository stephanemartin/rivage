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
public class SpecialFeanturePoint extends GMutablePoint {

    public static final double SEPARE = 0;

    public static enum Follows {

        X, Y, Free
    };
    final Follows follows;
    final ParameterType linkedPoint;
    final ParameterType var;
    ParameterType sign;

    /**
     *
     * @param wa
     * @param param
     * @param var
     * @param linkedPoint
     * @param follows
     */
    public SpecialFeanturePoint( Parameters param, ParameterType var, ParameterType linkedPoint, Follows follows) {
        super( param);
        this.color = Color.ORANGE;
        this.follows = follows;
        this.var = var;
        this.linkedPoint = linkedPoint;
    }

    public SpecialFeanturePoint(Parameters param, ParameterType var, ParameterType linkedPoint, ParameterType sign, Follows follows) {
        this( param, var, linkedPoint, follows);
        this.sign=sign;

    }

    @Override
    public Shape makeShape() {

        Point2D p = this.getPoint();

        AffineTransform af = this.getAffineTransform();
        if (af != null) {
            p =  af.transform(p, new Point2D.Double());
        }
        return new Ellipse2D.Double(p.getX() - sizeDraw.getX() / 2.0, p.getY() - sizeDraw.getY() / 2.0, sizeDraw.getX(), sizeDraw.getY());
    }

    @Override
    protected PointDouble getPoint() {
        PointDouble link = param.getPoint(linkedPoint);
        PointDouble signd;
        if (sign==null){
            signd=new PointDouble(1,1);
        }else{
            PointDouble tmp=param.getPoint(sign);
            signd=new PointDouble(tmp.getX()>=0?1:-1,tmp.getY()>=0?1:-1);
        }
        PointDouble ret=null;
        switch (follows) {
            case Free:
                PointDouble free = new PointDouble(param.getPoint(var)).mult(signd);
                ret= free.plus(link);
                break;
            case Y:
                ret=link.plus(0, (param.getDouble(var) / 2.0 + getSep().getY())*signd.getY());
                break;
            default:
                ret= link.plus((param.getDouble(var) / 2.0 + getSep().getX())*signd.getX(), 0);
                break;
        }
        
       return ret;
    }

    private PointDouble getSep() {
        return sizeDraw.mult(2.0,2.0).plus(SEPARE, SEPARE) ;
    }

    @Override
    public void setPoint(Point2D p) {
        PointDouble link = param.getPoint(linkedPoint); 
        /*AffineTransform af2=this.getAffineTransformInverse();
        if (af2!=null){
            af2.transform(p, p);
        }*/
        
        PointDouble signd;
        if (sign==null){
            signd=new PointDouble(1,1);
        }else{
            PointDouble tmp=param.getPoint(sign);
            signd=new PointDouble(tmp.getX()>=0?1:-1,tmp.getY()>=0?1:-1);
        }
        /*AffineTransform af = this.getAffineTransform();
        if (af != null) {
            link =  (PointDouble)af.transform(link,new PointDouble());
        }*/
        switch (follows) {
            case Free:
                // PointDouble free = new PointDouble(param.getPoint(var));
                param.setObject(var, (new PointDouble(p)).minus(link).mult(signd));
                break;
            case Y:
                param.setDouble(var, Math.min(Math.max(((p.getY() - link.getY()) * 2.0 )*signd.getY()- getSep().getY()*2.0, 0),param.getBounds().getHeight()));
                break;
            default:
                param.setDouble(var, Math.min(Math.max(((p.getX() - link.getX()) * 2.0)*signd.getX() - getSep().getX()*2.0, 0),param.getBounds().getWidth()));
                break;
        }
    }
}

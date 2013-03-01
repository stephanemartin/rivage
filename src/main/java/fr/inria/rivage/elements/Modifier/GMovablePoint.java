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
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GMovablePoint extends GMutablePoint {
   
    
    int factX;
    int factY;
    public GMovablePoint(Parameters param, int factX,int factY) {
        super(param);
        this.factX=factX;
        this.factY=factY;
    }
    public void setPoint(Point2D p){
        /*PointDouble p1=param.getPoint(ParameterType.Center);
        PointDouble d2=param.getPoint(ParameterType.Dimension).mult(factX/2.0, factY/2.0);
        PointDouble pold= new PointDouble(p1.plus(d2));*/
        //PointDouble d2=param.getPoint(ParameterType.Dimension).div(2.0, 2.0).mult(factX, factY);
        
        PointDouble diff=new PointDouble(p).minus(OriginePressed);
                
        //Works !
        param.setObject(ParameterType.Dimension, diff.mult(2.0*factX,2.0* factY).plus(OrigineDim));
        param.setObject(ParameterType.TopLeft, diff.mult(-factX, -factY).plus(OriginePos));
        
       /* param.setObject(ParameterType.Dimension, diff.mult(factX,factY).plus(OrigineDim));
        AffineTransform af=new AffineTransform();
        af.rotate(param.getDouble(ParameterType.Angular));
        Point2D pr=af.transform(new PointDouble(factX,factY),null);
        param.setObject(ParameterType.TopLeft, diff.mult(-pr.getX(), -pr.getY()).plus(OriginePos));
        */
       /* param.setObject(ParameterType.Dimension, diff.mult(factX,factY).plus(OrigineDim));
        double angular=param.getDouble(ParameterType.Angular);
        param.setObject(ParameterType.TopLeft, diff.mult((-factX+Math.cos(angular))*0.5, (-factY+Math.cos(angular))*0.5).plus(OriginePos));
        System.out.println("factX "+factX+" factY "+factY+" sin"+Math.sin(angular)+" "+Math.cos(angular));*/
        //diff=new PointDouble(p).minus(OriginePressed).mult(factX*Math.cos(angular), factY*Math.sin(angular)).plus(OriginePos);
        
        //param.addPoint(ParameterType.Dimension, diff);
        
      //  param.setPoint(x, p.getX(), param.getPoint(x).getY());
      //  param.setPoint(y,  param.getPoint(y).getX(),p.getY());
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
        return new Rectangle2D.Double( p.getX() - sizeDraw.getX()/2.0, p.getY() - sizeDraw.getY()/2.0, sizeDraw.getX(), sizeDraw.getY());
    }
    
    
    
    public PointDouble  getPoint(){
        PointDouble p=param.getPoint(ParameterType.TopLeft);
        PointDouble d2=param.getPoint(ParameterType.Dimension).mult(factX==-1?0:1, factY==-1?0:1);
        PointDouble ret= new PointDouble(p.plus(d2));
       /* AffineTransform af = this.getAffineTransform();
        if (af != null) {
            ret = (PointDouble) af.transform(ret, new PointDouble());
        }*/
        return ret;
    }

    @Override
    public String toString() {
        return "GMovablePoint{" + this.getRotatedPoint() + '}';
    }

    

}

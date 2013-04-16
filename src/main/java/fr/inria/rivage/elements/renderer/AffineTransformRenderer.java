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

package fr.inria.rivage.elements.renderer;

import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

/**
 * //do sum of modifications
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class AffineTransformRenderer extends Renderer implements Observer {

    AffineTransform af;
    ID modId;

    public AffineTransformRenderer(ID id, ID obj, ColObject... parent) {
        super(id, parent);
        this.parameters.addObserver(this);
        this.modId = obj;
        /*this.parameters.setPoint(Scale,1.0, 1.0);
         this.parameters.setPoint(Shear,1.0, 1.0);
         this.parameters.setDouble(Angular, 0.0);
         this.parameters.acceptMod();*/
    }

    public Shape transform(Shape shape) {
        /* if (af==null){
         updateTranformation();
         }*/
        updateTranformation();//TODO optimize with observer
        return af.createTransformedShape(shape);
    }

    /*public Shape invertTransform(Shape shape) {
     return afInvert.createTransformedShape(shape);

     }*/
    public PointDouble transform(PointDouble p) {
        if (af == null) {
            return p;
        }
        updateTranformation();//TODO optimize with observer
        return (PointDouble) af.transform(p, new PointDouble());
    }

    /*public PointDouble invertTransform(PointDouble p) {
     if (afInvert == null) {
     return p;
     }
     return (PointDouble) afInvert.transform(p, new PointDouble());
     }*/
    public void update(Observable o, Object arg) {
        updateTranformation();
      /*  System.out.print(".");
        System.out.flush();*/
    }

    public final void updateTranformation() {

        PointDouble center = parameters.getPoint(Center);
        if (center == null) {
            System.out.println("OOOOPS !!!! no center !");
            return;
        }
        PointDouble tmp;

        af = new AffineTransform();
        af.translate(center.getX(), center.getY());

        tmp = parameters.getPoint(Scale);
        if (tmp != null) {
            af.scale(tmp.getX(), tmp.getY());
        }

        tmp = parameters.getPoint(Shear);
        if (tmp != null) {
            af.shear(tmp.getX(), tmp.getY());
        }

        af.rotate(parameters.getDouble(Angular));
        af.translate(-center.getX(), -center.getY());

        tmp = parameters.getPoint(Translate);
        if (tmp != null) {
            af.translate(tmp.getX(), tmp.getY());

        }
        /*try {
         afInvert = af.createInverse();
         } catch (NoninvertibleTransformException ex) {
         Logger.getLogger(GRenderers.class.getName()).log(Level.SEVERE, null, ex);
         }*/

    }
    //3phases Set Center shear scale

    public void sendMod() {
        this.getParameters().sendMod();
    }

    /*public enum Transfomation {

     Center, Translate, Scale, Shear, Rotate
     };*/
    public Parameters.ParameterType[] types = {Scale, Shear, Translate, Angular};

    public boolean newNeeded(/*PointDouble center,*/Parameters.ParameterType t, ID id) {
        //Wishme : revise the matrix and affine transformation;)
       /* if (t!=Parameters.ParameterType.Center && !center.equals(this.getParameters().getPoint(Parameters.ParameterType.Center))){
         return true;
         }*/
        //System.out.println("--"+modId+" "+id);
        if (id != modId) {
            return true;
        }
        modId = id;
        for (Parameters.ParameterType type : types) {
            if (type != t && this.getParameters().getParameter(type) != null) {
                return true;
            }
        }

        return false;
        /*if(true)
         return true;*/
        /*switch (t) {
         case Center:
         if (this.getParameters().getParameter(Scale) != null) {
         return true;
         }
         case Scale:
         if (this.getParameters().getParameter(Shear) != null) {
         return true;
         }
         case Shear:
         if (this.getParameters().getDouble(Angular) != 0) {
         return true;
         }

         case Translate:
         /*if(this.getParameters().getParameter(Scale)!=null){
         return true;
         }*
         //Translate
         case Angular:
         //    return false;


         }
         return false;*/
    }

    @Override
    public String toString() {
        return "AffineTransformRenderer{" + this.getId() + "types=" + types + "zpos:" + this.getParameters().getPosition(Zpos) + '}';
    }

    public AffineTransform getAffineTransform() {
        if (af == null) {
            updateTranformation();
        }
        return af;
    }
}

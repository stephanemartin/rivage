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
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.AffineTransformeParameter;
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

    AffineTransformeParameter atp;
    ID modId;
    

    public AffineTransformRenderer(ID id, ID obj, AffineTransform af, ColObject... parent) {
        super(id, parent);
        
        this.modId = obj;
        atp = new AffineTransformeParameter(this.parameters);
        atp.setAf(af);
        this.parameters.addObserver(this);
    }

    public Shape transform(Shape shape) {
        /* if (af==null){
         updateTranformation();
         }*/
        //atp.loadAf();
        return atp.getAf().createTransformedShape(shape);
    }

    /*public Shape invertTransform(Shape shape) {
     return afInvert.createTransformedShape(shape);

     }*/
    public PointDouble transform(PointDouble p) {
        //atp.loadAf();
        return (PointDouble) atp.getAf().transform(p, new PointDouble());
    }

    public void update(Observable o, Object arg) {
        
       // atp.loadAf();
        atp.reset();
        ((GRenderersFeuille)(((GObject)this.getParent()[0]).getgRendreres())).modified();
        /*  System.out.print(".");
         System.out.flush();*/
    }

    //3phases Set Center shear scale
    public void sendMod() {
        atp.sendMod();
    }

    /*public enum Transfomation {

     Center, Translate, Scale, Shear, Rotate
     };*/
    public Parameters.ParameterType[] types = {Scale, Shear, Translate, Angular};

    @Override
    public String toString() {
        return "AffineTransformRenderer{" + this.getId() + "types=" + types + "zpos:" + this.getParameters().getPosition(Zpos) + '}';
    }

    public AffineTransform getAffineTransform() {
        return atp.getAf();
    }
}

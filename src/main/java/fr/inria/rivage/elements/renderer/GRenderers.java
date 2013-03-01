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

import fr.inria.rivage.elements.ColContainer;
import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.operations.CreateOperation;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.transaction.TransactionRequiredException;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
// Can be improved with colObjectContainer but this action needs two extends in gdocuement :( 
public class GRenderers extends ColContainer<Renderer> {

    public GRenderers() {
        
    }

    public GRenderers(ColObject parent) {
        super(parent);
    }

    
     public Shape transform(Shape shape) {
        for(Renderer r:contain){
            shape=r.transform(shape);
           
        }
        return  shape;
    }

    /*public Shape invertTransform(Shape shape) {
        for(Renderer r:contain){
            shape=r.invertTransform(shape);
        }
        return  shape;
    }*/

    public PointDouble transform(PointDouble p) {
        for(Renderer r:contain){
            p=r.transform(p);
        }
        return p;
    }

    @Override
    public Position getNext(ID id) {
        return super.getNext(id);
    }

    public AffineTransform getGlobal(){
        AffineTransform af=new AffineTransform();
        for (Renderer at:this.contain){
            if(at instanceof AffineTransformRenderer){
                AffineTransform t=new AffineTransform(((AffineTransformRenderer)at).getAffineTransform());
                 t.concatenate(af);
                 af=t;
            }
        
        }
        return af;
    }
    /*public PointDouble invertTransform(PointDouble p) {
        for(Renderer r:contain){
            p=r.invertTransform(p);
        }
        return p;
    }*/
    
    public AffineTransformRenderer newAffineRenderer(IConcurrencyController cc,ID obj) {
        
        
        //IConcurrencyController cc = wa.getFileController().getConcurrencyController();
        ID id =  cc.getNextID();
        Position pos = getNext(id);
        //Position pos = last().getParameters().getPosition(Parameters.ParameterType.Zpos).genNext(id);

        AffineTransformRenderer at = new AffineTransformRenderer(id,obj,this.getParent());

        at.getParameters().setObject(Parameters.ParameterType.Zpos, pos);
        at.setParent(this.getParent());
        cc.sendOperation(new CreateOperation(at));
        addObject(at);
        return at;
    }

    @Override
    public void setParent(ColObject... parent) {
        super.setParent(parent);
        for (Renderer r:contain){
            r.setParent(parent);
        }
    }   
}

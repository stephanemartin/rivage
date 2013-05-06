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
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.CreateOperation;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
// Can be improved with colObjectContainer but this action needs two extends in gdocuement :( 
public class GRenderersFeuille extends ColContainer<Renderer> implements GRenderer {

    public GRenderersFeuille() {
        
    }

    public GRenderersFeuille(ColObject parent) {
        super(parent);
    }

    
    @Override
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

    @Override
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

    @Override
    public AffineTransform getTransform(){
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
    
    public AffineTransformRenderer newAffineRenderer(FileController fc,ID obj,PointDouble center) {
        
        IConcurrencyController cc=fc.getConcurrencyController();
        //IConcurrencyController cc = wa.getFileController().getConcurrencyController();
        ID id =  cc.getNextID();
        Position pos = getNext(id);
        //Position pos = last().getParameters().getPosition(Parameters.ParameterType.Zpos).genNext(id);

        AffineTransformRenderer at = new AffineTransformRenderer(id,obj,center,this.getParent());

        at.getParameters().setObject(Parameters.ParameterType.Zpos, pos);
        at.setParent(this.getParent());
        cc.sendOperation(new CreateOperation(at));
        addObject(at);
        fc.getDocument().add(at);
        return at;
    }

    @Override
    public void setParent(ColObject... parent) {
        super.setParent(parent);
        for (Renderer r:contain){
            r.setParent(parent);
        }
    }   

    public void addTransform(AffineTransform trasform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

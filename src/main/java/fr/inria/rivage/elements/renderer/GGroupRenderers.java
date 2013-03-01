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
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.tools.Position;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GGroupRenderers extends GRenderers {

   

    public GGroupRenderers(GGroup group) {
        
        this.group = group;
        this.parent=group.getCollection().toArray(new ColObject[group.size()]);
    }
    
    GGroup group;
    @Override
    public synchronized void addObject(Renderer o) {
        super.addObject(o);
        for (GObject go:group){
            go.getgRendreres().addObject(o);
        }
    }

    @Override
    public Position getMax() {// TODO : optimize 
        if (contain.isEmpty()){
            Position max=null;
            for (GObject go: group.getRealObjects()){
                if(go.getgRendreres()!=null){
                    Position p=go.getgRendreres().getMax();
                    max=(p!=null)?p.getMax(max):max;
                }
            }
            return max;
        }else{
           return super.getMax();
        }
        
    }

    
    /*@Override
    public AffineTransformRenderer newAffineRenderer(IConcurrencyController cc) {
       ID id =  cc.getNextID();
        Position pos = getNext(id);
        //Position pos = last().getParameters().getPosition(Parameters.ParameterType.Zpos).genNext(id);
        AffineTransformRenderer at = new AffineTransformRenderer(id,group.getCollection().toArray(new ColObject[group.size()]));
        at.getParameters().setObject(Parameters.ParameterType.Zpos, pos);
        at.setParent(this.getParent());
        cc.sendOperation(new CreateOperation(at));
        return at;
    }*/

    @Override
    public void setParent(ColObject... parent) {
        super.setParent(parent);
    }

    @Override
    public ColObject[] getParent() {
        List <GObject> l = group.getRealObjects();
       return l.toArray(new ColObject[l.size()]);
    }
    
    
}

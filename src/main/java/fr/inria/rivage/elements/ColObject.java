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

package fr.inria.rivage.elements;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import java.io.Serializable;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class ColObject implements Serializable, Cloneable {

    private ID id;
    protected Parameters parameters;
    protected ColObject[] parent;
    private ID[] parentId;
   
    public void deleteMeFromParent(){
          for (ColObject co:this.parent){
                if(co instanceof GObjectContainer){
                    ((GObjectContainer)co).remove((GObject)this);
                }else if(co instanceof ColContainer){
                    ((ColContainer)co).delObject(this);
                }
            } 
    }
    public void addMeFromParent(){
       for (ColObject co:this.parent){
                if(co instanceof GObjectContainer){
                    ((GObjectContainer)co).add((GObject)this);
                }else if(co instanceof ColContainer){
                    ((ColContainer)co).addObject(this);
                }
            } 
    }
    
    
    public ColObject(GDocument doc) {
        this.parameters = new Parameters(doc);
    }

    public ColObject(ID id) {
        this.id = id;
    }
    
    GDocument getDoc() {
        if (this.parent == null || this.parent.length == 0) {
            if (this instanceof GDocument) {
                return (GDocument) this;
            } else {
                return null;
            }
        } else {
            return this.parent[0].getDoc();
        }
    }
    /*public ColObject( GObjectContainer parent) {
     this.id = id;
     ColObject
     }*/

    public ColObject(ColObject... parent) {
        if (parent==null || parent.length == 0) {
            return;
        }
        this.parameters = new Parameters(parent[0].getParameters().getGDocument(), null);
        initParent(parent);

    }

    public ColObject(ID id, ColObject... parent) {
        this(parent);
        this.setId(id);
        /*this.parameters = new Parameters(parent.getParameters().getGDocument(), id);
         this.parent = parent;
         parentId = parent.getId();*/

    }

    public ColObject[] getParent() {
        return parent;
    }

    public ID[] getParentId() {
        return parentId;
    }

    private void initParent(ColObject... parent) {
        this.parent = parent;
        parentId = new ID[parent.length];
        for (int i = 0; i < parent.length; i++) {
            parentId[i] = parent[i].getId();
        }
    }

    public void setParent(ColObject... parent) {
        initParent(parent);
    }

    public final void setId(ID id) {
        this.id = id;
        parameters.setTarget(id);
    }

    public ID getId() {
        return id;
    }

    public Parameters getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (this.getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GObject other = (GObject) obj;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public ColObject clone() throws CloneNotSupportedException {
        return (ColObject)super.clone();
    }
    
}

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
package fr.inria.rivage.engine.concurrency.tools;

import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import fr.inria.rivage.gui.listener.ParameterListener;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class Parameter<T> implements Serializable,Cloneable{

    protected T element = null;
    protected ParameterType type;
    protected ID target;
    protected GDocument doc;
   // protected LinkedList<ParameterListener>  listener=new LinkedList();
    //Parameters.ParameterType type;

    public Parameter() {
    }

    public void setDoc(GDocument doc) {
        this.doc = doc;
    }

    public GDocument getDoc() {
        return doc;
    }
    
    
    public ID getTarget() {
        return target;
    }

    public void setTarget(ID target) {
        this.target = target;
    }
    
    
    
    
    public T getElement() {
        return element;
    }

    public abstract void remoteUpdate(Parameter<T> up);

    /**
     *
     * @param t the value of t
     * @param nice the value of nice
     */
    public abstract void localUpdate(T t, int nice);

    public  void sendMod() {
        sendMod(null);
    }
    
    public abstract Parameter fork();
    public abstract void sendMod(ID id);

    public abstract void acceptMod();
    

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }
   /* public void addParameterListener(ParameterListener pl){
        this.listener.add(pl);
    }
    public void delParameterListener(ParameterListener pl){
        this.listener.remove(pl);
    }*/
    
}

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

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.utils.GComparator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Object for future cleaning
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class ColContainer<T extends ColObject> extends ColObject implements Iterable<T> {

    protected Parameters.ParameterType param = Parameters.ParameterType.Zpos;
    protected TreeSet<T> contain = new TreeSet<T>(new GComparator(param));
    protected HashSet<ID> containID = new HashSet<ID>();
    /* protected Position max = null;
     protected Position min = null;*/

    public ColContainer() {
    }

    public ColContainer(ID id) {
        super(id);
    }

    public ColContainer(ID id, ColObject ...parent) {
        super(id, parent);
    }

    public ColContainer(ColObject ...parent) {
        super(parent);
    }

    synchronized public void addObject(T o) {
        //CheckContain();
       
        contain.add(o);
        containID.add(o.getId());

    }

    synchronized public void addAllObject(List<T> l) {
        contain.addAll(l);

        for (T obj : l) {
            containID.add(obj.getId());
        }
    }

    synchronized public void delObject(T o) {
        //CheckContain();
        /*int pos = dicSearch(o, 0, contain.size() - 1);
         if (contain.get(pos).equals(o)) {
         contain.remove(pos);
         containID.remove(o.getId());
         }*/
        contain.remove(o);
        containID.remove(o.getId());
    }

    public void delObject(ID o) {
        throw new UnsupportedOperationException();
    }

    
    
    /*public List<T> getChildren() {
     CheckContain();
     return Arrays.asList();
     }*/
    public Position getMax() {
        return  contain.isEmpty()?null: (Position)contain.last().getParameters().getObject(param);
    }

    public Position getNext(ID id) {
         Position pos = this.getMax();
        if (pos == null) {
            pos = Application.getApplication().getCurrentFileController().getConcurrencyController().getFirstPosition(id);
        } else {
            pos = pos.genNext(id);
        }
        return pos;
    }

    public Position getMin() {
        return contain.isEmpty()?null:(Position) contain.first().getParameters().getObject(param);
    }

    public void clear() {
        contain.clear();
    }

    /*private void CheckContain() {
        if (contain == null && containID != null) {
            contain = new TreeSet<T>(new GComparator(param));
            for (ID id : containID) {
                T obj = (T) Application.getApplication().getFileManagerController().resolveObject(id);
                if (obj != null) {
                    this.addObject(obj);
                }
            }
        }
    }*/

    /**
     *
     * @return the fr.inria.geditor.elements.GBounds2D
     */
    public Iterator<T> iterator() {
        return contain.iterator();
    }

    public int size() {
        return contain.size();
    }

    public T get(int index) {
        //heu, simulation
        T ret = null;
        Iterator<T> it = this.iterator();
        for (; index > 0 && it.hasNext(); index--) {
            ret = it.next();
        }
        if (index != 0) {
            throw new OutOfMemoryError("Out of bound");
        }
        return ret;


    }

    public T first() {
        return contain.isEmpty()?null:contain.first();
    }

    public T last() {
        return contain.isEmpty()?null:contain.last();
    }

    public boolean contains(T co) {
        return contain.contains(co);
    }
    
    public Collection<T> getCollection() {
        return contain;
    }
}

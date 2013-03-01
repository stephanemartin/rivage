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
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.utils.GComparator;
import fr.inria.rivage.gui.listener.ParameterListener;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class GObjectContainer<T extends GObject> extends GObject implements Iterable<T>, ParameterListener {

    protected Parameters.ParameterType param = Parameters.ParameterType.Zpos;
    protected TreeSet<T> contain = new TreeSet<T>(new GComparator(param));
    protected HashSet<ID> containID = new HashSet<ID>();
    /* protected Position max = null;
     protected Position min = null;*/

    public GObjectContainer() {
    }

    public GObjectContainer(ID id) {
        super(id);
    }

    public GObjectContainer(ID id, GObjectContainer parent) {
        super(id, parent);
    }

    public GObjectContainer(GObjectContainer parent) {
        super(parent);
    }

    /*private int dicSearch(T obj, int begin, int end) {
     return GSortedObject.
     }*/
    /*private static final int MIN = -1;
     private static final int MAX = 1;
    
     private Position setMinMax(T o, int t, Position comp) {
     Position c = (Position) o.getParameters().getObject(param);
     if (comp == null) {
     return c;
     }
     if (c == null) {
     return comp;
     }
     if (c.compareTo(comp) == t) {
     return c;
     }
     return comp;
     }*/
    synchronized public void add(T o) {
        contain.add(o);
        containID.add(o.getId());

        // System.out.println("++"+this+" adds "+o+"->"+containID.size()+":"+contain.size());

    }

    synchronized public void addAll(Collection<T> l) {
        for (T obj : l) {
            add(obj);
        }
    }

    synchronized public void remove(T o) {
        //CheckContain();
        /*int pos = dicSearch(o, 0, contain.size() - 1);
         if (contain.get(pos).equals(o)) {
         contain.remove(pos);
         containID.remove(o.getId());
         }*/
        contain.remove(o);
        containID.remove(o.getId());
    }

    public void remove(ID o) {
        throw new UnsupportedOperationException();
    }

    @Override
    synchronized public void draw(Graphics2D g2) {
        //CheckContain();
        if (this.getParameters().getBooleanD(ParameterType.Visible)) {
            for (T obj : contain) {
                obj.draw(g2);
            }
        }
    }

    @Override
    synchronized public GObject getObjectByPoint(Point2D p, double tolerance) {
        GObject ret = null;
        //CheckContain();
        for (T o : contain) {
            GObject obj = o.getObjectByPoint(p, tolerance);
            if (obj != null) {
                ret = obj;
            }
        }
        return ret;
    }

    @Override
    public List<GObject> getObjectsByRectangle(Rectangle2D r) {
        //CheckContain();
        LinkedList ret = new LinkedList();
        for (T o : contain) {
            ret.addAll(o.getObjectsByRectangle(r));
        }
        return ret;
    }

    /*public List<T> getChildren() {
     CheckContain();
     return Arrays.asList();
     }*/
    public Position getMax() {
        try {
            return (Position) contain.last().getParameters().getObject(param);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    /*public void reordonne(T t){
     contain.remove(t);
     contain.add(t);
     }*/
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
        try {
            return (Position) contain.first().getParameters().getObject(param);
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    public void clear() {
        contain.clear();
        containID.clear();
        //this.gRendreres=null;
    }

    public void clearObj() {
        this.contain.clear();
        this.contain = null;
    }

    public void restaureFromId(GDocument doc) {
        this.contain = new TreeSet<T>(new GComparator(param));
        for (ID id : containID) {
            T obj = (T) doc.getObjectById(id);//Need get the father or global resolver ?
            if (obj != null) {
                contain.add(obj);
            }
        }

    }
    /*   private void CheckContain() {
     if (contain == null && containID != null) {
     contain = new TreeSet<T>(new GComparator(param));
     for (ID id : containID) {
     T obj = (T) Application.getApplication().getFileManagerController().resolveObject(id);//Need get the father or global resolver ?
     if (obj != null) {
     this.add(obj);
     }
     }
     }
     }*/

    /**
     *
     * @return the fr.inria.geditor.elements.GBounds2D
     */
    @Override
    public GBounds2D getEuclidBounds() {
        GBounds2D rec = null;
        for (GObject go : contain) {
            if (rec == null) {
                rec = new GBounds2D(go.getEuclidBounds());
            } else {
                rec.add(go.getEuclidBounds());
            }
        }
        return rec;
    }

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
        for (; index >= 0 && it.hasNext(); index--) {
            ret = it.next();
        }
        if (index != -1) {
            throw new OutOfMemoryError("Out of bound");
        }
        return ret;


    }

    public int getIndex(T e) {
        int ret = 0;
        for (ColObject co : this) {
            if (e.getId().equals(co.getId())) {
                return ret;
            }
            ret++;
        }
        return -1;
    }

    public T first() {
        return contain.first();
    }

    public T last() {
        return contain.last();
    }

    public T lower(T t) {
        return contain.lower(t);
    }

    public T higher(T t) {
        return contain.higher(t);
    }

    public boolean contains(T co) {
        return contain.contains(co);
    }
    /*public T[] toArray(T[] t){
     return contain.toArray(t);
     }
     public Object[] toArray(){
        
     return contain.toArray();
     }*/

    public Collection<T> getCollection() {
        return contain;
    }

    @Override
    public List<GObject> getRealObjects() {
        List<GObject> ret = new LinkedList();
        for (GObject go : contain) {
            ret.addAll(go.getRealObjects());
        }
        return ret;
    }

    @Override
    public String toString() {
        return "GObjectContainer{" + contain + '}';
    }

    public void preModify(Object o) {
        contain.remove((T) o);
    }

    public void postModify(Object o) {
        contain.add((T) o);
    }

    @Override
    public ColObject clone() throws CloneNotSupportedException {
        GObjectContainer cloned = (GObjectContainer) super.clone();
        cloned.contain = (TreeSet) this.contain.clone();
        cloned.containID = (HashSet) this.containID.clone();
        return cloned;
    }
}

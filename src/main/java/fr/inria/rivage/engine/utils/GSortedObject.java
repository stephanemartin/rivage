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
package fr.inria.rivage.engine.utils;

import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GSortedObject<T extends ColObject> implements SortedSet<T> {

    protected ArrayList<T> contain = new ArrayList<T>();
    protected Parameters.ParameterType param = Parameters.ParameterType.Zpos;
    Comparator<? super T> comparator;

    private int dicSearch(T obj, int begin, int end) {
        //CheckContain();
        if (begin > end) {
            return begin;
        }
        int milieu = (begin + end) / 2;
        T oMilieu = contain.get(milieu);
        if (obj.equals(oMilieu)) {
            return milieu;
        }
        //Position pmilieu = (Position) contain.get(milieu).getParameters().getObject(param);
        //Position pobj = (Position) obj.getParameters().getObject(param);
        if (comparator.compare(oMilieu, obj) > 0) {
            //if (pmilieu.compareTo(pobj) > 0) {
            return dicSearch(obj, begin, milieu - 1);
        }
        return dicSearch(obj, milieu + 1, end);


    }

    public int size() {
        return contain.size();
    }

    public boolean isEmpty() {
        return contain.isEmpty();
    }

    public boolean contains(Object o) {
        if (!(o instanceof ColObject)) {
            return false;
        }
        int index = dicSearch((T) o, 0, size());
        return index>=0 && index<size() && ( o.equals(contain.get(index)));
    }

    public Iterator<T> iterator() {
        return contain.iterator();
    }

    public Object[] toArray() {
        return contain.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return contain.toArray(a);
    }

    public boolean add(T e) {
        int index = dicSearch(e, 0, size());
        contain.add(index, e);
        return true;
    }

    public boolean remove(Object o) {
        /*if (o.getClass().isInstance()){
            return false;
        }*/
        int index = dicSearch((T)o, 0, size());
        if(index >=0 && index < size()){
            contain.remove(index);
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object t : c) {
            if (!contains(t)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends T> c) { 
        for (T t : c) {
            add(t);
        }
        return true;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
               boolean ret=false;
        for (T t : c) {
            ret=add(t)?true:ret;
        }
        return ret;
    }

    public boolean removeAll(Collection<?> c) {
        boolean ret=false;
        for (Object t : c) {
            ret=remove(t)?true:ret;
        }
        return ret;
        
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        contain.clear();
    }

    public Comparator<? super T> comparator() {
        return comparator;
    }

    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public T first() {
        return contain.get(0);
    }

    public T last() {
        return contain.get(size() - 1);
    }

    public T get(int index) {
        return contain.get(index);
    }
}

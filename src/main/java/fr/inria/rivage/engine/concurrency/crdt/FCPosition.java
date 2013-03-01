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
package fr.inria.rivage.engine.concurrency.crdt;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Position;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class FCPosition implements Position {

    final List<Byte> position;
    //final private ID id;

    public FCPosition(ID id) {
        //this.id = id;
        position = new LinkedList();
        position.add(new Byte((byte) 0));
        addId(position, id);
    }

    /*    public void setId(ID id) {
     +this.id = id;
     }*/
    public static void addId(List<Byte> l, ID id) {
        for (byte bb : id.toString().getBytes()) {
            l.add(bb);
        }
    }

    /**
     * convert a position with identifier string representation to sequence of
     * byte to ordering them
     *
     * @param b position bytes
     * @param id identifier
     * @return b and id are concatened.
     */
    public static LinkedList<Byte> conv(List<Byte> pos, ID id) {
        LinkedList<Byte> s1 = new LinkedList<Byte>();
        s1.addAll(pos);
        s1.add(Byte.MAX_VALUE);
        addId(s1, id);
        return s1;
    }

    int compareTo(FCPosition f2) {
        Iterator<Byte> s1 = this.getPosition().iterator();
        Iterator<Byte> s2 = f2.getPosition().iterator();

        while (s1.hasNext() && s2.hasNext()) {
            byte b1 = s1.next();
            byte b2 = s2.next();
            if (b1 < b2) {
                return -1;
            }
            if (b1 > b2) {
                return 1;
            }
        }
        if (s1.hasNext()) {
            return 1;
        }
        if (s2.hasNext()) {
            return -1;
        }
        return 0;
    }

    /**
     * Make object from position.
     *
     * @param position
     */
    public FCPosition(ID id, List<Byte> position) {
        this.position = conv(position, id);
        //this.id=id;
    }

    /**
     * Return the position
     *
     * @return
     */
    @Override
    public List<Byte> getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FCPosition other = (FCPosition) obj;
        if (!this.position.equals(other.position)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.position != null ? this.position.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "FCPosition{" + position + '}';
    }

    @Override
    public Position genNext(ID newId) {
        return new FCPosition(newId, createBetween(this, null));
    }

    @Override
    public Position genPrevious(ID newId) {
        return new FCPosition(newId, createBetween(null, this));
    }

    @Override
    public Position genBetween(Position p, ID newId) {
        return new FCPosition(newId, createBetween(this, (FCPosition) p));
    }

    @Override
    public int compareTo(Position obj) {
        if (obj==null){
            return 1;
        }
        if (obj instanceof FCPosition) {
            return this.compareTo((FCPosition) obj);
        }
        throw new UnsupportedOperationException("Uncomparable position." + obj);
    }

    List<Byte> createBetween(FCPosition n1, FCPosition n2) {
        // less is n1
        if(n1!=null && n2!=null && n1!=null && n2.compareTo(n1)==-1){
            FCPosition n3=n2;
            n2=n1;
            n1=n3;
            
        }
        Iterator<Byte> s1;
        Iterator<Byte> s2;
        
        /*
         * if(n1==null && n2==null){ return new FCPosition(new ArrayList(new
         * Byte[]{(byte)(Math.random()*(Byte.MAX_VALUE+Byte.MIN_VALUE)+Byte.MIN_VALUE)}));
         }
         */
        
        if (n1 == null) {
            s1 = new infinitString(Byte.MIN_VALUE);
        } else {
            s1 = n1.getPosition().iterator();//FCPosition.conv(n1).iterator();
        }

        if (n2 == null) {
            s2 = new infinitString((byte) (Byte.MAX_VALUE - 1));
        } else {
            s2 = n2.getPosition().iterator();//FCPosition.conv(n2).iterator();
        }
        

        LinkedList<Byte> sb = new LinkedList();
        while (s1.hasNext() && s2.hasNext()) {
            byte b1 = s1.next();
            byte b2 = s2.next();
            if (b2 - b1 > 1) {

                sb.addLast(new Byte((byte) ((b1 + b2) / 2)));
                break;
            } else {
                sb.addLast(b1);
            }
        }
        if (s1.hasNext()) {
            sb.addLast(s1.next());
        }
        return sb;
    }

    public Position getMax(Position pos) {
        if (this.compareTo(pos) > 0) {
            return this;
        } else {
            return pos;
        }
    }

    class infinitString implements Iterator<Byte> {

        byte ch;

        public infinitString(byte ch) {
            this.ch = ch;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Byte next() {
            return ch;
        }
    }
}

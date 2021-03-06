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

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public interface Position extends Comparable<Position> , Serializable {
    
    @Override
    public boolean equals(Object obj);
    
   
    @Override
    public int compareTo(Position obj);
    /**
     * Return the position
     * @return
     */
    public List<Byte> getPosition();

    @Override
    public int hashCode();
    
    public Position genNext(ID newId);
    public Position genPrevious(ID newId);
    public Position genBetween(Position p, ID newId); 
    public Position getMax(Position pos);
//    public int getIntPosition(List l);
    
    


    
}

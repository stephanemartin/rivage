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
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GComparator implements Comparator<ColObject>,Serializable{

    public GComparator(ParameterType param) {
        this.param = param;
    }

    
    ParameterType param;
    public int compare(ColObject o1, ColObject o2) {
        Comparable c1=(Comparable)o1.getParameters().getObject(param);
        Comparable c2=(Comparable)o2.getParameters().getObject(param);
        if(c1==null){
            if (c2==null){
                return 0;
            }else{
                return -1;
            }
        }
        return c1.compareTo(c2);
    }
    
}

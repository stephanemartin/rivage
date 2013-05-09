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

import fr.inria.rivage.engine.concurrency.tools.FactoryParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameter;
import fr.inria.rivage.engine.concurrency.tools.Parameters;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTFactoryParameter implements FactoryParameter  {
    //private UUID localUUID;

    //private GDocument doc;

    /*public CRDTFactoryParameter(GDocument doc) {
        this.doc = doc;
    }*/

    /* public CRDTFactoryParameter(ConcurrencyControllerCRDT cc) {
     this(id.getSiteID(),cc);
     }*/
  

   

    public Parameter create(Parameters.ParameterType param, ID target, ID doc) {
        return new CRDTParameter(doc, target, param);
    }
}

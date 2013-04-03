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

import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.engine.operations.Undo;
import java.util.LinkedList;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTHistory {
    private int limit=200;
    ConcurrencyControllerCRDT cc;
    
    LinkedList <Operation> history;
    //TODO: Progress me
    //nom de l'opération pour l'interface ?
    public void addOperation(Operation op){
        history.addLast(op);
        if(limit>0 && history.size()>limit){
            history.pollFirst();
        }
    }
    public void undoLastOperation(){
        if(history.size()>0){
            cc.sendOperation(
            new Undo(history.getLast()));
        }
    }
    
    
            
    
}

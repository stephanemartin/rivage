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
package fr.inria.rivage.net.group;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.operations.Operation;
import java.io.Serializable;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class  Message implements Serializable {
    private ID documentId;
    private Operation op;

    public Message(ID documentId, Operation op) {
        this.documentId = documentId;
        this.op = op;
    }

    public ID getDocumentId() {
        return documentId;
    }
    public ID getId(){
        return op.getId();
    }
   

    public Operation getOp() {
        return op;
    }

    @Override
    public String toString() {
        return "Message{" + "documentId=" + documentId + ", op=" + op + '}';
    }

   
    
}

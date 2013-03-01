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

import fr.inria.rivage.engine.concurrency.tools.DocID;
import java.util.UUID;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTDocID implements DocID {
    private final UUID docId;

    public CRDTDocID(UUID docId) {
        this.docId = docId;
    }

    public UUID getDocId() {
        return docId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.docId != null ? this.docId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CRDTDocID other = (CRDTDocID) obj;
        if (this.docId != other.docId && (this.docId == null || !this.docId.equals(other.docId))) {
            return false;
        }
        return true;
    }
    
}

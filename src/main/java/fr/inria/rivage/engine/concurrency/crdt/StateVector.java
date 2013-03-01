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

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class StateVector implements Serializable{

    HashMap<UUID, Long> sv = new HashMap<UUID, Long>();

    public static enum Status {

        Ready, Already, NotReady
    };

    /*public boolean isReady(CRDTID id) {
        Long nbOp = sv.get(id.getSiteID());
        return (nbOp == null && id.getNbOperation() == 1) || (id.getNbOperation() == nbOp + 1);
    }*/

    public Status getStatus(CRDTID id) {
        Long l = sv.get(id.getSiteID());
        long nbOp = l==null?0:l;
        if ( (id.getNbOperation() == nbOp + 1)) {
            return Status.Ready;
        } else if (id.getNbOperation() <= nbOp) {
            return Status.Already;
        } else {
            return Status.NotReady;
        }
    }

    public long getBySite(CRDTID id) {
        return sv.get(id.getSiteID());
    }

    public void setSiteID(UUID id, long nbOp) {
        sv.put(id, new Long(nbOp));
    }

    public void setSiteID(CRDTID id) {
        setSiteID(id.getSiteID(), id.getNbOperation());
    }

    @Override
    public String toString() {
        return "StateVector{" + "sv=" + sv + '}';
    }
}

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

import java.util.UUID;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTIDFactory {

   
    private final ConcurrencyControllerCRDT cc;
    public long operationCounter = 0;
    public UUID siteIDc;

    public UUID getSiteIDc() {
        return siteIDc;
    }

    public CRDTIDFactory(ConcurrencyControllerCRDT cc) {
        siteIDc = UUID.randomUUID();
        this.cc = cc;
    }

   
    public CRDTID getNewId() {
        synchronized (cc) {
            CRDTID id = new CRDTID(siteIDc, ++operationCounter);
            cc.getStateVector().setSiteID(id);
            return id;
        }
    }

    public CRDTID getNullID() {
        return new CRDTID(siteIDc, 0);
    }
}

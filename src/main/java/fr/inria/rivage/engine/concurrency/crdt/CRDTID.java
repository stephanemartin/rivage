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

import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.DocID;
import fr.inria.rivage.engine.concurrency.tools.ID;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTID implements Comparable<ID> ,ID, Serializable {
    /*public static long operationCounter=0;
    public static UUID siteIDc=UUID.randomUUID();*/
    
    
    long nbOperation;
    UUID siteID;
    
    @Override
    public int compareTo(ID o) {
        CRDTID id2=(CRDTID)o;
        int cSite=siteID.compareTo(id2.siteID);
        if (cSite!=0) {
            return cSite;
        }
        if (nbOperation== id2.nbOperation){
            return 0;
        }
        if (nbOperation> id2.nbOperation){
            return 1;
        }
        return -1;
        
    }

    public CRDTID(UUID siteID,long nbOperation) {
        /*nbOperation=operationCounter++;
        siteID=siteIDc;*/
        this.siteID = siteID;
        this.nbOperation = nbOperation;
        
    }

    public UUID getSiteID() {
        return siteID;
    }

    public long getNbOperation() {
        return nbOperation;
    }

    @Override
    public String toString() {
        return "(" +  nbOperation + "," + siteID + ')';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.nbOperation ^ (this.nbOperation >>> 32));
        hash = 43 * hash + (this.siteID != null ? this.siteID.hashCode() : 0);
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
        final CRDTID other = (CRDTID) obj;
        if (this.nbOperation != other.nbOperation) {
            return false;
        }
        if (this.siteID != other.siteID && (this.siteID == null || !this.siteID.equals(other.siteID))) {
            return false;
        }
        return true;
    }

 
    /*public DocID extractDocID() {
        return new CRDTDocID(siteID);
    }*/

    

    public boolean isLocal(IConcurrencyController cc) {
        return cc.isOurID(this);
    }
    
}

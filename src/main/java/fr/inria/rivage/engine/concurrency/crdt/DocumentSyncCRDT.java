/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.rivage.engine.concurrency.crdt;

import fr.inria.rivage.engine.concurrency.DocumentSync;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.elements.GDocument;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class DocumentSyncCRDT implements DocumentSync {
    StateVector stateVector;
    GDocument docu;

    public DocumentSyncCRDT() {
    }

    public DocumentSyncCRDT(StateVector stateVector, GDocument docu) {
        this.stateVector = stateVector;
        this.docu = docu;
    }

    public StateVector getStateVector() {
        return stateVector;
    }

    public GDocument getGDocument() {
        return docu;
    }

    public ID getID() {
        return docu.getId();
    }

    @Override
    public String toString() {
        return "DocumentSyncCRDT{" + "stateVector=" + stateVector + ", docu=" + docu + '}';
    }

    
    
}

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

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameter;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.ModifyOperation;
import fr.inria.rivage.engine.operations.Operation;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTParameter<T> extends Parameter<T> {
    int nice=0;
    long version = -1;
    long versionLoc= -1;
    UUID id;
    //private transient UUID localId;*/
    transient ConcurrencyControllerCRDT cc;
    transient T distElement = null;

    public ConcurrencyControllerCRDT getCc() {
        if (cc == null) {
            FileController fc = Application.getApplication().getFileManagerController().getFileControlerById(idFC);
            cc = (ConcurrencyControllerCRDT) fc.getConcurrencyController();
        }
        return cc;
    }

    /*public CRDTParameter() {
     //this.localId = getCc().getCrdtID().siteIDc;
     //this.doc = doc;
     }*/
   

    public CRDTParameter(ID idFC, ID target, Parameters.ParameterType type) {
        super(idFC, target, type);
    }

    public CRDTParameter(Parameters.ParameterType type, ID idDoc) {
        super(type, idDoc);
    }

    

    /* public CRDTParameter(T element, long version, UUID id, UUID localId) {
     this.element = element;
     this.version = version;
     this.id = id;
     }*/
    public synchronized void update(UUID id, long version, T element) {
        //synchronized (getCc()) {
            if (version < this.version) {
                return;
            }

            if (version == this.version && id.compareTo(id) < 0) {
                return;
            }

            this.element = element;
            this.distElement = element;
            this.id = id;
            this.version = version;
       // }
    }

    public synchronized void update(CRDTParameter<T> up) {
        //synchronized (getCc()) {
            if (up.version < this.version) {
                return;
            }

            if (up.version == this.version && (up.nice > this.nice || id.compareTo(up.id) < 0)) {
                return;
            }

            this.element = up.element;
            this.distElement = up.element;
            this.id = up.id;
            this.version = up.version;
            
        //}
        /* 
         * TODO : notify UI?
         */
    }

    @Override
    public synchronized void remoteUpdate(Parameter<T> up) {
        //synchronized (getCc()) {
            if (up instanceof CRDTParameter) {
                this.update((CRDTParameter) up);
            } else {
                throw new UnsupportedOperationException("Not good parameter object.");

            }
        //}
    }
   /* private void inUp(T el){
        for(ParameterListener pl:this.listener){
            pl.postModify(this);
        }
            
            this.element=el ;
    }*/
    /**
     *
     * @param t the value of t
     * @param nice the value of nice
     */
    @Override
    public synchronized void localUpdate(T t, int nice) {
//        synchronized (getCc()) {
            this.element = t;
            this.nice=nice;
 //       }
    }

    public synchronized void acceptMod() {
       // synchronized (getCc()) {
            if (distElement != element) {
                version++;
                distElement = element;
                id = getCc().getCrdtID().getSiteIDc();
            }
      //  }
    }

    public synchronized void sendMod(ID id) {
        // System.out.println("\n\n send mod \n");
       // synchronized (getCc()) {
            if(element != distElement){
            //if ((element == null && distElement!=null) || (element!=null && !element.equals(distElement))) {
                try {
                    acceptMod();
                    Operation op=new ModifyOperation(target, (CRDTParameter) this.clone());
                    if (id!=null){
                        op.setId(id);
                    }
                    getCc().sendOperation(op);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(CRDTParameter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       // }
    }

    @Override
    public String toString() {
        return "CRDTParameter{" + "version=" + version + ",type=" + this.type + ", id=" + id + ",Element" + this.getElement() + '}';
    }

    /*test*/
    public long getVersion() {
        return version;
    }

    public UUID getId() {
        return id;
    }

    public void lock(){
        //TODO : make copy of state
        throw new UnsupportedOperationException("not implemented yet");
    }
    public void unlock(){
        //TODO : merge copy distant and local
        throw new UnsupportedOperationException("not implemented yet");
    }
    @Override
    public Parameter fork() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

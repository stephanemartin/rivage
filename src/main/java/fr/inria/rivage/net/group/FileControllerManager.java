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

import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.tools.DocID;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.net.overlay.IOverlay;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class FileControllerManager extends Observable/*implements GroupController*/ {
    /*TODO : reflexion sur le fait d'avoir deux liste séparées pour les synchronisé et les autres 
     * 
     */

    List<FileController> fcs = new LinkedList();
    // List<FileController> openableFcs = new LinkedList();
    HashMap<DocID, FileController> synchronizedFc = new HashMap<DocID, FileController>();
    IOverlay net;

    /*public List<FileController> getOpenableFcs() {
     return openableFcs;
     }*/
    public FileControllerManager(IOverlay net) {
        this.net = net;
    }

    public List<FileController> getFiles() {
        return fcs;
    }

    public Collection<FileController> getSynchronizedFileList() {
        return synchronizedFc.values();
    }
    /*public FileController loadFile(String filename) throws IOException, ClassNotFoundException {
     throw new UnsupportedOperationException("Not supported yet.");
     }*/

    public FileController getFileControlerById(ID id) {
        return getFileControlerById(id.extractDocID());
    }

    public FileController getFileControlerById(DocID id) {
        return this.synchronizedFc.get(id);
    }

    public void registerRemoteNewFile(FileController fc) {

        if (synchronizedFc.containsKey(fc.getId().extractDocID())) {
            return;
        }
        fcs.add(fc);
        //if (fc.getDocument() != null) {
        synchronizedFc.put(fc.getId().extractDocID(), fc);
        //} 
        System.out.println("-= ASK UPDATE =-" + fc);
        this.setChanged();
        this.notifyObservers();
    }

    public void registerNewFile(FileController fc) {
        registerRemoteNewFile(fc);
        net.informNewFile(fc);

    }

    public void askDocument(FileController fc) {
        net.askDocument(fc.getId());
    }

    public void sendMessage(FileController fc, Operation op) {
        net.sendToAll(new Message(fc.getId(), op));
    }

    public void hasMessage(Message mess) {
        FileController fc = synchronizedFc.get(mess.getDocumentId().extractDocID());
        if (fc != null &&fc.getConcurrencyController()!=null ) {
            fc.getConcurrencyController().recieveOperation(mess.getOp());
        }
    }

    public GObject resolveObject(ID id) {
        /*FileController fc = this.synchronizedFc.get(id.extractDocID());
         if (fc == null || fc.getDocument() == null) {
         return null;
         }
         return fc.getDocument().getObjectById(id);*/
        throw new UnsupportedOperationException("Doesn't work");

    }
}

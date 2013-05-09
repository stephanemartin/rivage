/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2013 LORIA / Inria / SCORE Team
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
package fr.inria.rivage.tools;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class ObservableSerializable implements Serializable {

    final ID fCId;
    LinkedList<ID> observerID = new LinkedList<ID>();
    transient LinkedList<ObserverID> observer;

    public ObservableSerializable(ID fCId) {
        this.fCId = fCId;
    }

    private void checkObserver() {
        if (observer == null) {
            observer = new LinkedList();
            GDocument doc = Application.getApplication().getFileManagerController().getFileControlerById(fCId).getDocument();
            for (ID id : observerID) {
                observer.add((ObserverID) doc.getObjectById(id));
            }
        }
    }

    public void notifyAllObserver(Object o) {
        checkObserver();
        for(ObserverID ob:observer){
            ob.update(null, o);
        }
    }

    public void addObserver(ObserverID ob) {
        observerID.add(ob.getId());
        if (observer != null) {
            observer.add(ob);
        }
    }

    public void removeObserver(ObserverID ob) {
        checkObserver();
        observerID.remove(ob.getId());
        if (observer != null) {
            observer.remove(ob);
        }
    }

    public int countObserver() {
        return observerID.size();
    }
}

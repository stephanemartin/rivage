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
package fr.inria.rivage.net.overlay.rmi;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.net.group.FileControllerManager;
import fr.inria.rivage.net.group.Message;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class RMIComputer implements IRMIComputer{

    public RMIComputer(String name, FileControllerManager fcm, RMINetwork rmin) {
        this.name = name;
        this.fcm = fcm;
        this.rmin = rmin;
    }
    
    String name;
    FileControllerManager fcm;
    RMINetwork rmin;
    public String getName() {
       return name;
    }

    public void sendMessage(Message mess) {
      rmin.getInputQueue().enqueue(mess);
    }

    /**
     *
     */
    public void askKnownComputerList() {
        rmin.getConnectedMachine();
       
    }

    public void askGDocument(ID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

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
package fr.inria.rivage.net.overlay.tcp;

import fr.inria.rivage.engine.operations.Operation;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class Reemitor {
     /****** Replay **/
    //TODO : this 
    HashMap <UUID,Long> stateVector;
    List <Operation> waitBeforeReemit;
    TCPServerWithDiscover serv;
    List<Computer> suscribers;
    
    /****** /Replay **/
    synchronized public void addSuscriber(Computer sock){
        suscribers.add(sock);
    }
    synchronized public void reEmit(Object op){
        
        //TODO : check if is not already reemited
        for (Computer c:suscribers){
            c.sendObject(op);
        }
    }
    
    synchronized public void emit(){
        
    }
}

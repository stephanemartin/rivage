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

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.net.group.Message;
import fr.inria.rivage.net.overlay.IComputer;
import fr.inria.rivage.net.overlay.IOverlay;
import fr.inria.rivage.tools.Configuration;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class Node extends Observable implements  Runnable{

 ServerSocket server;
    Computer me;
    Discoverer discovery;
    public final static Logger logger=Logger.getLogger(Class.class.getName());
    int port = Configuration.getConfiguration().SERVER_PORT;
    List<Computer> knownComputer = new LinkedList();
    List<Computer> connectedComputer = new LinkedList();
    //a kind of garbage with undelivered messages. future implementation
    HashSet undeleveredMessages;
    public Computer getMe() {
        return me;
    }
    
/*
    public Node() throws UnknownHostException {
        me = new Computer(this, InetAddress.getLocalHost(), port, InetAddress.getLocalHost().getHostName());
    }

    public static boolean isLocalAddress(InetAddress i) {
        if (i.isAnyLocalAddress() || i.isLoopbackAddress()) {
            return true;
        }
        try {
            return NetworkInterface.getByInetAddress(i) != null;
        } catch (SocketException ex) {
            logger.log(Level.WARNING, ex.toString());
            return false;
        }
    }

    void addMachine(Computer comp) {
        if (!isLocalAddress(comp.getUri())) {
            logger.log(Level.INFO, "Machine {0} is added", comp);
            int index = knownComputer.indexOf(comp);
            if (index < 0) {

                knownComputer.add(comp);
            } else {
                comp = knownComputer.get(index);
            }
            if (!comp.isConnected()) {
                try {
                    comp.connect();
                    connectedComputer.add(comp);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void discovery() {
        try {
            discovery.sendRalliment();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void connectToMachine(IComputer m) {
        Computer c = (Computer) m;
        try {
            c.connect();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public List<? extends IComputer> getConnectedMachine() {
        return knownComputer;
    }

    public void start() {
        try {
            this.run = true;
            Thread th = new Thread(this);
            server = new ServerSocket(port);
            th.start();
            //discovery = new Discoverer(this);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        run = false;
        try {
            server.close();
        } catch (IOException ex) {
           logger.log(Level.SEVERE, null, ex);
        }
    }

    public boolean isRunning() {
        return run;
    }

    public void changeName(String name) {
        me.setName(name);
    }
    boolean run = true;

    public void run() {
        try {

            //InetSocketAddress addr=new InetSocketAddress(InetAddress.getLocalHost(),port);
            ///server.bind(addr);
            while (run) {
                Socket connect = server.accept();
                Computer c = new Computer(this, connect);
                connectedComputer.add(c);
                knownComputer.add(c);
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        run = false;
    }

    public void sendToAll(Object op) {
        for (Computer c : connectedComputer) {
            c.sendObject(op);
        }
    }

    public List<? extends IComputer> getknownMachine() {
        return knownComputer;
    }

    public void askDocument(ID id) {
        sendToAll(new ActionPacket(ActionPacket.Action.GetDocument, id));
    }

    public void sendToAll(Message mess) {
        this.sendToAll((Object) mess);
    }

    public void informNewFile(FileController fc) {
        logger.log(Level.INFO, "inform{0}", fc);    
        sendToAll(new ActionPacket(ActionPacket.Action.File, fc.getId(), fc.getFileName()));
    }

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }*/

    public void discovery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connectToMachine(IComputer m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<? extends IComputer> getknownMachine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<? extends IComputer> getConnectedMachine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void askDocument(ID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendToAll(Message mess) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void informNewFile(FileController fc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRunning() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void changeName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
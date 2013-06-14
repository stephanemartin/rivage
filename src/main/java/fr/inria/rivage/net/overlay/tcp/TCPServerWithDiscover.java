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
import java.net.BindException;
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
import javax.swing.JOptionPane;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class TCPServerWithDiscover extends Observable implements IOverlay, Runnable {

    ServerSocket server;
    Computer me;
    Discoverer discovery;
    public final static Logger logger = Logger.getLogger(Class.class.getName());
    int port = Configuration.getConfiguration().SERVER_PORT;
    List<Computer> knownComputer = new LinkedList();
    List<Computer> connectedComputer = new LinkedList();
    List<Computer> slavesComputer = new LinkedList();
    //a kind of garbage with undelivered messages. future implementation
    HashSet undeleveredMessages;
    private boolean allowMultipleConnection = false;

    public Computer getMe() {
        return me;
    }

    public TCPServerWithDiscover() throws UnknownHostException {
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

    synchronized public void addMachine(IComputer compi) {
        Computer comp = (Computer) compi;
        if (!isLocalAddress(comp.getUri())) {
            comp.setTcpServer(this);
            int index = knownComputer.indexOf(comp);
            if (index < 0) {
                knownComputer.add(comp);
            } else {
                comp = knownComputer.get(index);
            }
            logger.log(Level.INFO, "Machine {0} is added" + (comp.isConnected() ? " and already connected" : " connecting ... "), comp);
            if (!comp.isConnected()) {
                try {
                    comp.connect();
                    connectedComputer.add(comp);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        this.notifyByComputer();
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

    synchronized public List<? extends IComputer> getConnectedMachine() {
        return new LinkedList(knownComputer);
    }

    public void start() {
        try {
            this.run = true;
            try {
                server = new ServerSocket(port);
                Thread th = new Thread(this);
                th.start();

            } catch (BindException ex) {
                logger.log(Level.INFO, "Port{0} is already in use", port);
                logger.log(Level.WARNING, "{0}", ex);
                if (allowMultipleConnection) {
                    logger.log(Level.INFO, " try to connect to localhost");
                    Computer c = new Computer(this, InetAddress.getLocalHost(), port, InetAddress.getLocalHost().getHostName());
                    me.setName(null);
                    this.connectToMachine(c);
                } else {
                    logger.log(Level.SEVERE, " multi-hosting not possible yet");
                    JOptionPane.showMessageDialog(null, "Error : The port " + port + " is already in use", "Port listen failled", JOptionPane.ERROR_MESSAGE);
                    System.exit(-42);
                }
            }
            try {
                discovery = new Discoverer(this);
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Discoverer Oops", ex);
            }
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
                synchronized (this) {
                    connectedComputer.add(c);
                    knownComputer.add(c);
                    //this.addMachine(c);
                }
                this.notifyByComputer();
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        run = false;
    }

    synchronized public void sendToAll(Object op) {
        for (Computer c : connectedComputer) {
            c.sendObject(op);
        }
    }

    synchronized public List<? extends IComputer> getknownMachine() {
        return new LinkedList(knownComputer);
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
    }

    public void notifyByComputer() {
        try {
            this.setChanged();
            this.notifyObservers();
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Interface sucks: {0}", ex);
        }
    }

    /**
     *
     * @param computer the value of computer
     * @return the String
     */
    public String addSlaveMachine(IComputer computer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    synchronized public void connectTo(String addr) throws Exception {
        Computer computer = null;
        try {
            InetAddress inetAddr;
            inetAddr = InetAddress.getByName(addr);
            computer = new Computer(this, inetAddr, this.port, null);
            this.addMachine(computer);
            //Optionnal ?
            computer.askKnownComputerList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to connect{0}", ex);
            if (computer != null) {
                this.knownComputer.remove(computer);
            }
            throw ex;
        }
    }
}

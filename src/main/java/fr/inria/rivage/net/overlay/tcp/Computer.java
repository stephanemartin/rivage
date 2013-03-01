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

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.DocumentSync;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.net.group.Message;
import fr.inria.rivage.net.overlay.IComputer;
import fr.inria.rivage.net.overlay.IOverlay;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class Computer implements IComputer, Serializable {

    private String name;
    private InetAddress uri;
    private int port;
    transient private IOverlay tcpServer;
    transient private Socket socket;
    transient private ObjectOutputStream output;
    transient private Reciever receiver = new Reciever();
    transient private Sender sender = new Sender();

    public Computer(IOverlay tcpServer, Socket socket) throws IOException {
        this.tcpServer = tcpServer;
        this.socket = socket;
        output = new ObjectOutputStream(socket.getOutputStream());
        this.uri = socket.getInetAddress();
        this.port = socket.getLocalPort();
        this.setRun();
    }

    public Computer(IOverlay tcpServer, InetAddress uri, int port, String name) {
        this.uri = uri;
        this.port = port;
        this.tcpServer = tcpServer;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void connect() throws IOException {
        socket = new Socket(uri, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        this.setRun();

    }

    public boolean isConnected() {
        return run;
    }

    public InetAddress getUri() {
        return uri;
    }

    public void setUri(InetAddress uri) {
        this.uri = uri;
    }

    public void sendMessage(Message mess) {
        sendObject(mess);
    }

    void sendHelloProtocol() throws IOException {
        this.sendObject(new ActionPacket(ActionPacket.Action.MyNameIs, null, tcpServer.getMe().getName()));
        sendFileList();
    }

    synchronized public void sendObject(Object obj) {
        this.sender.sendObject(obj);

    }

    /**
     *
     */
    public void askKnownComputerList() {
        sendObject(new ActionPacket(ActionPacket.Action.GetKnowMachineList));
    }

    public void sendFileList() throws IOException {
        Collection<FileController> fcs = Application.getApplication().getFileManagerController().getFiles();
        for (FileController fc2 : fcs) {
            this.sendObject(new ActionPacket(ActionPacket.Action.File, fc2.getId(), fc2.getFileName()));
        }
    }
    boolean run = false;

    private void setRun() throws IOException {
        run = true;
        receiver.start();
        sender.start();

        this.sendHelloProtocol();
        this.tcpServer.notifyByComputer();
    }

    @Override
    public String toString() {
        return "Computer{" + "name=" + name + ", uri=" + uri.getHostAddress() + ", port=" + port + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        hash = 79 * hash + this.port;
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
        final Computer other = (Computer) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }

    class Reciever implements Runnable {

        Thread thRec;

        public void start() {
            thRec = new Thread(this);
            thRec.start();
        }

        public void run() {
            try {
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                while (run) {
                    Object obj = input.readObject();
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "recieve: {0}", obj.toString());
                    if (obj instanceof ActionPacket) {
                        ((ActionPacket) obj).doAction(Computer.this, tcpServer);
                    } else if (obj instanceof Message) {
                        //tcpServer.
                        Application.getApplication().getFileManagerController().hasMessage((Message) obj);
                    } else if (obj instanceof Computer) {
                        tcpServer.addMachine((Computer) obj);
                    } else if (obj instanceof DocumentSync) {
                        DocumentSync doc = (DocumentSync) obj;
                        FileController fc = Application.getApplication().getFileManagerController().getFileControlerById(doc.getID());
                        if (fc != null) {
                            fc.getConcurrencyController().setSyncInfo(doc);
                        }
                    }
                }
            } catch (EOFException ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.INFO, "{0} is deconnected", Computer.this.name);
            } catch (Exception ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
            }
            run = false;
            tcpServer.notifyByComputer();


        }
    }

    class Sender implements Runnable {

        transient private Thread thSend;
        transient private LinkedList sendList = new LinkedList();

        public Sender() {
        }

        public void start() {
            thSend = new Thread(this);
            thSend.start();
        }

        public void run() {
            try {
                while (run) {
                    if (sendList.size() > 0) {
                        Object obj = sendList.getFirst();
                        Logger.getLogger(getClass().getName()).log(Level.INFO, "Send{0} to {1}", new Object[]{obj.toString(), Computer.this.getName()});
                        try {
                            output.writeObject(obj);
                            output.flush();
                        } catch (Exception ex) {
                            System.err.println("++++ERREUR : " + obj.getClass().getName() + "\n\n");
                            ex.printStackTrace();
                            if (ex instanceof IOException) {
                                throw (IOException) ex;
                            }

                        }
                        sendList.pollFirst();

                    } else {
                        try {
                            synchronized (this) {
                                this.wait();
                            }
                        } catch (InterruptedException ex) {
                            //Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
            }
            run = false;
            tcpServer.notifyByComputer();

        }

        synchronized public void sendObject(Object obj) {
            sendList.addLast(obj);
            notifyAll();
        }
    }
}

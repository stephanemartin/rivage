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

    private static final Logger LOG = Logger.getLogger(Computer.class.getName());
    private String name;
    private InetAddress uri;
    private int port;
    transient boolean run = false;
    transient private IOverlay tcpServer;
    transient private Socket socket;
    transient private ObjectOutputStream output;
    transient private Reciever receiver;
    transient private Sender sender;

    public Computer(IOverlay tcpServer, Socket socket) throws IOException {
        this.tcpServer = tcpServer;
        this.socket = socket;
        //output = new ObjectOutputStream(socket.getOutputStream());
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
        //output = new ObjectOutputStream(socket.getOutputStream());
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
        LOG.log(Level.INFO, "send Hello to {0}", name);
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

    public void setTcpServer(IOverlay tcpServer) {
        this.tcpServer = tcpServer;
    }

    private void setRun() throws IOException {

        if (receiver == null) {
            receiver = new Reciever();
        }/* else {
            System.out.println("reciever is "+receiver.isAlive());
            if (receiver.isAlive()) {
                receiver.stop();
            }
        }*/
        if (sender == null) {
            sender = new Sender();
        } /*else {
            System.out.println("sender is "+sender.isAlive());
            if (sender.isAlive()) {
                sender.stop();
            }
        }*/
        run = true;
        sender.start();
        receiver.start();


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

        ObjectInputStream input;
        Thread thRec;

        public void start() {
            thRec = new Thread(this);
            thRec.start();
        }

        public void run() {
            try {

                input = new ObjectInputStream(socket.getInputStream());

                while (run) {
                    Object obj = input.readObject();
                    LOG.log(Level.INFO, "recieve: {0}", obj);
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
                LOG.log(Level.INFO, "{0} is deconnected", Computer.this.name);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            LOG.log(Level.INFO, "Reciever {0} is down", Computer.this.getName());
            sender.stop();
            run = false;
            tcpServer.notifyByComputer();
        }

        public boolean isAlive() {
            return thRec.isAlive();
        }

        public void stop() {
            if(!thRec.isAlive()){
                return;
            }
            run = false;
            try {
                if (input != null) {
                    input.close();
                }
                //thRec.join();
            } catch (IOException ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
            } /*catch (InterruptedException ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            
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
                output = new ObjectOutputStream(socket.getOutputStream());
                while (run) {
                    if (sendList.size() > 0) {
                        Object obj = sendList.getFirst();
                        LOG.log(Level.INFO, "Send{0} to {1}", new Object[]{obj.toString(), Computer.this.getName()});
                        try {
                            output.writeObject(obj);
                            output.flush();
                        } catch (Exception ex) {
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
            receiver.stop();
            LOG.log(Level.INFO, "sender {0} is down", Computer.this.getName());
            tcpServer.notifyByComputer();

        }

        synchronized void stop() {
            if(!thSend.isAlive()){
                return;
            }
            run = false;
            sendList.clear();
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.notifyAll();
            /*try {
                thSend.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }

        boolean isAlive() {
            return thSend.isAlive();
        }

        synchronized public void sendObject(Object obj) {
            //LOG.log(Level.INFO, "{0} is added to send list of {1}", new Object[]{obj, name});
            sendList.addLast(obj);
            notifyAll();
        }
    }
}

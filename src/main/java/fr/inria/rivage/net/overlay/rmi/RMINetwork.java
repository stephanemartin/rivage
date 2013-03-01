/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.rivage.net.overlay.rmi;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.net.group.Message;
import fr.inria.rivage.net.overlay.IComputer;
import fr.inria.rivage.net.overlay.IOverlay;
import fr.inria.rivage.net.queues.InputQueue;
import fr.inria.rivage.net.queues.OutputQueue;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Observer;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class RMINetwork implements IOverlay{
    List <RMIComputer> connected;
    RMIComputer me;
    
    
    @Override
    public void start() {
        try {
            me=new RMIComputer(InetAddress.getLocalHost().getHostName(), null, this);
            
            IRMIComputer stub=(IRMIComputer) UnicastRemoteObject.exportObject(me);
            LocateRegistry.createRegistry(1099);
            Registry registry=LocateRegistry.getRegistry();
            registry.rebind("CRDTGeditor", stub);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRunning() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

  

    
    public void connectToMachine(RMIComputer m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<RMIComputer> getConnectedMachine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputQueue getInputQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OutputQueue getOutputQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connectToMachine(IComputer m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void changeName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void discovery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<? extends IComputer> getknownMachine() {
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

    public void addObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IComputer getMe() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addMachine(IComputer computer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void notifyByComputer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param computer the value of computer
     * @return the String
     */
    public String addSlaveMachine(IComputer computer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connectTo(String addr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  
 
}

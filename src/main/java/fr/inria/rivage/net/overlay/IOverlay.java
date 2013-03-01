/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.overlay;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.net.group.Message;
import fr.inria.rivage.net.overlay.tcp.Computer;
import java.util.List;
import java.util.Observer;

/**
 * @author Yves
 */
public interface IOverlay {
/*
 * discovery IComputer to entame communication
 */
   
    public void discovery();

    public void connectToMachine(IComputer m);
    
    public List<? extends IComputer> getknownMachine();
    public List<? extends IComputer> getConnectedMachine();
    
   // public InputQueue getInputQueue();
    
    void askDocument(ID id);
    
    void sendToAll(Message mess);
    
    void informNewFile(FileController fc);
   // public OutputQueue getOutputQueue();
    /**
     * Start listening.
     */
    public void start();

    /**
     * Stop listening.
     */
    public void stop();

    /**
     * Is currently listening.
     */
    public boolean isRunning();


    public void changeName(String name);
    
    
    public void addObserver(Observer o);
    public void deleteObserver(Observer o);

    public IComputer getMe();

    public void addMachine(IComputer computer);
    /**
     *
     * @param computer the value of computer
     * @return the String
     */
    public String addSlaveMachine(IComputer computer);// on same computer
    public void notifyByComputer();
    public void connectTo(String addr) throws Exception;
}

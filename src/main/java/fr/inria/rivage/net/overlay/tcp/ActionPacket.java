/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.rivage.net.overlay.tcp;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.net.overlay.IComputer;
import fr.inria.rivage.net.overlay.IOverlay;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class ActionPacket implements Serializable {
    public static final Logger logger=Logger.getLogger(Class.class.getName());
    public static enum Action {
        
        GetKnowMachineList, GetDocument, File, GetFileList, MyNameIs,Pong,SubscribeSlaveList,TakeThisName
    };
    ID id;
    String name;
    Action action;
    
    public void doAction(Computer origine, IOverlay tcp) throws IOException {
        logger.log(Level.INFO, "doAction{0}", this);
        FileController fc;
        switch (action) {
            case GetKnowMachineList:
                for (IComputer comp : tcp.getknownMachine()) {
                    origine.sendObject(comp);
                }
                break;
            case GetDocument:
                fc = Application.getApplication().getFileManagerController().getFileControlerById(id);
                if (fc != null && fc.getConcurrencyController()!=null ) {
                    synchronized (fc.getConcurrencyController()) {
                        origine.sendObject(fc.getConcurrencyController().getSyncInfo());
                    }
                }else{
                     logger.log(Level.WARNING, "Asked File Not Found {0}", id);
                }
                break;
            case File:
                fc = new FileController(this.id, this.name);
                Application.getApplication().getFileManagerController().registerRemoteNewFile(fc);
                break;
            
            case GetFileList:
                origine.sendFileList();
                break;
            case SubscribeSlaveList:
                throw new UnsupportedOperationException("not yet");
                //break;
                
            case TakeThisName:
                tcp.getMe().setName(this.getName());
                logger.log(Level.INFO, "new name {0}", this.getName());
                break;
            case MyNameIs:
                if (name==null){
                    
                }
                origine.setName(name);
                logger.log(Level.INFO, "{0} is know as {1}", new Object[]{origine.getUri().toString(), this.getName()});
        }
    }
    
    public ActionPacket(Action action) {
        this.action = action;
    }
    
    public ActionPacket(Action action, ID id) {
        this(action);
        this.id = id;
    }
    
    public ActionPacket() {
    }
    
    public ID getId() {
        return id;
    }
    
    public void setId(ID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Action getAction() {
        return action;
    }
    
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     *
     * @param action the value of action
     * @param id the value of id
     * @param name the value of name
     */
    public ActionPacket(Action action, ID id, String name) {
        this.id = id;
        this.name = name;
        this.action = action;
    }
    
    @Override
    public String toString() {
        return "ActionPacket{" + "id=" + id + ", name=" + name + ", action=" + action + '}';
    }
}

package fr.inria.rivage.engine.manager;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.engine.concurrency.crdt.ConcurrencyControllerCRDT;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.net.group.FileControllerManager;
import java.awt.Dimension;
import org.apache.log4j.Logger;

public class FileController {

    //private  String fileID;
    private Logger log;
    private InnerWindow innerWindow;
    private GDocument document;
    private IConcurrencyController concurrencyController;
    private FileControllerManager fileControleurManager;
    private String fileName;
    private ID id;
    //private TreeManager treeManager;

    public FileControllerManager getFileControleurManager() {
        return fileControleurManager;
    }

    public FileController(String fileName/*,FileControllerManager fcm*/) {
        concurrencyController = new ConcurrencyControllerCRDT(this);
        this.log = Logger.getLogger(FileController.class);
        this.document = new GDocument(this, this.concurrencyController.getNextID(), fileName);
        //this.document.getParameters().setObject(Parameters.ParameterType.Text, fileName);/*+"."+UUID.randomUUID().toString();*/
        // this.groupController = new GroupController2(this);
        Page page = new Page(document, this.concurrencyController.getNextID(), "Page 1", new Dimension(2000, 2000));
        document.assignZPos(page, null);
        document.add(page);
        GLayer layer = new GLayer(page, this.concurrencyController.getNextID(), "Layer 1");
        document.assignZPos(layer, null);
        document.add(layer);
        this.innerWindow = new InnerWindow(document.getParameters().getText(), this);
        innerWindow.setDocument(document);
        this.fileControleurManager = Application.getApplication().getFileManagerController();
        this.id = document.getId();
        this.fileName = fileName;
        this.fileControleurManager.registerNewFile(this);
    }

    public FileController(ID id, String fileName) {
        this.fileControleurManager = Application.getApplication().getFileManagerController();
        this.id = id;
        this.fileName = fileName;
    }

    public void askDocuement() {
        if (this.document == null || concurrencyController==null) {
            concurrencyController = new ConcurrencyControllerCRDT(this);
            Application.getApplication().getFileManagerController().askDocument(this);
        } else {
            this.innerWindow = new InnerWindow(document.getParameters().getText(), this);
            innerWindow.setDocument(document);
        }
        
    }

    public void setDocument(GDocument doc) {
        this.document = doc;
        doc.setFileController(this);
        this.innerWindow = new InnerWindow(document.getParameters().getText(), this);
        innerWindow.setDocument(document);

    }

    public ID getId() {
        return id;
    }

    /**
     * Theoretically no need to reload, but perhaps someone needs to know if he
     * can now download the file or not, so send a msg that file is uploaded and
     * wait for ACKs?
     *
     * }
     */

    /*public void newFile(String fileName) {
        
        

     }

     public void loadFile(GDocument gdoc) {

     //        log.debug("The file is going to be loaded.");
     /**
     * Join Group. Stop and synchronize, tell others to save.
     */
    /*FileControllerInitMonitor fcim = FileControllerInitMonitor
     .getNewMonitor(fileID);
     fcim.setVisible(true);*/
    //try {
			/*groupController.join();
     groupController.start();
     fcim.setText("Waiting for group members to reply.");
     log.debug("Waiting for group to be joined.");
     groupController.groupJoined();
     log.debug("Group joined.");
     } catch (InterruptedException e1) {
     log.error("The GroupController2 was interrupted while joining.", e1);
     }*/
    /**
     * Group has been joined. Load the file and continue initialization.
     */
    //fcim.setText("Loading file from server.");
    //reloadFile();
    /**
     * Here, we assume that the file has already bee loaded and that GraphicTree
     * has been set right.
     */
    //document.add(new Page(document, this.concurrencyController.getNextID(), "Page 1", new Dimension(2000, 2000)));
       /* this.innerWindow = new InnerWindow(document.getParameters().getText(), this);
     innerWindow.setDocument(document);
     //this.concurrencyController.startNew();

     //concurrencyController.startNew();
     //fcim.dispose();
     Application.getApplication().setCurrentFileController(this);
     // Application.getApplication().addOpenedFile(this);*
     }*/
    public void doClose() {
        System.out.println("closing FileController...");
        innerWindow.close();
        Application app = Application.getApplication();
        concurrencyController.halt();
       // this.concurrencyController = null;
        if (app.getCurrentFileController() == this) {
            app.setCurrentFileController(null);
        }
        //app.removeOpenedFile(this);
        //this.document = null;
        System.out.println("FileController closed!");
    }

    public void doPrint() {
    }

    public void doAndSendOperation(Operation op) {
        concurrencyController.doAndSendOperation(op);

    }
    public void doRedo(){
        
    }
    public void doUndo(){
        
    }
    
    /*public void doRedo(boolean isGlobal) {
        if (isGlobal) {
            concurrencyController.redoLastGlobalOp();
        } else {
            concurrencyController.redoLastLocalOp();
        }

    }

    public void doUndo(boolean isGlobal) {
        if (isGlobal) {
            concurrencyController.undoLastGlobalOp();
        } else {
            concurrencyController.undoLastLocalOp();
        }
    }*/

    public WorkArea getCurrentWorkArea() {
        if (innerWindow == null) {
            return null;
        }
        return innerWindow.getSelectedWorkArea();
    }

    public GDocument getDocument() {
        return document;
    }

    public InnerWindow getInnerWindow() {
        return innerWindow;
    }

    public String getFileName() {
        return document == null ? this.fileName : document.getParameters().getText();
    }

    /*public ID getFileID() {
     return this.document.getParentId();
     }*/
    public void disableWorkAreas() {
        if (innerWindow == null) {
            return;
        }
        for (WorkArea wa : innerWindow.getWorkAreas()) {
            wa.setEnabled(false);
        }
    }

    public void enableWorkAreas() {
        if (innerWindow == null) {
            return;
        }
        for (WorkArea wa : innerWindow.getWorkAreas()) {
            wa.setEnabled(true);
        }
    }

    public IConcurrencyController getConcurrencyController() {
        return concurrencyController;
    }

    public void setConcurrencyController(
            IConcurrencyController concurrencyController) {
        if (this.concurrencyController == null) {
            this.concurrencyController = concurrencyController;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final FileController other = (FileController) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getFileName();
    }
}
/**
 * reloadFile assumes that the group has already been joined and that we only
 * have to download the file and update the tree.
 */
//public void reloadFile() {
        /*String content = "";
 try {
 content = Application.getApplication().getFileManagerController()
 .loadFileAsString(ServerNetwork.DOCUMENT_FILES, fileID);
 } catch (IOException e) {
 log.error("An IOException occured while loading file from server.",
 e);
 } catch (ClassNotFoundException e) {
 log.error(
 "There is some version mismatch between server and client.",
 e);
 }
		
 try {
 document = XMLDecoder.getDocument(
 XMLDecoder.getXMLElement(content));
			
 if (innerWindow != null) {
 innerWindow.setDocument(document);
 }
 } catch (Exception ex) {
 log.error("Cannot load file.", ex);
			
 document = new GDocument();
 //document.addBlankPage("Page 1", document.generateNextId(), new Dimension(2000, 2000));
 //document.addBlankPage("Page 1", document.generateNextGlobalId(), new Dimension(2000, 2000));
			
 if (innerWindow != null) {
 innerWindow.setDocument(document);
 }
 }*/
//}
   /* public void saveFile() {
 log.debug("The file is going to be saved.");
 /**
 * Stop and synchronize
 */
/**
 * Upload to server.
 */
        /*
         Document doc = new Document(XMLEncoder.getXMLElement(document));

         String fileContent = new XMLOutputter(Format.getPrettyFormat())
         .outputString(doc);

         try {
         Application.getApplication().getFileManagerController().saveFile(
         ServerNetwork.DOCUMENT_FILES, fileID, fileContent);
         } catch (IOException e) {
         log.error("An IOException occured while saving file to server.", e);
         } catch (ClassNotFoundException e) {
         log.error(
         "There is some version mismatch between server and client.",
         e);
         }*/
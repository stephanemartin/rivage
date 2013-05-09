package fr.inria.rivage.engine.manager;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.crdt.ConcurrencyControllerCRDT;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.net.group.FileControllerManager;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileController {
    //private  String fileID;
    //private Logger log = Logger.getLogger(FileController.class);
    private static final Logger log = Logger.getLogger(FileController.class.getName());
    
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
        this.concurrencyController = new ConcurrencyControllerCRDT(this);
        this.id = this.concurrencyController.getNextID();
        this.fileName = fileName;
        this.fileControleurManager = Application.getApplication().getFileManagerController();
        this.fileControleurManager.registerNewFile(this);
        this.document = new GDocument(this, id, fileName);

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

        //this.id = document.getId();


    }

    public FileController(ID id, String fileName) {
        this.fileControleurManager = Application.getApplication().getFileManagerController();
        this.id = id;
        this.fileName = fileName;
    }

    public void askDocuement() {
        if (this.document == null || concurrencyController == null) {
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
       log.log(Level.INFO, "FileController {0} closed!", this.fileName);
    }

    public void doPrint() {
    }

    public void doAndSendOperation(Operation op) {
        concurrencyController.doAndSendOperation(op);

    }

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

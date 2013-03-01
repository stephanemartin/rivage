/*
 * Created on Jul 19, 2004
 */
package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.handlers.Handlers;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.PageChangeListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.apache.log4j.Logger;

/**
 * @author Yves
 * @author Tobias Kuhn
 */
public class InnerWindow extends JInternalFrame implements MouseListener,
        InternalFrameListener, MouseMotionListener, ChangeListener,
        PageChangeListener {

    private static ArrayList<CurrentWorkAreaListener> workAreaListeners = new ArrayList<CurrentWorkAreaListener>();
    private JTabbedPane tabbedPane;
    private ArrayList<WorkArea> workAreas = new ArrayList<WorkArea>();
    private GDocument document;
    private FileController fileController;
    private Logger log;

    public InnerWindow(String fileID, FileController fileController) {
        super(fileID, true, true, true);
        try {
            this.fileController = fileController;
            log = Logger.getLogger(InnerWindow.class);
            setSize(300, 300);
            setLocation(0, 0);
           
            addInternalFrameListener(this);
            Application.getApplication().getMainFrame().getDesktop().add(this);
             setSelected(true);
            setMaximum(true);
        } catch (PropertyVetoException ex) {
            java.util.logging.Logger.getLogger(InnerWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        Application.getApplication().getMainFrame().getDesktop().remove(this);
    }

    public GDocument getDocument() {
        return document;
    }

    public void setDocument(GDocument document) {
        this.document = document;
        getContentPane().removeAll();

        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        tabbedPane.setFocusable(false);
        tabbedPane.addChangeListener(this);
        workAreas.clear();
        for (GObject page : document){
            WorkArea wa = new WorkArea(this, (Page) page);
            workAreas.add(wa);
            tabbedPane.addTab(page.getParameters().getText(), new Tab(wa));
        }
        getContentPane().add(tabbedPane);

        //document.addPageListener(this);

        setVisible(true);
        refreshEnvironment();
        repaint();
    }

    public ArrayList<WorkArea> getWorkAreas() {
        return (ArrayList<WorkArea>) workAreas.clone();
    }

    public Page getSelectedPage() {
        return (Page) document.get(tabbedPane.getSelectedIndex());
    }

    public int getSelectedPageIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public WorkArea getSelectedWorkArea() {
        int index=0;
        if (tabbedPane != null) {
            index=tabbedPane.getSelectedIndex();
        }
        return workAreas.size()>index && index>=0?workAreas.get(index):null;
    }

    public FileController getFileController() {
        return fileController;
    }

    private void refreshEnvironment() {
        Application.getApplication().setCurrentFileController(fileController);
        getSelectedWorkArea().setMode(Handlers.SELECTION);
        fireCurrentWorkAreaChanged();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        workAreas.get(tabbedPane.getSelectedIndex()).dispatchEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        log.debug("Mouse Pressed.");
        workAreas.get(tabbedPane.getSelectedIndex()).dispatchEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        log.debug("Mouse Released.");
        workAreas.get(tabbedPane.getSelectedIndex()).dispatchEvent(e);
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        refreshEnvironment();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        refreshEnvironment();
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        fileController.doClose();
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        log.debug("Internal frame opened.");
        // Nothing to do here, we don't handle this.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        workAreas.get(tabbedPane.getSelectedIndex()).dispatchEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        workAreas.get(tabbedPane.getSelectedIndex()).dispatchEvent(e);
    }

    @Override
    public void pageChanged(Event mode, ID id, int index) {
        Page page;
        switch (mode) {
            case NEW_PAGE:
                page = (Page) document.get(index);
                WorkArea wa = new WorkArea(this, page);
                workAreas.add(index, wa);
                tabbedPane.insertTab(
                        page.getParameters().getText(),
                        null,
                        new Tab(wa),
                        null,
                        index);
                break;
            case PAGE_REMOVED:
                workAreas.remove(index);
                tabbedPane.remove(index);
                break;
            case PAGE_CHANGED:
                page = (Page) document.get(index);
                tabbedPane.setTitleAt(index, page.getParameters().getText());
                break;
        }
    }

    private static void fireCurrentWorkAreaChanged() {
        for (CurrentWorkAreaListener listener : workAreaListeners) {
            listener.currentWorkAreaChanged();
        }
    }

    public static void addWorkAreaListener(CurrentWorkAreaListener listener) {
        if (!workAreaListeners.contains(listener)) {
            workAreaListeners.add(listener);
        }
    }

    public static void removeWorkAreaListener(CurrentWorkAreaListener listener) {
        workAreaListeners.remove(listener);
    }
}

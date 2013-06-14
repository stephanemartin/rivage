package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.engine.operations.CreateOperation;
import fr.inria.rivage.gui.listener.ActiveLayerListener;
import fr.inria.rivage.gui.listener.LayerChangeListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import fr.inria.rivage.tools.Configuration;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class WorkArea extends JPanel implements Printable, SelectionChangeListener,
        TreeChangeListener, LayerChangeListener {

    private static enum UpdateStyle {

        DEFAULT, LIGHT, FULL;
    }
    private static ArrayList<ActiveLayerListener> activeLayerListeners = new ArrayList<ActiveLayerListener>();
    private GHandler handler;
    private ListenerProxy listenerProxy = new ListenerProxy();
    private Tab tab;
    private final InnerWindow window;
    private final Page page;
    private GLayer activeLayer;
    private SelectionManager selectionManager;
    private BufferedImage bufferedImage;
    private UpdateStyle updateMode = UpdateStyle.DEFAULT;
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    private Logger log = Logger.getLogger(getClass().getName());
    private RenderingHints renderHints;

    public WorkArea(InnerWindow window, Page page) {
        this.window = window;
        this.page = page;
        // this.activeLayer = page.getBackgroundLayer();
        this.selectionManager = new SelectionManager(this);

        setFocusable(true);

        properties.put("FrtColor", Color.BLACK);
        properties.put("BckColor", Color.WHITE);
        properties.put("Stroke", new SerBasicStroke((float) 1.0));

        bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        this.selectionManager.addSelectionListener(this);
        //window.getFileController().getTreeManager().addTreeListener(this);
        //page.addLayerListener(this);

        addMouseListener(listenerProxy);
        addMouseMotionListener(listenerProxy);
        addKeyListener(listenerProxy);

        renderHints =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public void setMode(GHandler handler) {
        if (this.handler != null) {
            this.handler.cleanUP();
        }
        this.handler = handler;
        if (this.handler != null) {
            handler.init(this);
            Application.getApplication().getMainFrame().getDrawToolBar().update();
        }
        lightRepaint();
    }

    public GHandler getMode() {
        return handler;
    }

    public GLayer getActiveLayer() {
        if (activeLayer == null) {
            activeLayer = this.getPage().first();
        }
        return activeLayer;
    }

    public void setActiveLayer(GLayer layer) {
        if (!page.contains(layer) || activeLayer == layer) {
            return;
        }
        activeLayer = layer;
        //layer.setVisible(true);
        fireActiveLayerChange();
    }

    /*public ArrayList<GObjectShape> getAccessibleObjects() {
     if (selectionManager.getOpenGroup() == null) {
     return page.getAllVisibleObjects();
     } else {
     return selectionManager.getOpenGroup().getChildren();
     }
     }*/
    /* public GObject getAccessibleObjectByPoint(Point2D p) {
     /* if (selectionManager.getOpenGroup() == null) {
     return page.getObjectByPoint(p, getObjectTolerance());
     } else {
     return selectionManager.getOpenGroup().getObjectByPoint(p, getObjectTolerance());
     }*
     return null;
     }*/
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (tab != null) {
            tab.refresh();
        }



        g2.setRenderingHints(renderHints);

        Dimension drawingSize = page.getDimension();

        // new image buffer if window is resized
        if (bufferedImage.getWidth() != getWidth()
                || bufferedImage.getHeight() != getHeight()) {
            bufferedImage = new BufferedImage(
                    getWidth(),
                    getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            updateMode = UpdateStyle.FULL;
        }

        // affine transform for including the scrollbars positions and the
        // current zoom
        AffineTransform af = AffineTransform.getTranslateInstance(
                -getScrollPosX(),
                -getScrollPosY());
        af.concatenate(AffineTransform.getScaleInstance(getZoom(), getZoom()));

        // paint white background and objects into the buffer (not for light
        // repaint)
        if (updateMode != UpdateStyle.LIGHT) {
            Graphics2D bg = (Graphics2D) bufferedImage.getGraphics();
            if (Configuration.getConfiguration().ANTIALIASING_ON) {
                bg.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            } else {
                bg.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            bg.transform(af);
            bg.setColor(Color.WHITE);
            bg.fillRect(0, 0, drawingSize.width, drawingSize.height);
            page.draw(bg);
        }


        // draw the buffer onto the graphics device
        g2.drawImage(bufferedImage, 0, 0, this);
        g2.transform(af);
        // super.paint(g);


        // draw the selection marks
        // System.out.println("warning: No Selection Mark is implemented");
        //    selectionManager.drawSelectionMarks(g2);

        // draw the gray background
        g2.setColor(Color.GRAY);
        double zoom = getZoom();
        g2.fillRect(drawingSize.width, 0, (int) (getWidth() / zoom) * 2,
                (int) (getHeight() / zoom) * 2);
        g2.fillRect(0, drawingSize.height, (int) (getWidth() / zoom) * 2,
                (int) (getHeight() / zoom) * 2);

        // draw the top layer
        page.draw(g2);
        // let the handlers draw onto the graphics device
        if (handler != null) {
            handler.draw(g2);
        }
        selectionManager.drawSelectionMarks(g2);
        updateMode = UpdateStyle.DEFAULT;
        super.paintComponents(g);
        //update(g);
    }

    /**
     * Repaints only the drawing of the handlers, not the objects
     */
    public void lightRepaint() {
        if (updateMode == UpdateStyle.DEFAULT) {
            updateMode = UpdateStyle.LIGHT;
        }
        super.repaint();
    }

    @Override
    public void repaint() {/*heu peut-être pas le bon nom pour un fullrepaint*/
        updateMode = UpdateStyle.FULL;
        super.repaint();
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
        //g.clearRect(0, 0, 2000,2000);

    }

    /**
     * Printable interface
     */
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        if (pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) graphics;
        Dimension drawingSize = page.getDimension();

        g2d.drawImage(bufferedImage, (int) pageFormat.getPaper()
                .getImageableX(), (int) pageFormat.getPaper().getImageableY(),
                drawingSize.width, drawingSize.height, this);
        g2d.drawRect((int) pageFormat.getPaper().getImageableX(),
                (int) pageFormat.getPaper().getImageableY(), (int) pageFormat
                .getPaper().getImageableWidth(), (int) pageFormat
                .getPaper().getImageableHeight());
        g2d.scale(((double) drawingSize.width / (pageFormat.getPaper()
                .getWidth() - pageFormat.getPaper().getImageableX())),
                ((double) drawingSize.height
                / pageFormat.getPaper().getHeight() - pageFormat
                .getPaper().getImageableY()));
        return Printable.PAGE_EXISTS;
    }

    public InnerWindow getWindow() {
        return window;
    }

    public Page getPage() {
        return page;
    }

    public FileController getFileController() {
        return window.getFileController();
    }

    public IConcurrencyController getConcurrencyController() {
        return this.getFileController().getConcurrencyController();
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public double getZoom() {
        if (tab != null) {
            return tab.getZoom();
        }
        return 1;
    }

    public void setZoom(double zoom) {
        if (tab != null) {
            tab.setZoom(zoom);
        }
    }

    public void zoom(double zoomFactor, Point2D centerPoint) {
        if (tab != null) {
            tab.zoom(zoomFactor, centerPoint);
        }
    }

    private double getScrollPosX() {
        if (tab != null) {
            return tab.getScrollPosX();
        }
        return 0;
    }

    private double getScrollPosY() {
        if (tab != null) {
            return tab.getScrollPosY();
        }
        return 0;
    }

    public PointDouble getDrawingPoint(Point2D screenPoint) {
        return new PointDouble((screenPoint.getX() + getScrollPosX())
                / getZoom(), (screenPoint.getY() + getScrollPosY()) / getZoom());
    }

    public PointDouble getScreenPoint(Point2D drawingPoint) {
        return new PointDouble(
                (drawingPoint.getX() * getZoom() - getScrollPosX()),
                (drawingPoint.getY() * getZoom() - getScrollPosY()));
    }

    public double getObjectTolerance() {
        return Configuration.getConfiguration().OBJECT_TOLERANCE / getZoom();
    }

    public double getPointTolerance() {
        return Configuration.getConfiguration().POINT_TOLERANCE / getZoom();
    }

    @Override
    public void selectionChanged() {
        lightRepaint();
    }

    @Override
    public void treeChanged() {
        repaint();
        try {//whish me : Use observer
            if (Application.getApplication().getCurrentFileController().getCurrentWorkArea() == this) {
                Application.getApplication().getMainFrame().getLayersToolBar().activeLayerChanged();
            }
        } catch (Exception ex) {
            log.severe(ex.getMessage());
        }
    }

    @Override
    public void layerChanged(Type type) {
        if (!page.contains(activeLayer)) {
            fireActiveLayerChange();
            return;
        }
        repaint();
    }

    public Color getCurrentBckColor() {
        return (Color) properties.get("BckColor");
    }

    public Color getCurrentFrtColor() {
        return (Color) properties.get("FrtColor");
    }

    public Stroke getCurrentStroke() {
        return (Stroke) Application.getApplication().getMainFrame().getStrokeBar().getStrock();//properties.get("Stroke");
    }

    /**
     *
     * @param frt the value of frt
     * @param bck the value of bck
     */
    public void updateColors(Color frt, Color bck) {
        if (frt != null) {
            properties.put("FrtColor", frt);
        }
        if (bck != null) {
            properties.put("BckColor", bck);
        }
    }

    public CreateOperation getCreateOperation(GObject obj) {
        return getCreateOperation(obj, getActiveLayer().getMax());
    }

    public void assignIDAndParent(GObject obj) {
        window.getFileController().getConcurrencyController().assignIDs(obj);
        if (obj.getParent() == null) {
            obj.setParent(getActiveLayer());
        }


    }

    public void assigneZPosAndID(GObject obj) {
        assignIDAndParent(obj);
        getFileController().getDocument().assignZPos(obj, getActiveLayer().getMax());

    }

    public CreateOperation getCreateOperation(GObject obj, GObject pos1, GObject pos2) {
        assignIDAndParent(obj);
        getFileController().getDocument().assignZPos(obj, pos1, pos2);
        return new CreateOperation(obj);
    }

    public CreateOperation getCreateOperation(GObject obj, Position pos) {
        assignIDAndParent(obj);
        getFileController().getDocument().assignZPos(obj, pos);
        return new CreateOperation(obj);
    }

    private static void fireActiveLayerChange() {
        for (ActiveLayerListener listener : activeLayerListeners) {
            listener.activeLayerChanged();
        }
    }

    public static void addActiveLayerListener(ActiveLayerListener listener) {
        if (!activeLayerListeners.contains(listener)) {
            activeLayerListeners.add(listener);
        }
    }

    public static void removeActiveLayerListener(ActiveLayerListener listener) {
        activeLayerListeners.remove(listener);
    }

    /**
     * Class that forwards all the events to the local field handler.
     *
     * @author Tobias Kuhn
     */
    private class ListenerProxy implements MouseListener, MouseMotionListener,
            KeyListener {

        public ListenerProxy() {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (handler != null) {
                handler.mouseDragged(e);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (handler != null) {
                handler.mouseMoved(e);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (handler != null) {
                handler.mouseClicked(e);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (handler != null) {
                handler.mouseEntered(e);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (handler != null) {
                handler.mouseExited(e);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (handler != null) {
                handler.mousePressed(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (handler != null) {
                handler.mouseReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (handler != null) {
                handler.keyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (handler != null) {
                handler.keyReleased(e);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (handler != null) {
                handler.keyTyped(e);
            }
        }
    }
}
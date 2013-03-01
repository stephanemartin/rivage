package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.Modifier.IModifier;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.actions.Actions;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;

public class GSelectionHandler extends GHandler implements
        SelectionChangeListener,
        TreeChangeListener {

    private WorkArea wa;
    private static final Logger log = Logger.getLogger(Class.class.getName());
    //private SelectionMark currentMark;
    // the first/last point when dragging the mouse. null otherwise.
    private Point2D firstPoint, lastPoint;
    private MouseEvent firstEvent;
    // true if shift key is currently pressed. false otherwise.
    private boolean shiftPressed;
    // the mouse sensitive object that the cursor is on. null if there is no one.
    // private IMouseSensitive sensObject;
    //private ArrayList<SelectedObject> selection = new ArrayList<SelectedObject>();
    //private List<GHandler> move = new LinkedList();
    private IModifier mod;
    private GHandler modHandler = null;
    private boolean ctrlPressed;

    GSelectionHandler() {
    }

    @Override
    public void init(WorkArea wa) {
        if (this.wa != null) {
            this.wa.getSelectionManager().removeSelectionListener(this);
            // this.wa.getWindow().getFileController().getTreeManager().removeTreeListener(this);
        }
        this.wa = wa;
        this.wa.getSelectionManager().addSelectionListener(this);
        //this.wa.getWindow().getFileController().getTreeManager().addTreeListener(this);

        //  currentMark = null;
        lastPoint = null;
        firstPoint = null;
        shiftPressed = false;
        ctrlPressed = false;
        //  sensObject = null;
        refreshSelection();
        updateCursor();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        log.info("Mouse pressed.");
        firstPoint = wa.getDrawingPoint(e.getPoint());
        firstEvent = e;
//        } else {
//            lastPoint = currentMark.getPosition();
//            firstPoint = lastPoint;
//        }
        wa.requestFocus();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        log.log(Level.INFO, "Mouse clicked.{0}", modHandler);
        Point2D p = wa.getDrawingPoint(e.getPoint());
        GObject sel = wa.getFileController().getDocument().getObjectByPoint(p, wa.getObjectTolerance());
        if (e.getClickCount() > 1) {

            if (sel != null) {
                wa.setMode(sel.getModifier());
                //wa.getSelectionManager().clearSelection();
            }
        } else {

            SelectionManager sm = wa.getSelectionManager();

            if (e.getButton() == MouseEvent.BUTTON1) {
                if (!shiftPressed && !ctrlPressed) {
                    sm.clearSelection();
                }

                System.out.println("Button 1" + shiftPressed + sel);

                sm.addSelObject(sel, ctrlPressed);
                // refreshSelection();
                //mod = sm.getMarks();
            } else if (mod != null) {
                GHandler gh = mod.getHandlerByPoint(firstPoint, wa.getObjectTolerance(), 0);
                if (gh != null) {
                    gh.mouseClicked(e);
                }
                // wa.repaint();
            }
        }
        wa.lightRepaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        Point2D p = wa.getDrawingPoint(e.getPoint());

        if (modHandler == null && lastPoint == null) {//first time
            /*SelectionManager sm = wa.getSelectionManager();
             mod = sm.getMarks();
             wa.getSelectionManager().getMarks();
             */
            //System.out.println("--------------- resel");
            if (mod != null) {
                modHandler = mod.getHandlerByPoint(firstPoint, wa.getObjectTolerance(), 0);
                System.out.println("++modHandler:" + modHandler + " gf " + firstPoint);
            }

            //GHandler ha = mod.getHandlerByPoint(wa.getDrawingPoint(e.getPoint()));

            if (modHandler != null) {
                modHandler.mousePressed(firstEvent);
                //this.move.add(ha);
                lastPoint = null;
            }
        }


        if (modHandler != null) {
            modHandler.mouseDragged(e);
            wa.repaint();
        } else {
            wa.lightRepaint();
            lastPoint = p;
        }



    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (modHandler != null) {
            modHandler.mouseReleased(e);
            modHandler = null;
            wa.treeChanged();
        } else {
            if (firstPoint != null && lastPoint != null) {
                // ArrayList<GObjectShape> objects = wa.getAccessibleObjects();
                Rectangle2D rect = getSelectionRectangle();
                if (rect.getWidth() * rect.getHeight() * wa.getZoom() * wa.getZoom() > 10) {
                    if (!shiftPressed && !ctrlPressed) {
                        wa.getSelectionManager().clearSelection();
                    }
                }
                SelectionManager sm = wa.getSelectionManager();
                sm.addAllSelObject(wa.getFileController().getDocument().getObjectsByRectangle(rect), ctrlPressed);
                refreshSelection();
                wa.lightRepaint();
            }
        }
        /*lastPoint = null;
         firstPoint = null;
         wa.repaint();*/
        //modHandler = null;
        lastPoint = null;
    }

   /* @Override
    public void mouseMoved(MouseEvent e) {
        /* Point2D p = wa.getDrawingPoint(e.getPoint());
         GObject o =  wa.getPage().getObjectByPoint(p, wa.getObjectTolerance());
         System.out.println("o selected :"+o);
         if (sensObject != null) {
         sensObject.mouseIn(false);
         sensObject = null;
         }
         if (o instanceof IMouseSensitive) {
         sensObject = (IMouseSensitive) o;
         sensObject.mouseIn(true);
         }
         currentMark = null;
         for (SelectedObject s : selection) {
         SelectionMark m = s.getSelMark(p, wa.getPointTolerance());
         if (m != null) {
         currentMark = m;
         }
         }
         updateCursor();
         wa.lightRepaint();*
    }*/

    @Override
    public void draw(Graphics2D g2) {
        // g2.setPaintMode();
        //this.wa.getSelectionManager().getOpenGroup();
        //GHandler Grp = wa.getSelectionManager().getOpenGroup().getModifier();
        if (mod != null) {
            mod.draw(g2);
        }


        if (firstPoint != null && lastPoint != null) {
            GraphicUtils.setSelectionColor(g2);
            g2.draw(getSelectionRectangle());
        }
        g2.setPaintMode();

//        if (move == null || move.isEmpty()) {
//            if (lastPoint != null) {
//                if (currentMark == null) {
//                    if (firstPoint != null && lastPoint != null) {
//                        GraphicUtils.setSelectionColor(g2);
//                        g2.draw(getSelectionRectangle());
//                    }
//                } else {
//                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, .25f));
//                    for (SelectedObject o : selection) {
//                        o.getCopy().draw(g2);
//                    }
//                }
//            } else {
//                for (SelectedObject o : selection) {
//                    o.drawSelectionMarks(g2);
//                }
//            }
//        }
        //g2.setPaintMode();
    }

    private Rectangle2D getSelectionRectangle() {
        return GraphicUtils.MakeRect(firstPoint, lastPoint);
    }

    /*@Override
     public void keyTyped(KeyEvent e) {
     if (e.getKeyCode()== KeyEvent.VK_DELETE)  {
     for (GObject go:wa.getSelectionManager().getSelObjects()) {
                
     }
     }
     }*/
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
            log.info("Multiselection enabled.");
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrlPressed = true;
            log.info("Multiselection2 enabled.");
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE){
            Actions.DELETE_OBJS.doAction();
            log.info("Delete pressed.");
        }
        if(mod != null){
            mod.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
            log.info("Multiselection disabled.");
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrlPressed = false;
            log.info("Multiselection2 disabled.");
        }
        if(mod != null){
            mod.keyReleased(e);
        }
    }

    private void updateCursor() {
        wa.setCursor(Cursors.normal);
        /* if (currentMark == null) {
         
         } else {
         wa.setCursor(currentMark.getCursor());
         }*/
    }

    private void refreshSelection() {
        mod = (IModifier) wa.getSelectionManager().getOpenGroup().getModifier();
        if (mod != null) {
            mod.init(wa);
        }
        //System.out.println("----------------" + mod);
        //selection.clear();
        //ArrayList<GObject> selObjs = wa.getSelectionManager().getSelObjects();
        /*  for (GObjectShape obj : wa.getAccessibleObjects()) {
         if (selObjs.contains(obj)) {
         try {
         selection.add(new SelectedObject(obj));
         } catch (CloneNotSupportedException ex) {
         java.util.logging.Logger.getLogger(GSelectionHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }*/
    }

    @Override
    public void selectionChanged() {
        //currentMark = null;
        lastPoint = null;
        firstPoint = null;
        shiftPressed = false;

        //sensObject = null;
        refreshSelection();
        updateCursor();
    }

    @Override
    public void treeChanged() {
        /*if (lastPoint == null) {
         for (SelectedObject o : selection) {
         try {
         o.refreshCopy();
         } catch (CloneNotSupportedException ex) {
         java.util.logging.Logger.getLogger(GSelectionHandler.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         wa.lightRepaint();
         }*/
    }
}

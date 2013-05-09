package fr.inria.rivage.engine.manager;

import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.listener.LayerChangeListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class manages the selection of objects for a WorkArea.
 *
 * @author Tobias Kuhn
 */
public class SelectionManager implements TreeChangeListener, LayerChangeListener {

    private ArrayList<SelectionChangeListener> selListeners =
            new ArrayList<SelectionChangeListener>();
    private static ArrayList<SelectionChangeListener> genSelListeners =
            new ArrayList<SelectionChangeListener>();
    private final WorkArea workArea;
    //private List<GObject> selObjects = new ArrayList<GObject>();
    //private List<GModifier> group = new LinkedList();
    GGroup group;

    /**
     * Creates a new SelectionManager for the given WorkArea.
     *
     * @param workArea the WorkArea
     */
    public SelectionManager(WorkArea workArea) {
        this.workArea = workArea;
        //group=new GGroup(workArea.getFileController().getDocument());
        createNewGroup();
        //workArea.getWindow().getFileController().getTreeManager().addTreeListener(this);
        //workArea.getPage().addLayerListener(this);
    }
    public final void createNewGroup(){
        group=new GGroup(/*workArea.getFileController().getConcurrencyController().getNextID()*/);
    }
    /**
     * Returns the currently opened group.
     *
     * @return the currently opened group or null if there is no one
     */
    public GGroup getOpenGroup() {
        return this.group;
    }

    /**
     * Sets the selected object list to contain only this object. All other
     * objects are removed.
     *
     * @param selObject the object to put in the list.
     */
    public void setSelObject(GObject selObject) {
        /*selObjects.clear();
        selObjects.add(selObject);*/
        group.clear();
        group.add(selObject);
        fireSelectionChange();
    }

    /**
     * Sets the selected object list to contain only this object. All other
     * objects are removed.
     *
     * @param selObject the objects to put in the list.
     */
    public void setSelObjects(Collection<GObject> objs) {
       group.clear();
        group.addAll(objs);
        fireSelectionChange();
    }

    public void addAllSelObject(List<GObject> selObjects, boolean exclusif) {
        for (GObject gobj : selObjects) {
            addSelObject(gobj, exclusif);
        }
    }

    /**
     * Adds an object to the list of selected objects. If
     * <code>addToList</code> is false, the list is cleared prior to insertion.
     *
     * @param selObject the object to put in the list.
     */
    public void addSelObject(GObject selObject, boolean exclusif) {
        if (selObject == null) {
            return;
        }
        if (group.contains(selObject)) {
            if (exclusif) {
                group.remove(selObject);
            }
            return;
        }
        group.add(selObject);
        //GModifier lm = selObject.getModifier(this.workArea);
        //if (lm != null) {
        //group.add(selObject);
        //}
        fireSelectionChange();
    }

    /**
     * Removes an object from the list of selected objects.
     *
     * @param selobject the object to remove from the list.
     */
    public void removeSelObject(GObject object) {
        if (!group.contains(object)) {
            return;
        }

       // group.clear();
        group.remove(object);
        fireSelectionChange();
    }

    /**
     * Clears the list of selected objects.
     */
    public void clearSelection() {
       // selObjects.clear();
        group.clear();
        fireSelectionChange();
    }

    /**
     * Returns the list with all the selected objects.
     *
     * @return a list containing all the selected objects.
     */
    public ArrayList<GObject> getSelObjects() {
        return (ArrayList<GObject>) new ArrayList(group.getCollection());
    }

    /**
     * Sets a group as the currently open group.
     *
     * @param newGroup the group to open.
     */
    public void openGroup(GGroup newGroup) {
         group = newGroup;
        //clearSelection();
    }

    /**
     * Draws the selection marks of the selection onto the graphical device.
     *
     * @param g2 the graphical device
     */
    public void drawSelectionMarks(Graphics2D g2) {
       
        /*for (GModifier p : this.group) {
         p.draw(g2);
         }*/
        /*	Configuration conf = Configuration.getConfiguration();
         GGroup g = openGroup;
         AffineTransform a = g2.getTransform();
         g2.setTransform(new AffineTransform());
         g2.setStroke(new BasicStroke(3));
         g2.setColor(conf.GROUP_COLOR_ACTIVE);
         while (g != null) {
         Rectangle2D b = g.getScreenBounds2D();
         Point2D p1 = a.transform(
         new PointDouble(b.getX(), b.getY()),
         null);
         Point2D p2 = a.transform(
         new PointDouble(b.getMaxX(), b.getMaxY()),
         null);
         g2.draw(new Rectangle2D.Double(
         p1.getX() - 4,
         p1.getY() - 4,
         p2.getX() - p1.getX() + 7,
         p2.getY() - p1.getY() + 7));
			
         if (g.getParent() instanceof GGroup) {
         g = (GGroup) g.getParent();
         } else {
         g = null;
         }
			
         g2.setColor(conf.GROUP_COLOR_INACTIVE);
         }
         g2.setStroke(new BasicStroke(0));
         g2.setTransform(a);*/
    }

    private void fireSelectionChange() {
        for (SelectionChangeListener listener : genSelListeners) {
            listener.selectionChanged();
        }
        for (SelectionChangeListener listener : selListeners) {
            listener.selectionChanged();
        }
    }

    /**
     * Adds a selection listener for this SelectionManager. It gets notified at
     * every change of the selection of this instance. This listeners we call
     * "specific selection listeners".
     *
     * @param listener the specific selection listener to add
     */
    public void addSelectionListener(SelectionChangeListener listener) {
        if (!selListeners.contains(listener)) {
            selListeners.add(listener);
        }
    }

    /**
     * Removes a specific selection listener.
     *
     * @param listener the specific selection listener to remove
     */
    public void removeSelectionListener(SelectionChangeListener listener) {
        selListeners.remove(listener);
    }

    /**
     * Adds a selection listener for all SelectionManager instances. It gets
     * notified at every change of the selection of any instance. This listeners
     * we call "general selection listeners".
     *
     * @param listener the general selection listener to add
     */
    public static void addGeneralSelectionListener(SelectionChangeListener listener) {
        if (!genSelListeners.contains(listener)) {
            genSelListeners.add(listener);
        }
    }

    /**
     * Removes a general selection listener.
     *
     * @param listener the general selection listener to remove
     */
    public static void removeGeneralSelectionListener(SelectionChangeListener listener) {
        genSelListeners.remove(listener);
    }

    @Override
    public void treeChanged() {
        GDocument doc = workArea.getWindow().getDocument();

        /*if (openGroup != null) {
         //try {
         doc.getObjectById(openGroup.getId());*/
        /*} catch (InvalidIdException ex) {
         openGroup(null);
         return;
         }*/
    }
    /*for (GObjectShape o : getSelObjects()) {
     if (o.getParent() == null || !o.getParent().getChildren().contains(o)) {
     removeSelObject(o);
     } else if (o.getParent() != openGroup && !(o.getParent() instanceof GRoot)) {
     removeSelObject(o);
     } else if (!isObjVisible(o)) {
     removeSelObject(o);
     }
     }*/
//}

    @Override
    public void layerChanged(Type type) {
        if (type == Type.VISIBILITY_CHANGED) {
            /*for (GObjectShape o : getSelObjects()) {
             if (!isObjVisible(o)) {
             removeSelObject(o);
             }
             }*/
        }
    }

    /*private boolean isObjVisible(GObjectShape obj) {
     /*if (obj instanceof GRoot) {
     return ((GRoot) obj).getLayer().isVisible();
     }
     return isObjVisible(obj.getParent());*
     return true;
     }*/
    /*public GHandler getMarks() {
        GHandler m = this.group.getModifier();
        if (m != null) {
            m.init(workArea);
        }
        return m;

    }*/

    public Parameters getSelParameters() {
        if (this.group.size() == 1) {
            return this.group.get(0).getParameters();
        } else if (this.group.size() > 1) {
            return this.group.getParameters();
        }
        return null;
    }
}

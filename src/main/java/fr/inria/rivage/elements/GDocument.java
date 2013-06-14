package fr.inria.rivage.elements;

import fr.inria.rivage.elements.renderer.GRenderersFeuille;
import fr.inria.rivage.elements.renderer.Renderer;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.tree.IllegalTreeOpException;
import fr.inria.rivage.engine.tree.InvalidIdException;
import fr.inria.rivage.gui.WorkArea;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;

/**
 * This class represents a GEditor-document which may contain several pages.
 *
 * @author Tobias Kuhn
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GDocument extends GObjectContainer<GObject> {

    private HashMap<ID, ColObject> objectHash = new HashMap<ID, ColObject>();
    // a list of the pages in the order they appear in the tabs
    // private ArrayList<Page> pagesOrder = new ArrayList<Page>();
    // a hashmap of the pages with their IDs as keys
    //private HashMap<Long, Page> pagesHash = new HashMap<Long, Page>();
    // list of all page listeners
    //private ArrayList<PageChangeListener> pageListeners = new ArrayList<PageChangeListener>();
    // hashmap of all tree objects of all pages. The key is the ID of the objects.
    transient FileController fileController;

    /**
     * Creates a new empty document.
     */
    public GDocument(FileController fileController, ID id, String name) {
        super(id);
        this.fileController = fileController;
        this.parameters = new Parameters(this, id);
        this.getParameters().setObject(ParameterType.Text, name);

    }

    public List<GObject> getObjects() {
        return new ArrayList(objectHash.values());
    }

    public FileController getFileController() {
        return fileController;
    }

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }

    /**
     * Searches a graphic object in the object-hash.
     *
     * @param id the id of the object to search for
     * @return an object if one is found
     * @throws InvalidIdException if no object is found
     */
    public ColObject getObjectById(ID id) {
        if (id.equals(this.getId())) {
            return this;
        }
        return objectHash.get(id);
    }

    /**
     * Registers the object. It is stored together with its ID in the object
     * hash of this document.
     *
     * @param obj the tree object to register
     * @throws IllegalTreeOpException if the ID is already in use
     */
    @Override
    public void add(GObject obj) {
        addObject((ColObject) obj);
    }

    public void add(ColObject obj) {
        addObject( obj);
    }
    public void addObject(ColObject obj) {

        objectHash.put(obj.getId(), obj);
        //System.out.println("Student method "+obj );
        for (ID id : obj.getParentId()) {
//            System.out.println("parent " + id);
            ColObject objParent = this.getObjectById(id);
            if(objParent==null){
                continue;
            }
            if (obj instanceof Renderer) {
                /** little hack */
                ((GRenderersFeuille)((GObject) objParent).getgRendreres()).addObject((Renderer) obj);
                
            } else {
                if (objParent == this) {

                    super.add((GObject) obj);
                } else {
                    ((GObjectContainer) objParent).add((GObject) obj);
                    assert (((GObjectContainer) objParent).contains((GObject) obj));
//                    System.out.println("added" + objParent);
                }
            }
        }
    }

    @Override
    public void remove(ID id) {
        delObject(objectHash.get(id));
    }

    @Override
    public void remove(GObject obj) {
        delObject((ColObject) obj);
    }

    public void delObject(ColObject obj) {
        for (ID id : obj.getParentId()) {
            ColObject parentToD = this.getObjectById(id);
            if (obj instanceof Renderer) {
                /** little hack */
                ((GRenderersFeuille)((GObject) parentToD).getgRendreres()).delObject((Renderer) obj);
            } else {
                if (parentToD == this) {
                    super.remove((GObject) obj);
                } else {
                    ((GObjectContainer) parentToD).remove((GObject) obj);
                }
            }
        }
        this.objectHash.remove(obj.getId());
    }

    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void assignZPos(GObject obj, Position pos) {
        if (obj.getParameters().getObject(Parameters.ParameterType.Zpos) == null) {

            if (pos == null) {
                pos = this.getFileController().getConcurrencyController().getFirstPosition(obj.getId());
            } else {
                pos = pos.genNext(obj.getId());
            }
            obj.getParameters().setObject(Parameters.ParameterType.Zpos, pos);
        }

    }

    public void assignZPos(GObject obj, GObject pos1, GObject pos2) {
        Position p1 = pos1.getParameters().getPosition(Parameters.ParameterType.Zpos);
        Position p2 = pos2 == null ? null : pos2.getParameters().getPosition(Parameters.ParameterType.Zpos);
        obj.getParameters().setObject(Parameters.ParameterType.Zpos, p1.genBetween(p2, obj.getId()));
    }

    ///Wish me :put in good place
    public void zPosUp(GObjectContainer father, GObject obj) {
        Position begin;
        Position end = null;

        GObject go = father.higher(obj);
        if (go != null) {
            begin = go.getParameters().getPosition(ParameterType.Zpos);
            go = father.higher(go);
            if (go != null) {
                end = go.getParameters().getPosition(ParameterType.Zpos);
            }
            setNewbetween(begin, end, obj);
        }

        /* Iterator<GObject> it = father.iterator();
         while (it.hasNext()) {
         if (it.next().equals(obj)) {
         if (it.hasNext()) {
         begin = it.next().getParameters().getPosition(ParameterType.Zpos);
         if (it.hasNext()) {
         end = it.next().getParameters().getPosition(ParameterType.Zpos);
         }
         //Put code here
         setNewbetween(begin, end, obj);
         }
         }
         }*/
    }

    public void zPosDown(GObjectContainer father, GObject obj) {
        Position begin = null;
        Position end = null;
        GObject go = father.lower(obj);
        if (go != null) {
            end = go.getParameters().getPosition(ParameterType.Zpos);
            go = father.lower(go);
            if (go != null) {
                begin = go.getParameters().getPosition(ParameterType.Zpos);
            }
            setNewbetween(begin, end, obj);
        }
    }

    public Position setNewbetween(Position p1, Position p2, GObject obj) {
        Position pos;

        ID id = this.fileController.getConcurrencyController().getNextID();
        if (p1 == null && p2 == null) {
            LOG.warning("Two position are null !");
            pos = this.fileController.getConcurrencyController().getFirstPosition(id);
        } else if (p1 == null) {
            pos = p2.genPrevious(id);
        } else if (p2 == null) {
            pos = p1.genNext(id);
        } else {
            pos = p1.genBetween(p2, id);
        }
        if (pos != null) {
            obj.deleteMeFromParent();
            obj.getParameters().setObject(Parameters.ParameterType.Zpos, pos);
            obj.getParameters().getParameter(Parameters.ParameterType.Zpos).sendMod(id);
            obj.addMeFromParent();
            //this.fileController.getCurrentWorkArea().treeChanged();
            //wa.treeChanged();

        }
        return pos;
    }
    /*public void createPage(String name){
        
     }
     public void createLayer(String name){
        
     }*/
    private static final Logger LOG = Logger.getLogger(GDocument.class.getName());
}

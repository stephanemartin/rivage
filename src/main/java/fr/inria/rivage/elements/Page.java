package fr.inria.rivage.elements;


import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPopupMenu;
/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class Page extends GObjectContainer<GLayer> {

    // the ID of this page
    //private long ID;
    // the document this page belongs to
    //private GDocument document;
    // a list of the layers in the correct order
    //private ArrayList<GLayer> layersOrder = new ArrayList<GLayer>();
    // a hashmap of the layers with their IDs as keys
    //private HashMap<Long, GLayer> layersHash = new HashMap<Long, GLayer>();
    // the size of the page
    //private Dimension dimension;
    // the name of the page (shown in the tab)
    //private String name;
    // the specific layer-change-listeners
    //private ArrayList<LayerChangeListener> layerListeners = new ArrayList<LayerChangeListener>();
    // the general layer-change-listeners
    //private static ArrayList<LayerChangeListener> genLayerListeners = new ArrayList<LayerChangeListener>();
    //private Logger log = Logger.getLogger(getClass());

    /**
     * Creates a new page.
     *
     * @param document the document the new page should belong to
     * @param id the ID of the new page
     * @param dimension the size of the new page
     * @param name the name of the new page
     * @param createBackgroundLayer true if a background-layer should get
     * created automatically
     */
    //public Page(GDocument document, ID id, Dimension dimension, String name,boolean createBackgroundLayer) {
        /*this.document = document;
        
        setDimension(dimension);
        setName(name);*/
        /*if (createBackgroundLayer) {
            add(new GLayer(this, , "Background"));
        }*/
    //}

    public Page(GDocument document, ID nextID, String page_1, Dimension dimension) {
        super(nextID, document);
        this.getParameters().setObject(Parameters.ParameterType.Text, page_1);
        this.getParameters().setObject(Parameters.ParameterType.Dimension, dimension);
        this.getParameters().setObject(Parameters.ParameterType.BgColor, Color.WHITE);
    }

    
    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        return null;
    }

  /* public double getWidth(){
       throw new UnsupportedOperationException("not yet");
   }
public double getHeight(){
       throw new UnsupportedOperationException("not yet");
   }*/
    public Dimension getDimension(){
       return (Dimension)this.getParameters().getObject(Parameters.ParameterType.Dimension);
   }
    
    /**
     * Returns the document this page belongs to.
     *
     * @return the document
     */
    


    /**
     * Returns the object at the given point. Only visible layers are
     * considered.
     *
     * @param p the point to look for an object
     * @param tolerance the tolerance around the point
     * @return the object at this point
     */
    
    /**
     * Returns the atomic object at the given point. Only visible layers are
     * considered. 'Atomic' means that no group is returned but its accordant
     * child.
     *
     * @param p the point to look for an object
     * @param tolerance the tolerance around the point
     * @return the atomic object at this point
     */
  /* public GObjectShape getAtomicObjectByPoint(Point2D p, double tolerance) {
        GObjectShape object = null;
        for (GLayer l : layersOrder) {
            if (!l.isVisible()) {
                continue;
            }
            GObjectShape o = l.getAtomicObjectByPoint(p, tolerance);
            if (o != null) {
                object = o;
            }
        }
        return object;
    }*/

    /**
     * Returns all objects of all layers. Also unvisible layers are considered.
     *
     * @return a list of all objects
     */
   /* public ArrayList<GObjectShape> getAllObjects() {
        ArrayList<GObjectShape> objs = new ArrayList<GObjectShape>();
        for (GLayer l : layersOrder) {
            objs.addAll(l.getRoot().getChildren());
        }
        return objs;
    }*/

    /**
     * Returns all objects of all visible layers.
     *
     * @return a list of all objects of all visible layers
     */
    /*public ArrayList<GObjectShape> getAllVisibleObjects() {
        ArrayList<GObjectShape> objs = new ArrayList<GObjectShape>();
        for (GLayer l : layersOrder) {
            if (l.isVisible()) {
                objs.addAll(l.getRoot().getChildren());
            }
        }
        return objs;
    }*/

    /**
     * Adds the layer at the end of the list.
     *
     * @param newLayer the layer to add
     */
   /*public void addLayer(GLayer newLayer) {
        addLayer(newLayer, layersOrder.size());
    }*/

    /**
     * Adds the layer at the given position.
     *
     * @param newLayer the layer to add
     * @param index the position
     */
 /*   public void addLayer(GLayer newLayer, int index) {
        if (index == 0 && layersOrder.size() > 0) {
            throw new IndexOutOfBoundsException("Cannot add layer at position 0.");
        }
        layersOrder.add(index, newLayer);
        layersHash.put(newLayer.getId(), newLayer);
        try {
            document.registerAll(newLayer.getRoot());
        } catch (IllegalTreeOpException ex) {
            log.error("Add Layer: cannot register objects.", ex);
        }
        fireLayerChange(LayerChangeListener.NEW_LAYER);
    }*/

    /**
     * Removes the layer with the given ID.
     *
     * @param id the ID of the layer to delete
     * @throws InvalidIdException if there is no layer with that ID or if it's
     * the background-layer
     */
   /* public void removeLayer(long id) throws InvalidIdException {
        GLayer layer = layersHash.get(id);
        if (layer == null) {
            throw new InvalidIdException("No layer with ID: " + id);
        }
        if (layersOrder.indexOf(layer) == 0) {
            throw new InvalidIdException("Cannot remove background layer.");
        }
        layersOrder.remove(layer);
        layersHash.remove(layer);
        try {
            document.unregisterAll(layer.getRoot());
        } catch (IllegalTreeOpException ex) {
            log.error("Remove Layer: cannot unregister objects.", ex);
        }
        fireLayerChange(LayerChangeListener.LAYER_REMOVED);
    }
    
    public void removeAllLayers() {
        unregisterAllLayers();        
        layersOrder.clear();
        layersHash.clear();        
    }*
    
    public void registerAllLayers() {
        for (GLayer l : layersOrder) {
            try {
                document.registerAll(l.getRoot());
            } catch (IllegalTreeOpException ex) {
                log.error("Remove Layer: cannot unregister objects.", ex);
            }
        }
    }
    
    public void unregisterAllLayers() {
        for (GLayer l : layersOrder) {
            try {
                document.unregisterAll(l.getRoot());
            } catch (IllegalTreeOpException ex) {
                log.error("Remove Layer: cannot unregister objects.", ex);
            }
        }
    }
*/
    /**
     * Moves a layer to another position in the list.
     *
     * @param id the ID of the layer to move
     * @param newIndex the new position of the layer
     * @throws InvalidIdException if there is no layer with that ID or if it's
     * the background-layer
     */
    /*public void moveLayer(long id, int newIndex) throws InvalidIdException {
        GLayer layer = layersHash.get(id);
        if (layer == null) {
            throw new InvalidIdException("No layer with ID: " + id);
        }
        if (layersOrder.indexOf(layer) == 0) {
            throw new InvalidIdException("Cannot move background layer.");
        }
        if (newIndex <= 0 || newIndex >= layersOrder.size()) {
            throw new IndexOutOfBoundsException();
        }
        layersOrder.remove(layer);
        layersOrder.add(newIndex, layer);
        fireLayerChange(LayerChangeListener.LAYER_MOVED);
    }*/

    /**
     * This method has to get called whenever a layer of this page has been
     * changed.
     *
     * @param type the type of the change (see LayerChangeListener)
     */
    /*public void fireLayerChange(int type) {
        for (LayerChangeListener listener : genLayerListeners) {
            listener.layerChanged(type);
        }
        for (LayerChangeListener listener : layerListeners) {
            listener.layerChanged(type);
        }
    }*/

    /**
     * Adds a layer listener for this page. It gets notified at every change of
     * the layers of this instance. This listeners we call "specific layer
     * listeners".
     *
     * @param listener the specific layer listener to add
     */
  /*  public void addLayerListener(LayerChangeListener listener) {
        if (!layerListeners.contains(listener)) {
            layerListeners.add(listener);
        }
    }*/

    /**
     * Removes a specific layer listener.
     *
     * @param listener the specific layer listener to remove
     */
/*    public void removeLayerListener(LayerChangeListener listener) {
        layerListeners.remove(listener);
    }*/

    /**
     * Adds a layer listener for all page-instances. It gets notified at every
     * change of the layers of any instance. This listeners we call "general
     * layer listeners".
     *
     * @param listener the general layer listener to add
     */
/*    public static void addGeneralLayerListener(LayerChangeListener listener) {
        if (!genLayerListeners.contains(listener)) {
            genLayerListeners.add(listener);
        }
    }/**/

    /**
     * Removes a general layer listener.
     *
     * @param listener the general layer listener to remove
     */
    /*public static void removeGeneralLayerListener(LayerChangeListener listener) {
        genLayerListeners.remove(listener);
    }*/
}

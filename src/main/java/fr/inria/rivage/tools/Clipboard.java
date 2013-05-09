package fr.inria.rivage.tools;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.gui.WorkArea;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Clipboard {

    LinkedList<GObject> content = new LinkedList<GObject>();

    private void makeObjectFromList(Collection<GObject> gobjs, ColObject father) throws CloneNotSupportedException {
        for (GObject p : gobjs) {
            
            GObject nObj=(GObject) p.clone();
            // TODO : New Parameter
            // TODO : new Filter
            
            nObj.setId(null);
            nObj.setParent(father);
            
            if (p instanceof GObjectContainer) {
                makeObjectFromList(((GObjectContainer)p).getCollection() ,p);
            }
        }

    }
    public boolean isEmpty() {
        return content.isEmpty();
    }

    public void past() {
        
            WorkArea wa = Application.getApplication()
                    .getCurrentFileController().getCurrentWorkArea();
            
        try {
            makeObjectFromList(content, wa.getActiveLayer());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Clipboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void copy() {
        ArrayList<GObject> objs = Application.getApplication()
                .getCurrentFileController().getCurrentWorkArea()
                .getSelectionManager().getSelObjects();
        content.clear();
        content.addAll(objs);
    }
}

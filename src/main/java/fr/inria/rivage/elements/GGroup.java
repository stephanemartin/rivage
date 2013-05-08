package fr.inria.rivage.elements;

import fr.inria.rivage.elements.Modifier.GAffineTransformer;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.renderer.GGroupRenderers;
import fr.inria.rivage.elements.renderer.GRenderer;
import fr.inria.rivage.engine.concurrency.tools.GroupParameters;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPopupMenu;

public class GGroup extends GObjectContainer<GObject> {

    public GGroup(/*ID id*/) {
        //super(id);
        this.parameters = new GroupParameters(this);
    }

    @Override
    public synchronized void add(GObject o) {
        super.add(o);
        o.getParameters().addObserver((GroupParameters)this.parameters);
        ((GroupParameters)this.parameters).update(null, null);
    }

    @Override
    public synchronized void remove(GObject o) {
        super.remove(o);
        o.getParameters().deleteObserver((GroupParameters)this.parameters);
        ((GroupParameters)this.parameters).update(null, null);
    }

    
    
    /*@Deprecated
    public GGroup() {
        throw new UnsupportedOperationException("Deprecated");
    }*/
    /*public GGroup(GDocument doc) {
     this.gRendreres = new GGroupRenderers(this);
     this.parameters=new Parameters(doc);
     }*/

    @Override
    public GObject getObjectByPoint(Point2D p, double tolerance) {
        System.out.println("----yeah");
        return this.getEuclidBounds().isInside(p, tolerance)? this : null;
        /*GObject g = super.getObjectByPoint(p, tolerance);
        System.out.println("g:" + g);
        return g != null ? this : null;*/
    }

    @Override
    public List<GObject> getObjectsByRectangle(Rectangle2D r) {
        System.out.println("----yeah2");
        LinkedList ret = new LinkedList();
        if (this.getEuclidBounds().isInside(r)) {
            ret.add(this);
        }
        return ret;
    }

    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        return null;
    }

    @Override
    public Parameters getParameters() {
        if (size() == 1) {
            return first().getParameters();
        } else if (contain.size() > 1) {
            return super.getParameters();
        } else {
            return null;
        }

    }

    @Override
    public GHandler getModifier() {
        this.parameters = new GroupParameters(this);
        if (contain.size() > 1) {
            return new GAffineTransformer(this);
        } else if (contain.size() == 1) {
            // return contain.first().getModifier();
            return new GAffineTransformer(first());
        } else {
            return null;
        }
    }

    @Override
    public GRenderer getgRendreres() {
        if (gRendreres == null) {
            gRendreres = new GGroupRenderers(this);
        }
        return gRendreres;
    }

    @Override
    public synchronized void draw(Graphics2D g2) {
        //super.draw(g2);
    }

    @Override
    public String toString() {
        return "GGroup{id:" + getId() + "contain+" + contain.size() + ": " + contain + '}';
    }

    /*public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/
}

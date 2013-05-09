/*
 * Created on Jan 17, 2005
 */
package fr.inria.rivage.elements;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPopupMenu;

/**
 * @author papadops
 */
public class GLayer extends GObjectContainer {

    private boolean visible = true;
    //private Page page;

    public GLayer(Page page, ID id, String name) {
        super(id, page);
        getParameters().setObject(Parameters.ParameterType.Text, name);

    }

    @Override
    public void draw(Graphics2D g2) {
        if (this.visible) {
            super.draw(g2);
        }
    }
    
    @Override
    public synchronized GObject getObjectByPoint(Point2D p, double tolerance) {
        if (this.visible) {
            return super.getObjectByPoint(p, tolerance);
        } else {
            return null;
        }
    }

    @Override
    public List getObjectsByRectangle(Rectangle2D r) {

        if (this.visible) {
            return super.getObjectsByRectangle(r);
        } else {
            return new LinkedList();
        }
    }

    public Page getPage() {
        return (Page) this.getParent()[0];
    }

    public boolean isVisible() {
        //return this.parameters.getBoolean(Parameters.ParameterType.Visible);

        return this.visible;
    }

    public void setVisible(boolean b) {
        this.visible = b;
    }

    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "GLayer " + this.getId() + " " + contain+" "+containID;
    }
}

/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package fr.inria.rivage.elements;

import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.renderer.GRenderers;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;


/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class GObject extends ColObject implements Serializable, Cloneable {
    public static final Logger logger=Logger.getLogger(Class.class.getName());
    /* private ID docID;
     transient GDocument doc;*/
    //protected Parameter<Position> position;
    //protected Parameters parameters;
    //transient Set<GGroup> inGroup;
    protected Parameters.ParameterBounds bounds;
    protected GRenderers gRendreres;

     
   
    public GRenderers getgRendreres() {
        if (gRendreres==null){
            gRendreres=new GRenderers(this);
        }
        return gRendreres;
    }
    public void setRenderers(GRenderers gRendreres){
        this.gRendreres=gRendreres;
    }

    public GObject() {
       // gRendreres=new GRenderers(this);
    }

    public GObject(ID id) {
        super(id);
        //gRendreres=new GRenderers(this);
    }

    public GObject(ID id, GObjectContainer ...parent) {
        super(id, parent);
        this.bounds = this.getParameters().getBounds();
        //gRendreres=new GRenderers(this);
    }

    public GObject(GObjectContainer ...parent) {
        super(parent);
        this.bounds = this.getParameters().getBounds();
        //gRendreres=new GRenderers(this);
    }

    /**
     * Draws the object on the graphic device. After the call, the
     * AffineTransform and paint mode of the graphic device have to be the same.
     *
     * @param g2 the graphic device.
     */
    public abstract void draw(Graphics2D g2);

    public abstract GObject getObjectByPoint(Point2D p, double tolerance);

    public abstract List<GObject> getObjectsByRectangle(Rectangle2D r);

    /*by default there is no popup menu  */
    public JPopupMenu getPopup(WorkArea wa) {
        return null;
    }

    /*by default no option is edfined */
    public void activateOption(WorkArea wa) {
    }

  

    public GHandler getModifier() {
        return null;
    }

    /**
     *
     * @return the fr.inria.geditor.elements.GBounds2D
     */
    public abstract GBounds2D getEuclidBounds();

    @Override
    public void setParent(ColObject... parent) {
        super.setParent(parent);
        this.getgRendreres().setParent(parent);
    }
    public abstract List<GObject> getRealObjects();
    
}

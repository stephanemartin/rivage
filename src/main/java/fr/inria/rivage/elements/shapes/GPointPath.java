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
package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import fr.inria.rivage.elements.GObjectContainer;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GPointPath extends GObject {

    boolean First;
    boolean Last;

    public GPointPath(GObjectContainer p, Point2D point) {
        super(p);
        parameters.setObject(Parameters.ParameterType.TopLeft, point);
        //this.getParameters().addType(new PointDouble(0, 0), Curve1, Curve2);
        this.parameters.acceptMod();
    }

    public GPointPath(GObjectContainer p, ID id, Point2D point) {
        super(id, p);
        parameters.setObject(Parameters.ParameterType.TopLeft, point);
        this.parameters.acceptMod();
    }

    @Override
    public void draw(Graphics2D g2) {
    }

    @Override
    public GObject getObjectByPoint(Point2D p, double tolerance) {
        if (this.getPoint().distance(p) <= tolerance) {
            return this;
        } else {
            return null;
        }

    }

    @Override
    public List<GObject> getObjectsByRectangle(Rectangle2D r) {
        LinkedList ret = new LinkedList();
        if (r.contains(this.getPoint())) {
            ret.add(this);
        }
        return ret;
    }
    public PointDouble getPoint(){
        return this.parameters.getPoint(TopLeft);
    }
    /**
     *
     * @return the fr.inria.geditor.elements.GBounds2D
     */
    @Override
    public GBounds2D getEuclidBounds() {
        Point2D p=this.getPoint();
        return new GBounds2D(p.getX(),p.getY(),0,0);
    }

    @Override
    public List<GObject> getRealObjects() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "GPointPath" +getPoint()+' ';
    }
    
}

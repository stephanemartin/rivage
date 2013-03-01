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

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GArea extends GObjectContainer<GObject> {
    public static enum Kind{Addition, Subtraction, Intersection, ExclusiveOr};
    @Override
    public synchronized void draw(Graphics2D g2) {
        super.draw(g2);
    }

    @Override
    public synchronized GObject getObjectByPoint(Point2D p, double tolerance) {
        return super.getObjectByPoint(p, tolerance);
    }

    @Override
    public List<GObject> getObjectsByRectangle(Rectangle2D r) {
        return super.getObjectsByRectangle(r);
    }

    @Override
    public List<GObject> getRealObjects() {
        return super.getRealObjects();
    }
    
    
}

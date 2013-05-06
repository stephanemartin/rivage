/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2013 LORIA / Inria / SCORE Team
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
package fr.inria.rivage.elements.renderer;

import fr.inria.rivage.elements.PointDouble;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class GRendreres2  implements GRenderer{
    AffineTransform af;
    
    public AffineTransform getTransform() {
        return af;
    }

    public Shape transform(Shape shape) {
        return af.createTransformedShape(shape);
    }

    public PointDouble transform(PointDouble p) {
        return (PointDouble)af.transform(p, new PointDouble());
    }

    public void addTransform(AffineTransform trasform) {
        
        
    }
    
}

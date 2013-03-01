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
package fr.inria.rivage.elements.renderer;

import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.ID;
import java.awt.Shape;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class Renderer extends ColObject {

    public Renderer(ID id) {
        super(id);
    }

    public Renderer(ID id, ColObject ...parent) {
        super(id, parent);
    }

    public Renderer(ColObject ... parent) {
        super(parent);
    }

    public abstract Shape transform(Shape shape);

    //public abstract Shape invertTransform(Shape shape);

    public abstract PointDouble transform(PointDouble p);

    //public abstract PointDouble invertTransform(PointDouble p);
}

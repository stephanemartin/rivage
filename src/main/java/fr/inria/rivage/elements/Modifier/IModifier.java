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
package fr.inria.rivage.elements.Modifier;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public interface IModifier {

    public void draw(Graphics2D g);

    /**
     *
     * @param p the value of p
     * @param tolerance the value of tolerance
     * @param button the value of button
     */
    public GHandler getHandlerByPoint(Point2D p, double tolerance, int button);

    public void keyPressed(KeyEvent e);

    public void keyReleased(KeyEvent e);

    public void mouseDragged(MouseEvent e);

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);
    
    public void init(WorkArea wa);
    
    public void setAffineTransform(AffineTransform p);
    
}

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
package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class GFreeHandConstructorFrisken extends GHandler {
    LinkedList<Segment> plist = null;
    WorkArea wa = null;
    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        wa.getSelectionManager().clearSelection();
        wa.setCursor(Cursors.freehand);
        plist = new LinkedList<Segment>();
        wa.getSelectionManager().clearSelection();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
    }
    
    

    final class Segment {

        PointDouble c[] = new PointDouble[4];

        Segment(double x, double y){
            initCurveSegment(x, y);
        }
        public void initCurveSegment(double x, double y) {
            for (int i = 0; i < c.length; i++) {
                c[i] = new PointDouble(x, y);
            }

        }

       void setSeg(int i,double x,double y){
           c[i].setLocation(x, y);
       }
       void setSeg(int i, PointDouble p){
           c[i]=p;
       }
       PointDouble getPoint(int i){
           return c[i];
       }
       void updateCurveSegment(double x,double y){
           
       }
    }
}

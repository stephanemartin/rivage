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

import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GGroupRenderers implements GRenderer {

    GGroup group;
    AffineTransform af;
    PointDouble center;

    public GGroupRenderers(GGroup group) {
        this.group = group;
    }

    public AffineTransform getTransform() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    public Shape transform(Shape shape) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    public PointDouble transform(PointDouble p) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    public AffineTransform getOverAf() {
        if (af == null) {
            af = new AffineTransform();
            publishAF();
        }
        return af;
    }

    private void publishAF() {
        for (GObject o : group) {
            o.getgRendreres().setOverAf(af);
            //System.out.println("==publish");
        }
    }

    public void setOverAf(AffineTransform af){
        this.af = af;
        publishAF();
       
    }

    public void validateOverAf(FileController fc, GObject obj) {
        for(GObject go:group){
            go.getgRendreres().validateOverAf(fc, go);
        }
        af.transform(center, center);
        af=null;
    }

    public PointDouble getCenter() {
       if(center==null){
           center= group.getEuclidBounds().getCenter();
       }
       if(af==null){
       return center;//.plus(group.getEuclidBounds().getCenter());
       }else{
           return(PointDouble) af.transform(center, new PointDouble());
       }
    }

    public void setCenter(PointDouble center) {
        this.center=center;//:.minus(group.getEuclidBounds().getCenter());
    }
}

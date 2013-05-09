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

import fr.inria.rivage.elements.ColContainer;
import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.AffineTransformeParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.CreateOperation;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Observer;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
// Can be improved with colObjectContainer but this action needs two extends in gdocuement :( 
public class GRenderersFeuille extends ColContainer<Renderer> implements GRenderer {

    AffineTransform afSup = new AffineTransform();
    boolean newest = false;
    AffineTransform af = null;
    PointDouble center;

    public GRenderersFeuille() {
    }

    public GRenderersFeuille(ColObject parent) {
        super(parent);
    }

    public void modified() {
        af = null;
    }

    private AffineTransform getAF() {
        if (af == null) {
            af = this.getTransform();
        }
        AffineTransform res = af;
        if (afSup != null) {
            res = new AffineTransform(afSup);
            res.concatenate(af);
        }
        return res;
    }

    @Override
    public Shape transform(Shape shape) {
        return getAF().createTransformedShape(shape);
    }

    @Override
    public PointDouble transform(PointDouble p) {
        return (PointDouble) getAF().transform(p, new PointDouble());
    }

    @Override
    public Position getNext(ID id) {
        return super.getNext(id);
    }

    @Override
    public AffineTransform getTransform() {
        AffineTransform af = new AffineTransform();
        for (Renderer at : this.contain) {
            if (at instanceof AffineTransformRenderer) {
                AffineTransform t = new AffineTransform(((AffineTransformRenderer) at).getAffineTransform());
                t.concatenate(af);
                af = t;
            }

        }
        return af;
    }
    /*public PointDouble invertTransform(PointDouble p) {
     for(Renderer r:contain){
     p=r.invertTransform(p);
     }
     return p;
     }*/

    public void validateOverAf(FileController fc, GObject obj) {
        AffineTransformRenderer at = (AffineTransformRenderer) last();
        if (center != null) {
            afSup.transform(center, center);
        }
        if (at != null && fc.getConcurrencyController().isOurID(at.getId())) {
            AffineTransformeParameter atp = new AffineTransformeParameter(at.getParameters());
            atp.concat(afSup);
            afSup = new AffineTransform();
            atp.sendMod();
        } else {
            IConcurrencyController cc = fc.getConcurrencyController();
            //IConcurrencyController cc = wa.getFileController().getConcurrencyController();
            ID id = cc.getNextID();
            Position pos = getNext(id);
            //Position pos = last().getParameters().getPosition(Parameters.ParameterType.Zpos).genNext(id);

            at = new AffineTransformRenderer(id, obj.getId(), afSup, this.getParent());

            at.getParameters().setObject(Parameters.ParameterType.Zpos, pos);
            at.setParent(this.getParent());
            cc.sendOperation(new CreateOperation(at));
            addObject(at);
            afSup = new AffineTransform();
            fc.getDocument().add(at);
        }
    }

    @Override
    public void setParent(ColObject... parent) {
        super.setParent(parent);
        for (Renderer r : contain) {
            r.setParent(parent);
        }
    }

    public AffineTransform getOverAf() {
        return afSup;
    }

    public PointDouble getCenter() {

        if (center == null) {
            center = ((GObject) this.getParent()[0]).getEuclidBounds().getCenter();
        }
        if (afSup == null) {
            return center;
        } else {
            return (PointDouble) afSup.transform(center, new PointDouble());
        }
    }

    public void setCenter(PointDouble center) {
        /*try {
         AffineTransform rev = getAF().createInverse();
         this.center = (PointDouble) rev.transform(center, new PointDouble());
         } catch (NoninvertibleTransformException ex) {
         Logger.getLogger(GRenderersFeuille.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        this.center = center;
    }

    @Override
    public synchronized void addObject(Renderer o) {
        super.addObject(o); //To change body of generated methods, choose Tools | Templates.
        modified();
        o.getParameters().addObserver((Observer) o);
    }

    @Override
    public synchronized void addAllObject(List<Renderer> l) {
        super.addAllObject(l); //To change body of generated methods, choose Tools | Templates.
        modified();
        for (Renderer o : l) {
            o.getParameters().addObserver((Observer) o);
        }
    }

    @Override
    public synchronized void delObject(Renderer o) {
        super.delObject(o); //To change body of generated methods, choose Tools | Templates.
        modified();
    }

    @Override
    public void delObject(ID o) {
        super.delObject(o); //To change body of generated methods, choose Tools | Templates.
        modified();
    }

    @Override
    public void clear() {
        super.clear(); //To change body of generated methods, choose Tools | Templates.
        modified();
    }

    public void setOverAf(AffineTransform af) {
        this.afSup = af;
    }
}

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

import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.Modifier.IModifier;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GEditFormModifier extends GHandler {

    GObject gobject;
    WorkArea wa;
    GHandler mod;
    IModifier imod;
    AffineTransform afine;
    AffineTransform afinv;

    public GEditFormModifier(GObject gobject, IModifier imod) {
        this.gobject = gobject;
        if (gobject instanceof GGroup) {
        } else {
            this.imod = imod;//gobject.getModifier();
            afine = gobject.getgRendreres().getGlobal();
            imod.setAffineTransform(afine);
            try {
                afinv = afine.createInverse();
                // int i = Configuration.getConfiguration().SEL_MARK_SIZE;
                //imod.setSizeSnapDraw(new PointDouble(afinv.getScaleX(), afinv.getScaleY()).mult(i, i));
            } catch (NoninvertibleTransformException ex) {
                Logger.getLogger(GEditFormModifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        imod.init(wa);

        // imod = gobject.getModifier();

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (mod != null) {
            mod.mouseDragged(e);
        }
        wa.repaint();
    }

    /*    @Override
     public void mouseMoved(MouseEvent e) {
     super.mouseMoved(e);
     }*/
    @Override
    public void mouseClicked(MouseEvent e) {
        // mod.mouseClicked(e);
        Point2D p = wa.getDrawingPoint(e.getPoint());
        mod = imod.getHandlerByPoint(p, wa.getObjectTolerance(), 0);
        if (mod == null) {
            if (e.getClickCount() > 1) {
                wa.setMode(Handlers.SELECTION);
            }
        } else {
            mod.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //MouseEvent er = adaptMouse(e);
        Point2D p = wa.getDrawingPoint(e.getPoint());
        mod = imod.getHandlerByPoint(p, wa.getObjectTolerance(),e.getButton());
        //System.out.println("mod: " + mod);
        if (mod != null) {
            mod.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (mod != null) {
            mod.mouseReleased(e);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        //g.setTransform(afine);
        imod.draw(g);
        // g.setTransform(new AffineTransform());
    }

    /* public MouseEvent adaptMouse(MouseEvent e) {
     //return e;
     Point2D p = e.getPoint();
     PointDouble pr = wa.getScreenPoint(afinv.transform(wa.getDrawingPoint(p), new PointDouble()));
     return new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), (int) pr.getX(), (int) pr.getY(), e.getClickCount(), false, e.getButton());

     }*/
    @Override
    public void keyPressed(KeyEvent e) {
        if (imod != null) {
            imod.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (imod != null) {
            imod.keyReleased(e);
        }
    }
//    @Override
//    public void keyTyped(KeyEvent e) {
//        if (imod != null) {
//            imod.keyTyped(e);
//        }
//    }
}

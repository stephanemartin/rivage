package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class GHandler implements MouseMotionListener, MouseListener, KeyListener {

    public abstract void init(WorkArea wa);
    AffineTransform af;
    AffineTransform afInv;

    public void setAffineTransform(AffineTransform af) {
        this.af = af;
        try {
            this.afInv = af.createInverse();
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(GHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PointDouble getPointFromMouseEvent(WorkArea wa, MouseEvent e) {
        if (af != null) {
            return wa.getScreenPoint(afInv.transform(wa.getDrawingPoint(e.getPoint()), new PointDouble()));
        } else {
            return new PointDouble(e.getPoint());
        }
    }

    public AffineTransform getAffineTransform(){
        return af;
    }
     public AffineTransform getAffineTransformInverse(){
        return afInv;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void draw(Graphics2D g) {
        if (af != null) {
            g.setTransform(af);
        }
    }

    public void cleanUP() {
    }
}
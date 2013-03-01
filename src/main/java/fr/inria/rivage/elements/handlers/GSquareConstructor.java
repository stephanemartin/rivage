package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public abstract class GSquareConstructor extends GHandler {

    boolean ratio = false;
    private WorkArea wa;
    private Point2D p1, p2;
    private Rectangle2D rec;

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        wa.getSelectionManager().clearSelection();
        wa.setCursor(Cursors.crosshair);
        p1 = null;
        p2 = null;
        rec = new Rectangle2D.Double();
    }

    @Override
    public void draw(Graphics2D g) {
        if (p2 != null) {
            g.setStroke(wa.getCurrentStroke());
            Shape shape = makeShape();
            g.setColor(wa.getCurrentBckColor());
            g.fill(shape);
            g.setColor(wa.getCurrentFrtColor());
            g.draw(shape);
        }
    }

    public Shape makeShape() {
        return rec;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        p1 = wa.getDrawingPoint(e.getPoint());
    }
    double makeAbsMax(double r1,double r2 ){
        return Math.abs(r1)> Math.abs(r2)?r1:r2;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        p2 = wa.getDrawingPoint(e.getPoint());
        if(ratio){
            PointDouble diff=new PointDouble(p2).minus(p1);
            double r=makeAbsMax(diff.getX(), diff.getY());
            p2=new PointDouble(r,r).plus(p1);
        }
        double leftupperx = (p1.getX() < p2.getX() ? p1.getX() : p2.getX());
        double leftuppery = (p1.getY() < p2.getY() ? p1.getY() : p2.getY());
        double rightlowerx = (p1.getX() > p2.getX() ? p1.getX() : p2.getX());
        double rightlowery = (p1.getY() > p2.getY() ? p1.getY() : p2.getY());
        rec.setRect(leftupperx, leftuppery, rightlowerx - leftupperx, rightlowery - leftuppery);
        wa.lightRepaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (p1 != null && p2 != null) {
            makeObject();
        }
    }

    public abstract void makeObject();

    public WorkArea getWa() {
        return wa;
    }

    public Point2D getP1() {
        return p1;
    }

    public Point2D getP2() {
        return p2;
    }

    public Rectangle2D getRec() {
        return rec;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        ratio = e.isControlDown();

    }

    @Override
    public void keyReleased(KeyEvent e) {

        ratio = e.isControlDown();

    }
}

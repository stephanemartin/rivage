package fr.inria.rivage.elements;

import fr.inria.rivage.elements.Modifier.GModifier;
import fr.inria.rivage.elements.handlers.GEditFormModifier;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.renderer.GRenderer;
import fr.inria.rivage.engine.concurrency.tools.AffineTransformeParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPopupMenu;

/**
 * Each new shape has to implement a default constructor.
 *
 * @author Yves
 * @author Tobias Kuhn
 * @author Stéphane Martin <stephane.martin@loria.fr>
 */
public abstract class GObjectShape extends GObject implements Serializable, Cloneable/*,IScreenBounds/*, ITreeElement */ {

    protected AffineTransform af = new AffineTransform();
    protected Shape shape;
    protected Shape transformedShape;


      public GObjectShape(ID id) {
     super(id);
     }

    public GObjectShape() {
    }
    

      
    public GObjectShape(ID id, GObjectContainer parent) {
        /*super(id, parent);
         bounds=new GBounds2DP(parameters);
         this.parameters.addZeroType(Parameters.ParameterType.Angular);*/
        this(parent);
        this.setId(id);
    }

    public GObjectShape(GObjectContainer parent) {
        super(parent);

        //this.parameters.addZeroType(Parameters.ParameterType.Angular);
    }

    public AffineTransform getAf() {
        return af;
    }

    public Shape getShape() {
        return shape;
    }

    //public abstract String[] getToSaveFieldList();
    public abstract Shape makeShape();

    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        return null;
    }

    // @Override
    public Rectangle2D getScreenBounds2D() {
        throw new UnsupportedOperationException();
    }

    public static Shape transformeShape(Parameters param, GRenderer gRendreres, Shape shape, Point2D center) {
        AffineTransform af = new AffineTransform();
        //System.out.println(""+param.getDouble(Parameters.ParameterType.Angular));
        af.rotate(param.getDouble(Parameters.ParameterType.Angular), center.getX(), center.getY());



        /* --- new renderer ---*/
        AffineTransformeParameter atp=new AffineTransformeParameter(param);
        if (atp.isReady()) {
            atp.loadAf();
            af.concatenate(atp.getAf());
        }
        /* --- end new renderer */

        shape = af.createTransformedShape(shape);
        if (gRendreres != null) {
            shape = gRendreres.transform(shape);
        }

        // af.rotate(param.getDouble(Parameters.ParameterType.RScale), shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY());
        return shape;
    }

    public static Shape draw(Graphics2D g2, Parameters param, GRenderer gRendreres, Shape shape, Point2D center) {
        shape = transformeShape(param, gRendreres, shape, center);


        Stroke oldStroke = g2.getStroke();
        Color oldColor = g2.getColor();
        /*TODO : Regarder setPaint*/
        
        Stroke newStroke = (Stroke) param.getObject(Parameters.ParameterType.Stroke);
        if (newStroke != null) {
            g2.setStroke(newStroke);
        }
        Color c = param.getColor(Parameters.ParameterType.BgColor);
        if (c != null) {
            
            g2.setColor(c);
            g2.fill(shape);
        }
        c = param.getColor(Parameters.ParameterType.FgColor);
        if (c != null) {
            g2.setColor(c);
            g2.draw(shape);
        }

        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
        return shape;
    }

    @Override
    public void draw(Graphics2D g2) {
        this.transformedShape = draw(g2, this.getParameters(), this.gRendreres, this.makeShape(), this.parameters.getBounds().getCenter());
    }

    /**
     * Draws the selection mark on the graphic device. After the call, the
     * AffineTransform and paint mode of the graphic device have be the same.
     *
     * @param g2 the graphic device.
     */
    //public abstract void drawSelectionMark(Graphics2D g2, GHandler mode);
    @Override
    public GObject getObjectByPoint(Point2D p, double tolerance) {
        //Rectangle2D rec=Rectangle2D.Double(p.getX()-tolerance,p.getY()-tolerance,tolerance*2,tolerance*2);

        /* shape = makeShape();
         shape = af.createTransformedShape(shape);*/
        return transformedShape.intersects(p.getX() - tolerance, p.getY() - tolerance, tolerance * 2, tolerance * 2) ? this : null;
    }

    @Override
    public List<GObject> getObjectsByRectangle(Rectangle2D r) {
        LinkedList ret = new LinkedList();
        /*Shape shape = makeShape();
         shape = af.createTransformedShape(shape);*/
        if (transformedShape.intersects(r)) {
            ret.add(this);
        }
        return ret;
    }

    @Override
    public GHandler getModifier() {
        return new GEditFormModifier(this, GModifier.generateBox(parameters));
    }

    /**
     *
     * @return the fr.inria.geditor.elements.GBounds2D
     */
    @Override
    public GBounds2D getEuclidBounds() {
        return new GBounds2D(transformeShape(parameters, this.gRendreres, this.makeShape(), this.parameters.getBounds().getCenter()));
    }

    @Override
    public List<GObject> getRealObjects() {
        List<GObject> ret = new LinkedList();
        ret.add(this);
        return ret;
    }
}

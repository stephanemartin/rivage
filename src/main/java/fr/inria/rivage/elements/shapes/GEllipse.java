package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GEllipse extends GObjectShape /*implements  ISnappable, 
 IStrokable, IHasTransform*/ {

    public GEllipse() {
    }

    public GEllipse(GObjectContainer parent, Rectangle2D rec, Color frtColor, Color bckColor, Stroke stroke) {
        super(parent);
        bounds.setRect(rec);
        this.parameters.setObject(FgColor, frtColor);
        this.parameters.setObject(BgColor, bckColor);
        this.parameters.setObject(Stroke, stroke);
        this.parameters.acceptMod();

    }
   
    /**
     * Draws an ellipse.
     *
     * @param g2 Graphics2D the graphic device to draw on.
     */
    @Override
    public Shape makeShape() {
        return new Ellipse2D.Double(bounds.getX(), bounds.getBounds().getY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public String toString() {
        return "GEllipse " + parameters;
    }
} 
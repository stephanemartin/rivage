package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.Modifier.GModifier;
import fr.inria.rivage.elements.Modifier.SpecialFeanturePoint;
import fr.inria.rivage.elements.handlers.GEditFormModifier;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class GRectangle extends GObjectShape {

    public GRectangle(GObjectContainer parent, Rectangle2D rec, Color frtColor, Color bckColor, Stroke stroke) {
        super(parent);
        this.getParameters().getBounds().setRect(rec);
        this.parameters.setObject(FgColor, frtColor);
        this.parameters.setObject(BgColor, bckColor);
        this.getParameters().addZeroType(Curve1);
        this.getParameters().setDouble(Curve2,rec.getHeight());
        this.parameters.setObject(Stroke, stroke);
        this.parameters.acceptMod();


    }

    public GRectangle() {
        /*this(new PointDouble(0, 0), 0, 0, null, null,
         new SerBasicStroke(0));*/
    }

    /**
     * @todo implement clone
     * @throws CloneNotSupportedException
     * @return Object
     */
   

    @Override
    public Shape makeShape() {
        Parameters.ParameterBounds bounds =this.getParameters().getBounds();
        return new RoundRectangle2D.Double(
                bounds.getX(),
                bounds.getY(),
                bounds.getWidth(),
                bounds.getHeight(),
                getParameters().getDouble(Curve1),
                getParameters().getDouble(Curve2));
        //return new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public GHandler getModifier() {
        
        GModifier mod = GModifier.generateBox(parameters);
        mod.addExtra(new SpecialFeanturePoint( parameters, Curve1, TopLeft,Dimension, SpecialFeanturePoint.Follows.X));
        mod.addExtra(new SpecialFeanturePoint( parameters, Curve2, TopLeft,Dimension, SpecialFeanturePoint.Follows.Y));
        return  new GEditFormModifier(this,mod);
    }
}
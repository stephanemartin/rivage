package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.Modifier.GModifier;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;

public class GLine extends GObjectShape /*implements IStrokable,
 ISnapper, IHasTransform */ {

    public GLine() {
    }

    //private AffineTransform af = new AffineTransform();
    //private GSnapPoint p1, p2;
    //private IGroup parent;
    //private SerBasicStroke stroke;
    public GLine(GObjectContainer parent,PointDouble p1,PointDouble p2, Color frtColor, Stroke stroke) {
        super(parent);
        /* this.p1 = new GSnapPoint(this, p1);
         this.p2 = new GSnapPoint(this, p2);*/
        //this.parameters.getBounds().setRect(rec);
        
        this.getParameters().setObject(Parameters.ParameterType.TopLeft,p1 );
         this.getParameters().setObject(Parameters.ParameterType.Dimension,p2.minus(p1));
        this.getParameters().setObject(Parameters.ParameterType.FgColor, frtColor);
        this.getParameters().setObject(Parameters.ParameterType.Stroke, stroke);
        //this.stroke = new SerBasicStroke(StrokeSize);
        this.parameters.acceptMod();
    }

    @Override
    public Shape makeShape() {
        return new Line2D.Double(this.parameters.getBounds().getP1(), this.parameters.getBounds().getP2());
    }

    @Override
    public GModifier getModifier() {
        return GModifier.getLine(parameters);
    }
}
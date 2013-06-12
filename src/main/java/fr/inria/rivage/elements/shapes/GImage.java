package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.ImageIcon;
import static fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType.*;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GImage extends GObjectShape {

    ImageIcon img;
    
    
    public GImage(GObjectContainer parent, PointDouble pos, ImageIcon img, Stroke stroke) {
        super(parent);
        this.img = img;
        Parameters.ParameterBounds bound=this.parameters.getBounds();
        bound.setP1(pos);
        bound.setDimension(img.getIconWidth(), img.getIconHeight());
        this.parameters.setObject(Stroke, stroke);
        this.shape=new Rectangle2D.Double(pos.x, pos.y,  img.getIconWidth(),img.getIconHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        AffineTransform af=new AffineTransform();
        AffineTransform aft=this.getgRendreres().getTransform();
        af.concatenate(aft);
        af.translate(this.getParameters().getBounds().getX(), this.getParameters().getBounds().getY());
        
        this.transformedShape= aft.createTransformedShape(shape);
        g2.drawImage(img.getImage(), af, img.getImageObserver());
        //g2.draw(transformedShape);
    }

    
    @Override
    public Shape makeShape() {
        return  shape;
    }
}

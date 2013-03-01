package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.shapes.GEllipse;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Handler that allows ellipse construction.
 *
 * @author yves
 *
 */
public class GEllipseConstructorHandler extends GSquareConstructor {

    @Override
    public Shape makeShape() {
        return new Ellipse2D.Double(getRec().getX(), getRec().getY(), getRec().getWidth(), getRec().getHeight());
    }

    @Override
    public void makeObject() {
        GEllipse gpl = new GEllipse(
                getWa().getActiveLayer(),
                getRec(),
                getWa().getCurrentFrtColor(),
                getWa().getCurrentBckColor(),
                getWa().getCurrentStroke());

        getWa().getFileController().doAndSendOperation(getWa().getCreateOperation(gpl));
    }
}

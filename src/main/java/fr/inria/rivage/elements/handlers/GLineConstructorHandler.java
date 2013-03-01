package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.shapes.GLine;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GLineConstructorHandler extends GSquareConstructor {

    @Override
    public Shape makeShape() {
        return new Line2D.Double(this.getP1(), this.getP2());
    }

    @Override
    public void makeObject() {
        GLine gpl = new GLine(
                getWa().getActiveLayer(),
                new PointDouble(this.getP1()), new PointDouble(this.getP2()),
                getWa().getCurrentFrtColor(),
                getWa().getCurrentStroke());

        getWa().getFileController().getConcurrencyController().doAndSendOperation(getWa().getCreateOperation(gpl));
    }
}

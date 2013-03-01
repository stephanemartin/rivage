package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.shapes.GRectangle;
/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GRectangleConstructorHandler extends GSquareConstructor {

    @Override
    public void makeObject() {
            GRectangle gpl = new GRectangle(
                    getWa().getActiveLayer(),
                    this.getRec(),
                    getWa().getCurrentFrtColor(),
                    getWa().getCurrentBckColor(),
                    getWa().getCurrentStroke());
            
            getWa().getFileController().getConcurrencyController().doAndSendOperation(
                    getWa().getCreateOperation(gpl));
    }
}

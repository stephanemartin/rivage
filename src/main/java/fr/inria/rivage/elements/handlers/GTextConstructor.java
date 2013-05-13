/*
 * Created on May 7, 2004
 */
package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.shapes.GText;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;



/**
 * @author Yves
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GTextConstructor extends GHandler {
  WorkArea wa;

    @Override
    public void mouseClicked(MouseEvent e) {
        PointDouble p =wa.getDrawingPoint(e.getPoint());
        GText text = new GText(wa.getActiveLayer(),
					p,
					wa.getCurrentFrtColor(),
                                        wa.getCurrentBckColor(),
					"Use property panel ->",
                                        new Font("Arial", Font.PLAIN, 20),
                                        wa.getCurrentStroke()
			);
        wa.getFileController().doAndSendOperation(wa.getCreateOperation(text));
        //wa.setMode(new GTextHandler(text,true));
    }
    @Override
    public void init(WorkArea wa) {
        this.wa=wa;
        wa.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
    }

    @Override
    public void cleanUP() {
        wa.setCursor(Cursor.getDefaultCursor());
    }

}

package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.tools.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class Paste extends AbstractAction {

    Paste() {
        this.putValue(NAME, "Paste");
        this.putValue(SHORT_DESCRIPTION, "Paste the objects from clipboard");
        this.putValue(MNEMONIC_KEY, KeyEvent.VK_V);
        this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    }

    @Override
    public boolean isEnabled() {
        if (Application.getApplication().getCurrentFileController() == null) {
            return false;
        }
        return !Application.getApplication().getClipboard().isEmpty();
    }

    
   
    @Override
    public void actionPerformed(ActionEvent e) {
        Application.getApplication().getClipboard().past();
        /*FileController fc = Application.getApplication().getCurrentFileController();
        WorkArea wa = fc.getCurrentWorkArea();
        


        throw new UnsupportedOperationException("Not yet");
        /*
         GObjectShape obj = Application.getApplication().getClipboard().getObject();
         FileController fc = Application.getApplication().getCurrentFileController();
         WorkArea wa = fc.getCurrentWorkArea();
         Point2D p = wa.getDrawingPoint(new PointDouble(0, 0));
         GBounds2D b = obj.getBounds2D();
		
         Transformer.translate(obj, p.getX() - b.x, p.getY() - b.y);
	
         fc.doOperation(
         wa.getCreateOperation(obj));

         wa.setMode(Handlers.SELECTION);*/
    }
}

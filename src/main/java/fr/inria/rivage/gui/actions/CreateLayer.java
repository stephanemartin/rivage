package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.CreateOperation;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class CreateLayer extends AbstractAction {

    CreateLayer() {
        this.putValue(AbstractAction.NAME, "Create Layer");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Create a new Layer");
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                Application.class.getResource("resources/images/addlayer.png")));
    }

    @Override
    public boolean isEnabled() {
        return Application.getApplication().getCurrentFileController() != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        FileController fc = Application.getApplication().getCurrentFileController();
        /*IConcurrencyController cc = fc.getConcurrencyController();
        ID newId = cc.getNextID();
        // The user specified a valid name for the new layer.

        Page page = fc.getCurrentWorkArea().getPage();
        GLayer layer = new GLayer(page, newId, "Layer " + page.size());
        fc.getDocument().assignZPos(layer, page.getMax());
        cc.doAndSendOperation(new CreateOperation(layer));*/


        /*SpecifyLayer specify = new SpecifyLayer();
         specify.setVisible(true);*/
        createLayer(fc.getCurrentWorkArea().getPage(), fc);
    }
    
    public static void createLayer(Page page,FileController fc){
        IConcurrencyController cc = fc.getConcurrencyController();
        ID newId = cc.getNextID();
        GLayer layer = new GLayer(page, newId, "Layer " + (page.size()+1));
        fc.getDocument().assignZPos(layer, page.getMax());
        cc.doAndSendOperation(new CreateOperation(layer));
    }
}

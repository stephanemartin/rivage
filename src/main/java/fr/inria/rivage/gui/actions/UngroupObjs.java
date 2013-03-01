package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.engine.operations.DeleteOperation;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class UngroupObjs extends AbstractAction {

    UngroupObjs() {
        this.putValue(AbstractAction.NAME, "Ungroup");
        this.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Ungroup selected Object");
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                Application.class.getResource("resources/images/ungroup.gif")));
        this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_U);
    }

    @Override
    public boolean isEnabled() {
        boolean ret;
        try {
            ArrayList<GObject> objs = Application.getApplication()
                    .getCurrentFileController().getCurrentWorkArea()
                    .getSelectionManager().getSelObjects();
            ret =  (objs.get(0) instanceof GGroup) && objs.size()==1;
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileController fc = Application.getApplication()
                .getCurrentFileController();
        SelectionManager sm = fc.getCurrentWorkArea().getSelectionManager();

        GGroup group =  (GGroup) sm.getSelObjects().get(0);
        sm.clearSelection();
        //List children = Arrays.asList(group.toArray());
        sm.setSelObjects(group.getCollection());
        IConcurrencyController cc = fc.getConcurrencyController();
        cc.doAndSendOperation(new DeleteOperation(group.getId()));


        fc.getCurrentWorkArea().repaint();
    }
}

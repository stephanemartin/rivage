package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.engine.operations.DeleteOperation;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class DeleteObjs extends AbstractAction {

    DeleteObjs() {
        this.putValue(AbstractAction.NAME, "Delete");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Delete selected Objects");
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/delete.gif")));
       // this.putValue(MNEMONIC_KEY, KeyEvent.VK_DELETE);
        //this.putValue(ACCELERATOR_KEY,  KeyEvent.VK_DELETE);
    }

    @Override
    public boolean isEnabled() {
        try {
            return Application.getApplication().getCurrentFileController()
                    .getCurrentWorkArea().getSelectionManager().getSelObjects().size() > 0;
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileController fc = Application.getApplication().getCurrentFileController();
        WorkArea wa = fc.getCurrentWorkArea();
        SelectionManager sm = wa.getSelectionManager();
        List<GObject> objs = (List) sm.getSelObjects().clone();
        for (GObject obj : objs) {
            fc.doAndSendOperation(new DeleteOperation(obj.getId()));
        }
        sm.clearSelection();
    }
}

package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.DeleteOperation;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class DeleteLayer extends AbstractAction {

    DeleteLayer() {
        this.putValue(AbstractAction.NAME, "Delete Layer");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Delete Layer");
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                Application.class
                .getResource("resources/images/dellayer.png")));
    }

    @Override
    public boolean isEnabled() {
        return Application.getApplication().getCurrentFileController() != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        FileController fc = Application.getApplication()
                .getCurrentFileController();
        WorkArea wa = fc.getCurrentWorkArea();
        Page page = wa.getPage();
        GLayer sel = fc.getCurrentWorkArea().getActiveLayer();
        if (sel != null) {
            int i = page.getIndex(sel);
            if (page != null && fc != null && i>=0) {

                IConcurrencyController cc = fc.getConcurrencyController();
                cc.doAndSendOperation(new DeleteOperation(sel.getId()));
                i = Math.max(0, i - 1);
                if (page.size() > 0) {
                    wa.setActiveLayer(page.get(i));
                }
                wa.treeChanged();
            }
        }
    }
}

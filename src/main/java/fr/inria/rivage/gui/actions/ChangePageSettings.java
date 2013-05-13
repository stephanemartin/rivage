package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.ModifyOperation;
import fr.inria.rivage.gui.dialog.PageSettingsDialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ChangePageSettings extends AbstractAction {

    ChangePageSettings() {
        this.putValue(AbstractAction.NAME, "Page Settings");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Change the settings of the current Page");
    }

    @Override
    public boolean isEnabled() {
        return Application.getApplication().getCurrentFileController() != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileController fc = Application.getApplication().getCurrentFileController();
        Page page = fc.getInnerWindow().getSelectedPage();

        PageSettingsDialog dialog = new PageSettingsDialog(
                "Page Settings",
                page.getParameters().getText(),
                page.getDimension());

        if (!dialog.ok()) {
            return;
        }

        fc.doAndSendOperation(
                new ModifyOperation(
                fc.getInnerWindow().getSelectedPage(),
                Parameters.ParameterType.Text,
                dialog.getName()));
        fc.doAndSendOperation(
                new ModifyOperation(
                fc.getInnerWindow().getSelectedPage(),
                Parameters.ParameterType.Dimension,
                dialog.getDimension()));
    }
}

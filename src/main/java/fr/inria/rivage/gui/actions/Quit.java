package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class Quit extends AbstractAction {

    Quit() {
        this.putValue(AbstractAction.NAME, "Quit");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Quit the application");
        this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
        FileController[] f = Application.getApplication().getFileControllers();
        for (int i = 0; i < f.length; i++) {
            f[i].doClose();
        }
        Application.getApplication().getMainFrame().dispose();
        System.exit(0);
    }
}

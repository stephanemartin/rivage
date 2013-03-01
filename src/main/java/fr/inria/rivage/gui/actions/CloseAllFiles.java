package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class CloseAllFiles extends AbstractAction {

	CloseAllFiles() {		
		this.putValue(AbstractAction.NAME, "Close All");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Close all open files");
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_A);
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		FileController[] f = Application.getApplication().getFileControllers();
		for (int i = 0; i < f.length; i++) f[i].doClose();
	}

}

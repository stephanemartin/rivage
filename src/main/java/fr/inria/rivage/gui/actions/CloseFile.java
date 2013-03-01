package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class CloseFile extends AbstractAction {

	CloseFile() {		
		this.putValue(AbstractAction.NAME, "Close");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Close this file");
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_L);
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		Application.getApplication().getCurrentFileController().doClose();
	}

}

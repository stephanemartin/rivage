package fr.inria.rivage.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class ShowConflicts extends AbstractAction {

	ShowConflicts() {		
		this.putValue(AbstractAction.NAME, "Conflicts");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Show the Conflicts");
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_F);
	}
	
	@Override
	public boolean isEnabled() {
		// TODO
		return true;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO
	}

}

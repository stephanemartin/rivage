package fr.inria.rivage.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class ShowSettings extends AbstractAction {

	ShowSettings() {		
		this.putValue(AbstractAction.NAME, "Settings");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Open Settings Dialog");
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_S);
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO
	}

}

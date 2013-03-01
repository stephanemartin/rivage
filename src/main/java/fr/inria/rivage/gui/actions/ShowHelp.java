package fr.inria.rivage.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class ShowHelp extends AbstractAction {

	ShowHelp() {		
		this.putValue(AbstractAction.NAME, "Help Topics");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Show the Help Topics");
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_F1);
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

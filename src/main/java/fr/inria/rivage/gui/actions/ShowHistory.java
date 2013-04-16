package fr.inria.rivage.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

public class ShowHistory extends AbstractAction {

	ShowHistory() {		
		this.putValue(AbstractAction.NAME, "History");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Show the History of this file");
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_H);
	}
	
	@Override
	public boolean isEnabled() {
		// TODO
		return false;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO
	}

}

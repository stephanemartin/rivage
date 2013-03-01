package fr.inria.rivage.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ShowStatusbar extends AbstractAction {

	ShowStatusbar() {		
		this.putValue(AbstractAction.NAME, "Statusbar");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Show the Statusbar");
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

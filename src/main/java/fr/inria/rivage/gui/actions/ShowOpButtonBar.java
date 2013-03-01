package fr.inria.rivage.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ShowOpButtonBar extends AbstractAction {

	ShowOpButtonBar() {		
		this.putValue(AbstractAction.NAME, "Operation Button Bar");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Show the Operation Button Bar");
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

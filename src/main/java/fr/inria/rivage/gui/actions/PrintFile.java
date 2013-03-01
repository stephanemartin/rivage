package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class PrintFile extends AbstractAction {

	PrintFile() {		
		this.putValue(AbstractAction.NAME, "Print");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Print this file");
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/print.gif")));
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_P);
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO
	}

}

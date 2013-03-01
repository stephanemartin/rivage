package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class SaveFile extends AbstractAction {

	SaveFile() {		
		this.putValue(AbstractAction.NAME, "Save");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Save this file");
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/save.gif")));
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_S);
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		//Application.getApplication().getCurrentFileController().saveFile();
	}

}

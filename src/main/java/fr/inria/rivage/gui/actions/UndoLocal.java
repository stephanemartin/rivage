package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class UndoLocal extends AbstractAction {

	UndoLocal() {
		this.putValue(AbstractAction.NAME, "Undo Local");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Do a local Undo Operation");
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/undo.gif")));
		this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_Z);
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		FileController fc = Application.getApplication().getCurrentFileController();
		//fc.doUndo(false);
	}

}

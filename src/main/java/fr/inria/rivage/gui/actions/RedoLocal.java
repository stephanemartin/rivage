package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class RedoLocal extends AbstractAction {

	RedoLocal() {
		this.putValue(AbstractAction.NAME, "Redo Local");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Do a local Redo Operation");
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/redo.gif")));
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		FileController fc = Application.getApplication().getCurrentFileController();
		//fc.doRedo(false);
	}

}

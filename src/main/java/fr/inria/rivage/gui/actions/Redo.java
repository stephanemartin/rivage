package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class Redo extends AbstractAction {

	Redo() {
		this.putValue(AbstractAction.NAME, "Redo Global");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Do a global Redo Operation");
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/redo.gif")));
	}
	
	@Override
	public boolean isEnabled() {
		return false && Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		FileController fc = Application.getApplication().getCurrentFileController();
		fc.getConcurrencyController().redoLastGlobalOp();
	}

}

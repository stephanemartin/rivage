package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class CopyPage extends AbstractAction {

	CopyPage() {
		this.putValue(AbstractAction.NAME, "Copy this Page");
		this.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Makes a new page which is a copy of the current page");
	}

	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		FileController fc = Application.getApplication()
				.getCurrentFileController();

		String name = JOptionPane.showInputDialog(Application.getApplication()
				.getMainFrame(), "Enter a name for the new page:", "Copy Page",
				JOptionPane.QUESTION_MESSAGE);

		if (name == null)
			return;
		if (name.length() == 0)
			name = "Copy of " + fc.getInnerWindow().getSelectedPage().getParameters().getText();
/*
		fc.doAndSendOperation(
				new NewPageOperation(
						fc.getInnerWindow().getSelectedPageIndex(),
						name,
						fc.getGDocument().generateNextId(),
						true));
						*/
	}

}

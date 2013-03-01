package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.DeleteOperation;
import fr.inria.rivage.elements.Page;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class RemovePage extends AbstractAction {

	RemovePage() {
		this.putValue(AbstractAction.NAME, "Remove this Page");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Remove the current Page");
	}
	
	@Override
	public boolean isEnabled() {
		FileController fc = Application.getApplication().getCurrentFileController();
		if (fc == null) {
			return false;
		}
		return (fc.getDocument().size() > 1);
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		FileController fc = Application.getApplication().getCurrentFileController();
		
		Page page = fc.getInnerWindow().getSelectedPage();
		
		int a = JOptionPane.showConfirmDialog(
				Application.getApplication().getMainFrame(),
				"Do you really want to remove the Page '" +
					fc.getInnerWindow().getSelectedPage().getParameters().getText()+ "'?",
				"Remove Page",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (a != JOptionPane.YES_OPTION) return;
				
		fc.doAndSendOperation(new DeleteOperation(fc.getCurrentWorkArea().getPage().getId()));
	}

}

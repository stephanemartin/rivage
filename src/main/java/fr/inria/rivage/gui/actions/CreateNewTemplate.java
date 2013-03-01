package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GTemplate;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CreateNewTemplate extends AbstractAction {

	CreateNewTemplate() {
		this.putValue(AbstractAction.NAME, "New Template");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Store the selected Objects as a new Template");
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/newtemplate.gif")));
	}
	
	@Override
	public boolean isEnabled() {
            return false;
		/*try {
			return Application.getApplication().getCurrentFileController()
				.getCurrentWorkArea().getSelectionManager().getSelObjects().size() > 0;
		} catch (Exception ex) {}
		return false;*/
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		GObject shape;
		ArrayList<GObject> objects = Application.getApplication().getCurrentFileController()
			.getCurrentWorkArea().getSelectionManager().getSelObjects(); 
		if (objects.size() == 0) {
			JOptionPane.showMessageDialog(
					Application.getApplication().getMainFrame(),
					"no object selected",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else if (objects.size() == 1) {
			shape = objects.get(0);
		} else {
			GGroup g = new GGroup();
			for (GObject obj : Application.getApplication().getCurrentFileController()
					.getDocument().getObjects()) {
				if (objects.contains(obj)) g.add(obj);
			}
			shape = g;
		}
		
		String name = JOptionPane.showInputDialog(
				Application.getApplication().getMainFrame(),
				"Enter a name for the template:",
				"New Template",
				JOptionPane.QUESTION_MESSAGE);
		if (name != null) {
                try {
                    new GTemplate(shape, name);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(CreateNewTemplate.class.getName()).log(Level.SEVERE, null, ex);
                }
		}
	}

}

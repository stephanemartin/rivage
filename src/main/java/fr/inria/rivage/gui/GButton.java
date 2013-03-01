package fr.inria.rivage.gui;

import javax.swing.AbstractAction;
import javax.swing.JButton;


public class GButton extends JButton {
	
	AbstractAction action;
	
	public GButton(AbstractAction action) {
		super(action);
		this.action = action;
		if (getIcon() != null) setText(null);
	}
	
	public void refresh() {
		setEnabled(action.isEnabled());
	}

}

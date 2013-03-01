package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class ViewLogMenu extends AbstractMenu {
	
	public ViewLogMenu() {
		super("Operation Log");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.SHOW_HISTORY.createMenuItem());
		add(Actions.SHOW_CONFLICTS.createMenuItem());
	}
	
}

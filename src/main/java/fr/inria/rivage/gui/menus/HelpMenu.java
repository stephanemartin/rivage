package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class HelpMenu extends AbstractMenu {
	
	public HelpMenu() {
		super("Help");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.SHOW_HELP.createMenuItem());
		add(Actions.SHOW_ABOUT.createMenuItem());
	}
	
}

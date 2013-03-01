package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class OptionsMenu extends AbstractMenu {
	
	public OptionsMenu() {
		super("Options");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.SHOW_SETTINGS.createMenuItem());
	}
	
}

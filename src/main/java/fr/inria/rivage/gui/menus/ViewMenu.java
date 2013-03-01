package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class ViewMenu extends AbstractMenu {
	
	public ViewMenu() {
		super("View");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.SHOW_STATUSBAR.createMenuItem());
		add(Actions.SHOW_OP_BUTTON_BAR.createMenuItem());
		addSeparator();
		add(Actions.SHOW_CHAT_FRAME.createMenuItem());
		addSeparator();
		add(new ViewLogMenu());
	}
	
}

package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class PageMenu extends AbstractMenu {
	
	public PageMenu() {
		super("Pages");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.NEW_PAGE.createMenuItem());
		//add(Actions.COPY_PAGE.createMenuItem());
		add(Actions.REMOVE_PAGE.createMenuItem());
		add(Actions.CHANGE_PAGE.createMenuItem());
	}
	
}

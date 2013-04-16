package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class EditMenu extends AbstractMenu {
	
	public EditMenu() {
		super("Edit");		
	}
	
	@Override
	protected void makeMenu() {
		/*add(Actions.UNDO_LOCAL.createMenuItem());
		add(Actions.REDO_LOCAL.createMenuItem());
		addSeparator();
		add(Actions.UNDO_GLOBAL.createMenuItem());
		add(Actions.REDO_GLOBAL.createMenuItem());*/
		add(Actions.UNDO.createMenuItem());
		add(Actions.REDO.createMenuItem());
                addSeparator();
		add(Actions.CUT_OBJS.createMenuItem());
		add(Actions.COPY_OBJS.createMenuItem());
		add(Actions.PASTE_OBJS.createMenuItem());
		add(Actions.DELETE_OBJS.createMenuItem());
		addSeparator();
		add(Actions.GROUP_OBJS.createMenuItem());
		add(Actions.UNGROUP_OBJS.createMenuItem());
		addSeparator();
		add(Actions.OBJS_TO_FRONT.createMenuItem());
		add(Actions.OBJS_TO_BACK.createMenuItem());
		addSeparator();
		//add(new LayerMenu());
		//addSeparator();
		add(Actions.CREATE_NEW_TEMPLATE.createMenuItem());
	}
	
}

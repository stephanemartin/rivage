package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class LayerMenu extends AbstractMenu {
	
	public LayerMenu() {
		super("Layers");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.CREATE_LAYER.createMenuItem());
		add(Actions.DELETE_LAYER.createMenuItem());
		add(Actions.UP_LAYER.createMenuItem());
		add(Actions.DOWN_LAYER.createMenuItem());
		//add(Actions.MOVE_OBJS_TO_LAYER.createMenuItem());
	}
	
}

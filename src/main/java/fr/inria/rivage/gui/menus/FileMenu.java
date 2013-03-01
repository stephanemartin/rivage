package fr.inria.rivage.gui.menus;

import fr.inria.rivage.gui.actions.Actions;


public class FileMenu extends AbstractMenu {
	
	public FileMenu() {
		super("File");		
	}
	
	@Override
	protected void makeMenu() {
		add(Actions.CONNECT_TO.createMenuItem());
		add(Actions.NEW_FILE.createMenuItem());
		add(Actions.OPEN_FILE.createMenuItem());
		add(Actions.SAVE_FILE.createMenuItem());
		add(Actions.CLOSE_FILE.createMenuItem());
		add(Actions.CLOSE_ALL_FILES.createMenuItem());
		addSeparator();
		add(new PageMenu());
		addSeparator();
		add(Actions.EXPORT_SVG.createMenuItem());
		add(Actions.IMPORT_SVG.createMenuItem());
		addSeparator();
		add(Actions.PRINT_FILE.createMenuItem());
		addSeparator();
		add(Actions.QUIT.createMenuItem());
	}
	
}

package fr.inria.rivage.gui.menus;

import javax.swing.Box;
import javax.swing.JMenuBar;


public class MenuBar extends JMenuBar {
	
	public MenuBar() {
		add(new FileMenu());
		add(new EditMenu());
		add(new ViewMenu());
		add(new OptionsMenu());
		add(Box.createHorizontalGlue());
		add(new HelpMenu());
	}

}

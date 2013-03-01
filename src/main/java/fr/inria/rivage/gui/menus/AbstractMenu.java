package fr.inria.rivage.gui.menus;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class AbstractMenu extends JMenu {
	
	public AbstractMenu(String name) {
		super(name);
		
		addMenuListener(new MenuListener() {

            @Override
			public void menuSelected(MenuEvent e) {
				removeAll();
				makeMenu();
			}

            @Override
			public void menuDeselected(MenuEvent e) {}

            @Override
			public void menuCanceled(MenuEvent e) {}
			
		});

	}
	
	protected abstract void makeMenu();
	
}

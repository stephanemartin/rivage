package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.gui.GButton;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.actions.Actions;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import java.awt.Component;
import javax.swing.JToolBar;


public class StandardButtonToolbar extends JToolBar implements
	CurrentWorkAreaListener {
	
	public StandardButtonToolbar() {
        setName("Standard buttons");
        setOrientation(JToolBar.HORIZONTAL);
        add(Actions.NEW_FILE.createButton());
        add(Actions.OPEN_FILE.createButton());
        add(Actions.SAVE_FILE.createButton());
        add(Actions.PRINT_FILE.createButton());
        
        InnerWindow.addWorkAreaListener(this);
        
        refresh();
    }
        
    private void refresh() {
    	Component[] c = getComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof GButton) ((GButton) c[i]).refresh();
		}
    }
    
    @Override
    public void currentWorkAreaChanged() {
		refresh();
	}
    
}

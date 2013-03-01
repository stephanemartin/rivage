package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.gui.GButton;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.actions.Actions;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import java.awt.Component;
import javax.swing.JToolBar;


public class OperationsToolBar extends JToolBar implements
	TreeChangeListener, SelectionChangeListener, CurrentWorkAreaListener {

	public OperationsToolBar() {
        setName("Operations");
        setOrientation(JToolBar.HORIZONTAL);
        
        add(Actions.GROUP_OBJS.createButton());
    	add(Actions.UNGROUP_OBJS.createButton());
    	addSeparator();
    	add(Actions.OBJS_TO_FRONT.createButton());
    	add(Actions.OBJS_UP.createButton());
    	add(Actions.OBJS_DOWN.createButton());
        add(Actions.OBJS_TO_BACK.createButton());
    	addSeparator();
    	/*add(Actions.UNDO_LOCAL.createButton());
    	add(Actions.REDO_LOCAL.createButton());
    	addSeparator();
    	add(Actions.UNDO_GLOBAL.createButton());
    	add(Actions.REDO_GLOBAL.createButton());*/
        add(Actions.UNDO.createButton());
    	add(Actions.REDO.createButton());
    	/*addSeparator();
		add(Actions.CREATE_LAYER.createButton());
		add(Actions.MOVE_OBJS_TO_LAYER.createButton());
		add(Actions.DELETE_LAYER.createButton());*/
    	addSeparator();
        GButton b=Actions.DELETE_OBJS.createButton();
        
    	add(b);
    	addSeparator();
    	add(Actions.CREATE_NEW_TEMPLATE.createButton());
    	
    	SelectionManager.addGeneralSelectionListener(this);
    	InnerWindow.addWorkAreaListener(this);
        
        refresh();
    }
    
    public void refresh() {
    	Component[] c = getComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof GButton) ((GButton) c[i]).refresh();
		}
    }

    @Override
	public void treeChanged() {
		refresh();
	}
	
    @Override
    public void selectionChanged() {
		refresh();		
	}
    
    @Override
	public void currentWorkAreaChanged() {
		refresh();
	}
    
}

package fr.inria.rivage.gui.listener;

import fr.inria.rivage.engine.concurrency.tools.ID;


public interface PageChangeListener {
	
        public static enum Event{NEW_PAGE,PAGE_REMOVED,PAGE_CHANGED};
	public void pageChanged(Event mode, ID id, int index);

}

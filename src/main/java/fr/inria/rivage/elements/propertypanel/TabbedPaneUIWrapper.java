package fr.inria.rivage.elements.propertypanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.TabbedPaneUI;

/**
 * This class wraps the TabbedPaneUI class. TabbedPaneUI causes a
 * strange IndexOutOfBoundsException that we catch with this wrapper.
 * 
 * @author Tobias Kuhn
 */
public class TabbedPaneUIWrapper extends TabbedPaneUI {
	
	// the wrapped object
	private TabbedPaneUI ui;
	
	public TabbedPaneUIWrapper(TabbedPaneUI ui) {
		this.ui = ui;
	}
	
	// Delegate to the TabbedPaneUI object and catch IndexOutOfBoundsException.
    @Override
	public void update(Graphics g, JComponent c) {
		try {
			// strange IndexOutOfBoundsException happens here...
			ui.update(g, c);
		} catch (IndexOutOfBoundsException ex) {}
	}
	
	// All the following methods just delegate to the TabbedPaneUI object.
	// No need to catch any exception here.

    @Override
	public Rectangle getTabBounds(JTabbedPane pane, int index) {
		return ui.getTabBounds(pane, index);
	}

    @Override
	public int getTabRunCount(JTabbedPane pane) {
		return ui.getTabRunCount(pane);
	}

    @Override
	public int tabForCoordinate(JTabbedPane pane, int x, int y) {
		return ui.tabForCoordinate(pane, x, y);
	}

    @Override
	public boolean contains(JComponent c, int x, int y) {
		return ui.contains(c, x, y);
	}

    @Override
	public Accessible getAccessibleChild(JComponent c, int i) {
		return ui.getAccessibleChild(c, i);
	}

    @Override
	public int getAccessibleChildrenCount(JComponent c) {
		return ui.getAccessibleChildrenCount(c);
	}

    @Override
	public Dimension getMaximumSize(JComponent c) {
		return ui.getMaximumSize(c);
	}

    @Override
	public Dimension getMinimumSize(JComponent c) {
		return ui.getMinimumSize(c);
	}

    @Override
	public Dimension getPreferredSize(JComponent c) {
		return ui.getPreferredSize(c);
	}

    @Override
	public void installUI(JComponent c) {
		ui.installUI(c);
	}

    @Override
	public void paint(Graphics g, JComponent c) {
		ui.paint(g, c);
	}

    @Override
	public void uninstallUI(JComponent c) {
		ui.uninstallUI(c);
	}

    @Override
	public boolean equals(Object obj) {
		return ui.equals(obj);
	}

    @Override
	public int hashCode() {
		return ui.hashCode();
	}

    @Override
	public String toString() {
		return ui.toString();
	}

}

package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;

public class ZoomToolbar extends JToolBar implements
	ActionListener, CurrentWorkAreaListener {
	
	private JComboBox cb;
	private int zoomPct;

	public ZoomToolbar() {
		super("Zoom", JToolBar.HORIZONTAL);
				
		add(new JLabel("Zoom % "));
		
		cb = new JComboBox(new Integer[] {10, 20, 50, 75, 100, 150, 200, 300, 400, 800, 1600});
		cb.setEditable(true);
		cb.addActionListener(this);
		add(cb);
		
		setMaximumSize(getPreferredSize());
		
		InnerWindow.addWorkAreaListener(this);
		
		refresh();
	}
	
	public void refresh() {
		try {
			WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
			zoomPct = (int) (wa.getZoom() * 100);
			cb.getEditor().setItem(zoomPct);
			cb.setEnabled(true);
		} catch (NullPointerException ex) {
			zoomPct = 100;
			cb.getEditor().setItem(zoomPct);
			cb.setEnabled(false);
		}
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		String s = cb.getEditor().getItem().toString();
		try {
			int z = Integer.parseInt(s);
			WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
			wa.setZoom(z/100.0);
		} catch (NumberFormatException ex) {
			cb.getEditor().setItem(zoomPct);
		}
	}

    @Override
	public void currentWorkAreaChanged() {
		refresh();
	}
	
}
package fr.inria.rivage.gui;

import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class ObjectInspector extends JToolBar implements
	TreeChangeListener, SelectionChangeListener, CurrentWorkAreaListener {

	public final static int PWIDTH = 170;

	public final static int PHEIGHT = 150;

	private JLabel title;

	private JTextField note;

	private JScrollPane scroll;

	private PropertyPanel propPanel;

	public ObjectInspector() {
		super();
		setName("Object Inspector");
		setOrientation(JToolBar.VERTICAL);

		setSize(new Dimension(PWIDTH + 20, PHEIGHT));

		title = new JLabel("Object Inspector");
		title.setForeground(Color.blue);

		note = new JTextField("Inactive");
		note.setBackground(Color.lightGray);
		note.setForeground(Color.red);
		note.setFont(new Font("times", Font.BOLD, 14));

		propPanel = new PropertyPanel();
		scroll = new JScrollPane(propPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVisible(true);
		scroll.setEnabled(true);
		add(scroll, BorderLayout.CENTER);

		JPanel panelNorth = new JPanel(new BorderLayout());
		panelNorth.add(title, BorderLayout.NORTH);
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		
		SelectionManager.addGeneralSelectionListener(this);
		InnerWindow.addWorkAreaListener(this);

		revalidate();
	}

    @Override
	public void treeChanged() {
		propPanel.update();
	}

    @Override
	public void selectionChanged() {
		propPanel.update();
	}

    @Override
	public void currentWorkAreaChanged() {
		propPanel.update();		
	}

}

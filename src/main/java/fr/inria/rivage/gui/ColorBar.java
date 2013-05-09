package fr.inria.rivage.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA. User: Lorant Zeno Csaszar Email:
 * lcsaszar@inf.ethz.ch Date: Mar 4, 2003 Time: 5:19:23 PM
 */
public class ColorBar extends JComponent {

	protected MainFrame parent;

	// component elements
	protected JTextField textField;

	private ColorChooserFrame colorChooserFrame;

	private boolean enabled;

	public ColorBar(MainFrame parent, String toolTipText) {
		this.parent = parent;
		enabled = false;
		setToolTipText(toolTipText);
		setFocusable(false);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
			.createRaisedBevelBorder(), BorderFactory
			.createLoweredBevelBorder()));
		setBounds(0, 0, 22, 22);
		// create text field
		textField = new JTextField();
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setEditable(false);
		textField.setFocusable(false);
		textField.addMouseListener(new MouseHandler());
		textField.setToolTipText(toolTipText);
		add(textField);
		colorChooserFrame = new ColorChooserFrame();
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!enabled)
				return;
			colorChooserFrame.setVisible(true);
		}
	}

	private class ColorChooserFrame extends JDialog implements ChangeListener,
			ActionListener {
		protected JColorChooser cc;

		protected JButton ok, cancel;

		private Color newColor;

		public ColorChooserFrame() {
			super(parent, "Color Chooser", true);
			cc = new JColorChooser(textField.getBackground());
			cc.getSelectionModel().addChangeListener(this);
			cc.setBorder(BorderFactory
				.createTitledBorder("Choose Object Color"));
			ok = new JButton("Ok");
			ok.addActionListener(this);
			cancel = new JButton("Cancel");
			cancel.addActionListener(this);
			// intermadiar
			JPanel panel = new JPanel();
			panel.add(ok);
			panel.add(cancel);
			// frame init
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(cc, BorderLayout.CENTER);
			getContentPane().add(panel, BorderLayout.PAGE_END);
			newColor = textField.getBackground();
			pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((screenSize.width - getWidth()) / 2,
					(screenSize.height - getHeight()) / 2);
		}

        @Override
		public void stateChanged(ChangeEvent e) {
			newColor = cc.getColor();
		}

        @Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.startsWith("Ok")) {
				textField.setBackground(newColor);
				/*Application.getApplication().getCurrentFileController()
					.getCurrentWorkArea().updateColors();*/
				this.dispose();
			} else if (cmd.startsWith("Cancel")) {
				this.dispose();
			}
		}

	}

	public void setColor(Color color) {
		textField.setBackground(color);
	}

	public Color getColor() {
		return textField.getBackground();
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (!enabled)
			textField.setBackground(Color.LIGHT_GRAY);
	}

}

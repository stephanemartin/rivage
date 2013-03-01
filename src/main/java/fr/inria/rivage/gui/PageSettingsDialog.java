package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PageSettingsDialog extends JDialog implements ActionListener {
	
	private JTextField nameField, widthField, heightField;
	private boolean ok = false; // gets true when the OK-button gets clicked
	private String name;
	private int width, height;
	
	public PageSettingsDialog(String title, String name, Dimension dim) {
		super(Application.getApplication().getMainFrame(), title, true);
		setSize(300, 200);
		setLocation(300,200);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 2, 2));
		
		panel.add(createLabeledTextField(nameField = new JTextField(name), "Page name:"));
		panel.add(createLabeledTextField(widthField = new JTextField(dim.width + ""), "Page width:"));
		panel.add(createLabeledTextField(heightField = new JTextField(dim.height + ""), "Page height:"));
		
		JPanel scrollPaneContent = new JPanel(new BorderLayout());
		scrollPaneContent.add(panel, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(
				scrollPaneContent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			name = nameField.getText();
			if (name == null || name.length() == 0) {
				JOptionPane.showMessageDialog(this, "Not a valid name!",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				width = Integer.parseInt(widthField.getText());
				height = Integer.parseInt(heightField.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Illegal number!",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (width <= 0 || height <= 0) {
				JOptionPane.showMessageDialog(this, "Only positive values are allowed!",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ok = true;
			setVisible(false);
		} else if (e.getActionCommand().equals("Cancel")) {
			ok = false;
			setVisible(false);
		}
	}
	
	public boolean ok() {
		return ok;
	}
	
    @Override
	public String getName() {
		if (ok) return name;
		return null;
	}
	
	public Dimension getDimension() {
		if (ok) return new Dimension(width, height);
		return null;
	}
	
	private static JPanel createLabeledTextField(JTextField textField, String labelText) {
		JPanel panel = new JPanel(new BorderLayout(2, 2));
		JLabel label = new JLabel(labelText);
		label.setMinimumSize(new Dimension(75, 0));
		label.setPreferredSize(new Dimension(75, 0));
		panel.add(label, BorderLayout.WEST);
		panel.add(textField, BorderLayout.CENTER);
		return panel;
	}
	
}

package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class TextDialog extends JDialog implements ActionListener {
	
	private JTextPane textPane;
	private boolean ok; // gets true when the OK-button gets clicked
	
	public TextDialog(String title, String text) {
		super(Application.getApplication().getMainFrame(), title, true);
		setSize(300,200);
		setLocation(300,200);
		getContentPane().add(new JScrollPane(textPane = new JTextPane(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
					
		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		textPane.setText(text);
		textPane.setSelectionStart(0);
		textPane.setSelectionEnd(text.length());
		
		setVisible(true);
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			ok = true;
		} else if (e.getActionCommand().equals("Cancel")) {
			ok = false;
		}
		setVisible(false);
	}
	
	public String getText() {
		if (ok) return textPane.getText();
		return null;
	}
	
}

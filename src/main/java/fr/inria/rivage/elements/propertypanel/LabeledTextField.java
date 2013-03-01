package fr.inria.rivage.elements.propertypanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledTextField extends JPanel implements ActionListener {
	
	private JTextField textField;
	private ActionListener actionListener;
	
	public LabeledTextField(String labelText, ActionListener actionListener) {
		this.actionListener = actionListener;
		setLayout(new GridLayout(1, 2));
		textField = new JTextField(7);
		textField.addActionListener(LabeledTextField.this);
		add(new JLabel(labelText));
		add(textField);
	}
	
	public String getText() {
		return textField.getText();
	}
	
	public void setText(String text) {
		textField.setText(text);
	}
	
	public void setEditable(boolean editable) {
		textField.setEditable(editable);
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		actionListener.actionPerformed(new ActionEvent(this, e.getID(), e.getActionCommand()));
	}
	
}

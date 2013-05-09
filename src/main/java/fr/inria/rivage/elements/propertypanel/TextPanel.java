package fr.inria.rivage.elements.propertypanel;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.operations.ModifyOperation;
import fr.inria.rivage.gui.PropertyPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This is a panel for an object that implements the
 * interface ITextable, in order to be able to change its text.
 * 
 * @author Tobias Kuhn
 */
public class TextPanel extends JPanel implements IPropPanel, ActionListener, KeyListener {

	private static final Font FONT = new Font("Arial", Font.PLAIN, 12);
	
	private GObject obj;
	private LabeledTextField fontSize;
	private JTextArea textArea;
	
	private PropertyPanel pp;

	/**
	 * Creates a new panel for the textable object.
	 * @param obj the textable object
	 */
	public TextPanel(PropertyPanel pp) {
		this.pp = pp;
		
		setLayout(new BorderLayout());
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fontSize = new LabeledTextField("Font size:", this);
		panel.add(fontSize);
		add(panel, BorderLayout.NORTH);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(FONT);
		textArea.addKeyListener(this);
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Text"));
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == fontSize) {
			IConcurrencyController cc = Application.getApplication().getCurrentFileController().getConcurrencyController();
			try  {
				int s = Integer.parseInt(fontSize.getText());
                                throw new UnsupportedOperationException("Not implemented yet");
				//cc.doAndSendOperation(new ModifyOperation(obj,Parameters.ParameterType.Font, new Font(s)));
			} catch (NumberFormatException ex) {
				//fontSize.setText(obj.getFontSize() + "");
			}
		}
	}
	
    @Override
	public void refreshObject() {
		obj = null;
		
		/*for (GObjectShape o : pp.getAtomObjects()) {
			if (o instanceof ITextable) {
				if (obj == null) {
					//obj = (ITextable) o;
				} else {
					return;
				}
			}
		}*/
		
		if (obj == null) return;
		//pp.addTab("Text", this);
		
		//textArea.setText(obj.getText());
		
		/*if (obj.isFontResizable()) {
			fontSize.setText(obj.getFontSize() + "");
			fontSize.setVisible(true);
		} else {
			fontSize.setVisible(false);
		}*/
	}

    @Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			IConcurrencyController cc = Application.getApplication().getCurrentFileController().getConcurrencyController();
			String text = textArea.getText();
			cc.doAndSendOperation(new ModifyOperation(obj, Parameters.ParameterType.Text, text));
		}
	}

    @Override
	public void keyReleased(KeyEvent e) {}

    @Override
	public void keyTyped(KeyEvent e) {}
	
}
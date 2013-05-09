package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.svg.decoder.SVGDecodeException;
import fr.inria.rivage.engine.svg.decoder.SVGDecoder;
import fr.inria.rivage.gui.MainFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ImportSVG extends AbstractAction {
    private static final Logger log = Logger.getLogger(ImportSVG.class.getName());
	
	

	ImportSVG() {		
		this.putValue(AbstractAction.NAME, "Import SVG");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Load a local SVG file.(Not implemented yet)");
	}
	
	private class LogDialog extends JDialog implements ActionListener {

		public LogDialog(String text) {
			super(Application.getApplication().getMainFrame(), "SVG decoded", true);
			setSize(400, 300);
			setLayout(new BorderLayout());
			JTextArea textArea = new JTextArea(text);
			textArea.setEditable(false);
			add(new JScrollPane(textArea), BorderLayout.CENTER);
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JButton okButton = new JButton("OK");
			okButton.addActionListener(this);
			buttonPanel.add(okButton);
			add(buttonPanel, BorderLayout.SOUTH);
			setVisible(true);
		}
		
        @Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
		}
		
	}
	
	@Override
	public boolean isEnabled() {
            return false;
		//return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		MainFrame mf = Application.getApplication().getMainFrame();
		
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(mf);
		if (returnVal != JFileChooser.APPROVE_OPTION) return;
		File file = chooser.getSelectedFile();
		
		SVGDecoder svgDecoder = new SVGDecoder(file);
		GGroup svgRoot = null;
		try {
			svgRoot = svgDecoder.decode();
		} catch (SVGDecodeException ex) {
			log.log(Level.SEVERE, "Could not decode the SVG file.{0}", ex);
			JOptionPane.showMessageDialog(
					mf,
					"Could not decode the SVG file.",
					"Invalid SVG file",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String decodeLog = svgDecoder.getDecodeLog();
		if (decodeLog.length() == 0) {
			new LogDialog("SVG successfully decoded!");
		} else {
			new LogDialog("SVG decoded!\nSome warnings occured:\n\n" + decodeLog);
		}
		
		FileController fc = Application.getApplication().getCurrentFileController();
		fc.doAndSendOperation(
				fc.getCurrentWorkArea().getCreateOperation(svgRoot));
	}

}

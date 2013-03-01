package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.svg.encoder.SVGEncoder;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ExportSVG extends AbstractAction {

	ExportSVG() {		
		this.putValue(AbstractAction.NAME, "Export as SVG");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Save this file locally in the SVG format");
	}
	
	@Override
	public boolean isEnabled() {
		return Application.getApplication().getCurrentFileController() != null;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		MainFrame mf = Application.getApplication().getMainFrame();
		Page page = Application.getApplication().getCurrentFileController()
			.getCurrentWorkArea().getPage();
		
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(mf);
		if (returnVal != JFileChooser.APPROVE_OPTION) return;
		File file = chooser.getSelectedFile();
		
		if (file.exists()) {
			int a = JOptionPane.showConfirmDialog(
					mf,
					"The file '" + file.getName() + "' already exists. Do you want to replace it?",
					"File already exists",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (a != JOptionPane.YES_OPTION) return;
		}
		
		SVGEncoder svgEncoder = new SVGEncoder(page, file);
		try {
			svgEncoder.create();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(
					mf,
					"Could not save the file:\n" + ex.getMessage(),
					"saving file failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(
				mf,
				"The file has been successfully exported.",
				"Exported",
				JOptionPane.INFORMATION_MESSAGE);
	}

}

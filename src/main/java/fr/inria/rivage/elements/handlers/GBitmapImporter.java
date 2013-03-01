package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.shapes.GBitmap;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.RemoteFileChooser;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;

public class GBitmapImporter extends GHandler {

	private WorkArea wa;

	private String imageName;

	GBitmapImporter() {
	}

	@Override
	public void init(WorkArea wa) {
		this.wa = wa;
		wa.getSelectionManager().clearSelection();

		try {
			RemoteFileChooser rfc;
			rfc = new RemoteFileChooser(Application.getApplication()
					.getMainFrame(), RemoteFileChooser.DATA_FILES);
			rfc.setVisible(true);
			//imageName = rfc.getSelectedFile();
			if (imageName == null) {
				wa.setMode(Handlers.SELECTION);
				return;
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(wa, "Could not load the file!",
					"Error", JOptionPane.ERROR_MESSAGE);
			wa.setMode(Handlers.SELECTION);
			return;
		}

		wa.setCursor(Cursors.importing);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (imageName == null)
			return;
		Point2D p = wa.getDrawingPoint(e.getPoint());

		GBitmap bitmap = new GBitmap(p, imageName);

		wa.getFileController().getConcurrencyController().doAndSendOperation(
				wa.getCreateOperation(bitmap));

		wa.setMode(Handlers.SELECTION);
	}

}

package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GFile;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.tree.XMLDecoder;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.RemoteFileChooser;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class GFileImporter extends GHandler {

    @Override
    public void init(WorkArea wa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
//	private WorkArea wa;
//	private GFile file;
//	
//	GFileImporter() {}
//	
//	@Override
//	public void init(WorkArea wa) {
//		this.wa = wa;
//		wa.getSelectionManager().clearSelection();
//	    
//		try {
//			RemoteFileChooser rfc;
//			String filename=null;
//			rfc = new RemoteFileChooser(Application.getApplication().getMainFrame(),
//					RemoteFileChooser.DOCUMENT_FILES);
//			rfc.setVisible(true);
//			//filename = rfc.getSelectedFile();
//			if (filename == null) {
//				wa.setMode(Handlers.SELECTION);
//				return;
//			}
//			
//			String xmlString =null;/* Application.getApplication().getFileManagerController()
//				.loadFileAsString(GroupController.DOCUMENT_FILES, filename);*/
//			
//			GDocument doc = XMLDecoder.getDocument(
//					XMLDecoder.getXMLElement(xmlString));
//			
//			List pages = doc.getChildren();
//			Page page;
//			
//			if (pages.size() <= 1) {
//				page = (Page)pages.get(0);
//			} else {
//				page = (Page) JOptionPane.showInputDialog(
//						wa,
//						"Choose the page to import.",
//						"Select Page",
//						JOptionPane.OK_CANCEL_OPTION,
//						null,
//						pages.toArray(),
//						pages.get(0));
//				
//				if (page == null) {
//					wa.setMode(Handlers.SELECTION);
//					return;
//				}
//			}
//			
//			
//			//file = new GFile(filename, pages.indexOf(page));
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(
//					wa,
//					"Could not load the file!",
//					"Error",
//					JOptionPane.ERROR_MESSAGE);
//			ex.printStackTrace();
//			wa.setMode(Handlers.SELECTION);
//			return;
//		}
//		
//		wa.setCursor(Cursors.importing);
//	}
//	
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		/*Point2D point = wa.getDrawingPoint(e.getPoint());
//
//		Transformer.translate(
//				file,
//				/*point.getX() - file.getScreenBounds2D().x - file.getScreenBounds2D().width/2,
//				point.getY() - file.getScreenBounds2D().y - file.getScreenBounds2D().height/2);*
//		
//		wa.getFileController().getConcurrencyController().doOperation(
//				wa.getCreateOperation(file));
//
//		wa.setMode(Handlers.SELECTION);*/
//	}

}

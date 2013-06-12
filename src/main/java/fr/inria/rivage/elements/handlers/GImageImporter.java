package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.shapes.GImage;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.tools.FilesTools;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class GImageImporter extends GHandler {

    private static final Logger LOG = Logger.getLogger(GImageImporter.class.getName());
    private WorkArea wa;
    private ImageIcon imageName;

    GImageImporter() {
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        wa.getSelectionManager().clearSelection();

        wa.setCursor(Cursors.importing);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        PointDouble p = wa.getDrawingPoint(e.getPoint());
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FilesTools.ImageFileFilter());
        
        int resp=jfc.showOpenDialog(Application.getApplication().getMainFrame());
        // System.out.println("test");

        try {
            if (resp==JFileChooser.APPROVE_OPTION &&  jfc.getSelectedFile() != null ) {
                ImageIcon img = new ImageIcon(jfc.getSelectedFile().getPath());
                GImage bitmap = new GImage(wa.getActiveLayer(), p, img, wa.getCurrentStroke());

                wa.getFileController().doAndSendOperation(wa.getCreateOperation(bitmap));
            }
        } catch (Exception ex) {
            LOG.severe(ex.toString());
        }
        wa.setMode(Handlers.SELECTION);
    }
}

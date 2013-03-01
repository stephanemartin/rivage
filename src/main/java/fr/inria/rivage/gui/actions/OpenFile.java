package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.RemoteFileChooser;
import fr.inria.rivage.tools.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class OpenFile extends AbstractAction {

    OpenFile() {
        this.putValue(AbstractAction.NAME, "Open");
        this.putValue(AbstractAction.SHORT_DESCRIPTION, "Open a file");
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/open.gif")));
        this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_O);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker openWorker = new SwingWorker() {
            @Override
            public Object construct() {
                doOpen();
                return null;
            }
        };
        openWorker.start();
    }

    private void doOpen() {
        /**
         * Ask the user which file to open. If none selected or an error
         * happens, just do nothing.
         */
        RemoteFileChooser rfc;
        FileController filename;
        try {
            rfc = new RemoteFileChooser(Application.getApplication().getMainFrame(),
                    RemoteFileChooser.DOCUMENT_FILES);
            rfc.setVisible(true);
        } catch (Exception e) {
            return;
        }

        filename = rfc.getSelectedFile();

        if (filename == null) {
            return;
        }
        filename.askDocuement();
        //Application.getApplication().getFileManagerController().askDocument(filename);
        /**
         * The user now selected a file. We can now create a filecontroller for
         * that one, register it in the application. The FileController should
         * take care about setting the other things right, like the windows,
         * buttone ... It should also take care about getting the file,
         * negotiating the group join and ...
         */
        
        //fc.loadFile();

    }
}

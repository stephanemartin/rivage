package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.tools.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

public class NewFile extends AbstractAction {

    private Logger log;
    private String fileName;

    NewFile() {
        log = Logger.getLogger(getClass());
        putValue(AbstractAction.NAME, "New");
        putValue(AbstractAction.SHORT_DESCRIPTION, "Create a new file");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/new.gif")));
        putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_F);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
         * New file button
         */
        /*
         * NewFileDialog dialog = new NewFileDialog(Application.getApplication().getMainFrame());
         */
        Object filenameO = JOptionPane.showInputDialog(Application.getApplication().getMainFrame(), "Enter the file name : ", "New file", JOptionPane.QUESTION_MESSAGE, null, null, "filename");//.toString();/*dialog.getFileName();*/
        if (filenameO == null) {
            return;
        }
        fileName = filenameO.toString();
		//Application.getApplication().getFileManagerController().createNewFile(fileName);
		/*
         * try {
         *
         * Application.getApplication().getFileManagerController().createNewFile(fileName);
         * // TODO : ???????????? } catch (IOException ex) { log.error("Could
         * not create file on the server.", ex); } catch (ClassNotFoundException
         * ex) { log.error("Could not create file on the server.", ex);
		}
         */

        SwingWorker openWorker;
        openWorker = new SwingWorker() {

@Override
public Object construct() {

                FileController fileController = new FileController(fileName);
                //Application.getApplication().getFileManagerController().registerNewFile(fc);
    
    return null;
}
};
        openWorker.start();
    }
}

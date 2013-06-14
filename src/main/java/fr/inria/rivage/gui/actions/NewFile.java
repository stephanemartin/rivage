package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.dialog.NewFileDialog2;
import fr.inria.rivage.tools.SwingWorker;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class NewFile extends AbstractAction {

    private String fileName;
    private Dimension dim;

    NewFile() {

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

        NewFileDialog2 dialog = new NewFileDialog2(Application.getApplication().getMainFrame(), true);
        dialog.setVisible(true);

        /*Object filenameO = JOptionPane.showInputDialog(Application.getApplication().getMainFrame(), "Enter the file name : ", "New file", JOptionPane.QUESTION_MESSAGE, null, null, "filename");//.toString();/*dialog.getFileName();*/
        /*if (filenameO == null) {
         return;
         }*/
        if (!dialog.isOk()) {
            return;
        }


        fileName = dialog.getFilename() + " (" + Application.getApplication().getNetwork().getMe().getName() + ")";
        dim = dialog.getDimension();
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

                new FileController(fileName, dim);
                //Application.getApplication().getFileManagerController().registerNewFile(fc);

                return null;
            }
        };
        openWorker.start();
    }
}

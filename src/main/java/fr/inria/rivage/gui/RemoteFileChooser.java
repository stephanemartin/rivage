/*
 * Created on Jul 19, 2004
 */
package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.net.group.FileControllerManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author Yves
 */
public class RemoteFileChooser extends JDialog implements ActionListener, Observer {

    public static final int DATA_FILES = 0x000001;
    public static final int DOCUMENT_FILES = 0x000002;
    ///private int fileType;
    private FileController selectedFile;
    private JList jlist;
    private FileList fileList;
    private FileControllerManager fcm;

    public void update(Observable o, Object arg) {
        fileList.fireContentsChanged(o, 0, fileList.getSize());
    }

    class FileList extends AbstractListModel {

        List list;

        public FileList(FileControllerManager fcm) {
            this.list = fcm.getFiles();

        }

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return this.list.get(index);
        }

        @Override
        public void fireContentsChanged(Object source, int index0, int index1) {
            super.fireContentsChanged(source, index0, index1);
        }
    }
    class ActionList extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()>1){
                int index = RemoteFileChooser.this.jlist.locationToIndex(e.getPoint());
                RemoteFileChooser.this.selectFile( (FileController)RemoteFileChooser.this.jlist.getModel().getElementAt(index));
                RemoteFileChooser.this.jlist.ensureIndexIsVisible(index);
            }
            
        }
       
                
    }
    /**
     * Initializes a dialog to choose files on the server.
     *
     * @param parent a parent <code>JFrame</code>
     * @param fileType either <code>DATA_FILES</code> or
     * <code>DOCUMENT_FILES</code>
     * @throws IOException in case some connection error occured. This exception
     * is only for information, the user already knows about it.
     * @throws ClassNotFoundException in case the server and client have not the
     * same version. This exception is only for information, the user already
     * knows about it.
     */
    public RemoteFileChooser(JFrame parent, int fileType) {
        super(parent, "Choose a file to open", true);
        build(parent, fileType);
    }

    final void build(JFrame parent, int fileType) {
        this.selectedFile = null;
        fcm = Application.getApplication().getFileManagerController();

        fileList = new FileList(fcm);


        setLocation(Application.getApplication().getMainFrame().getLocation().x + 100,
                Application.getApplication().getMainFrame().getLocation().y + 100);

        /*this.fileType = (fileType == DOCUMENT_FILES
         ? GroupController.DOCUMENT_FILES
         : GroupController.DATA_FILES);*/

        setTitle("Remote file chooser for " + (fileType == DOCUMENT_FILES ? "Documents" : "Images"));
        ((BorderLayout) getLayout()).setHgap(6);
        ((BorderLayout) getLayout()).setVgap(6);


        jlist = new JList(fileList);
        this.setPreferredSize(new Dimension(300, 300));

        JButton btnOK = new JButton("OK");
        btnOK.setActionCommand("OK");
        btnOK.addActionListener(this);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setActionCommand(("CANCEL"));
        btnCancel.addActionListener(this);
        getContentPane().add(jlist, BorderLayout.CENTER);

        JPanel jpanel = new JPanel(new GridLayout(1, 2, 6, 6));
        jpanel.add(btnOK);
        jpanel.add(btnCancel);

        getContentPane().add(jpanel, BorderLayout.SOUTH);
        if (fcm.getFiles().size() > 0) {
            jlist.setSelectionInterval(0, 0);
        }
        
        
        
        jlist.addMouseListener(new ActionList());

        fcm.addObserver(this);
        pack();
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            selectFile((FileController) jlist.getSelectedValue());
        } else if (e.getActionCommand().equals("CANCEL")) {
            selectFile(null);
        }
    }

    void selectFile(FileController fc){
         selectedFile=fc;
         setVisible(false);
        fcm.deleteObserver(this);
    }
    public FileController getSelectedFile() {
        return selectedFile;
    }
}
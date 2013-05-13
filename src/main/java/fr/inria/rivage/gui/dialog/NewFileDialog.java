package fr.inria.rivage.gui.dialog;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class NewFileDialog extends JDialog implements ActionListener {
	
	private static final String fileEnding = "";
	private static final String defaultFileName = "filename";
	
    private static final Logger log = Logger.getLogger(NewFileDialog.class.getName());
	
	private JList fileList;
	private JTextField fileNameField;
	private JButton ok, cancel;
	
	private String fileName;

	public NewFileDialog(JFrame parent) {
		super(parent, "Creating a new file", true);
		
		setSize(400, 300);
		setLayout(new BorderLayout());
		fileList = new JList();
		fileList.setEnabled(false);
		JScrollPane scroll = new JScrollPane(fileList);
		add(scroll, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel(new BorderLayout());
		add(southPanel, BorderLayout.SOUTH);
		
		fileNameField = new JTextField(defaultFileName + fileEnding);
		fileNameField.setSelectionStart(0);
		fileNameField.setSelectionEnd(defaultFileName.length());
		
		southPanel.add(fileNameField, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(ok = new JButton("ok"));
		ok.addActionListener(this);
		buttonPanel.add(cancel = new JButton("cancel"));
		cancel.addActionListener(this);
		southPanel.add(buttonPanel, BorderLayout.EAST);
		
		loadFileList();
		
		setVisible(true);
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			fileName = fileNameField.getText();
			if (!fileName.endsWith(fileEnding)) fileName += fileEnding;
		}
		setVisible(false);
	}
	
	public String getFileName() {
		return fileName;
	}
	
	private void loadFileList() {
		Collection <FileController> f = null;
		//try {
			f = Application.getApplication().getFileManagerController().getFiles();
                        //LinkedList <Sting>
		/*} catch (IOException ex) {
			log.error("Could not load file list from server.", ex);
		} catch (ClassNotFoundException ex) {
			log.error("Could not load file list from server.", ex);
		}*/
		fileList.setListData(f.toArray());
	}

}
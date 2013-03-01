/*
 * Created on Jan 17, 2005
 */
package fr.inria.rivage.gui;

/**
 * @author Stavroula Papadopoulou
 */
import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.CreateOperation;
import fr.inria.rivage.engine.operations.MoveToLayerOperation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SpecifyLayer extends JDialog implements ActionListener,
        KeyListener {

    private JButton buttonNew, cancel;
    private JTextField text;
    private JPanel northPanel, southPanel;

    public SpecifyLayer() {
        super(Application.getApplication().getMainFrame());
        setModal(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(5, 5));

        northPanel = new JPanel();
        northPanel.add(new JLabel("Name:"));
        text = new JTextField(30);
        text.addKeyListener(this);
        northPanel.add(text);

        southPanel = new JPanel();
        buttonNew = new JButton("Create");
        buttonNew.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        southPanel.add(buttonNew);
        southPanel.add(cancel);

        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();
        if (cmd.equals("Create")) {
            createLayer();

        } else {
            setVisible(false);
            //return;
        }
    }

    private ArrayList<String> getLayerNames() {
        ArrayList<String> layerName = new ArrayList<String>();
        for (GObject l : Application.getApplication()
                .getCurrentFileController().getCurrentWorkArea().getPage()) {
            layerName.add(l.getParameters().getText());
        }
        return layerName;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            createLayer();
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void createLayer() {
        MainFrame mf = Application.getApplication().getMainFrame();
        String textFromTextField = text.getText();

        if (getLayerNames().contains(textFromTextField)) {
            JOptionPane.showMessageDialog(mf,
                    "There is already a Layer with this name!\n"
                    + "Type in a new name for the new Layer");
            return;
        }

        if (textFromTextField.equals("Type in the name of the new Layer")
                || textFromTextField.equals("")) {
            JOptionPane.showMessageDialog(mf,
                    "Please type in a name for the new Layer");
            return;
        } else {
            //  try {
            FileController fc = Application.getApplication().getCurrentFileController();
            IConcurrencyController cc = fc.getConcurrencyController();
            ID newId = cc.getNextID();
            // The user specified a valid name for the new layer.
            GLayer layer = new GLayer(fc.getCurrentWorkArea().getPage(), newId, textFromTextField);
            
            cc.doAndSendOperation(new CreateOperation(layer));
            /* ArrayList<GObject> members = fc.getCurrentWorkArea().getSelectionManager().getSelObjects();
             ArrayList<ID> membersID = new ArrayList<ID>();
             for (GObject o : members) {
             membersID.add(o.getId());
             }
             for (ID m : membersID) {
             cc.doAndSendOperation(new MoveToLayerOperation(m, newId));
             }*/


            dispose();

        }
    }
}

package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.net.overlay.IComputer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class ConnectedComputer extends JToolBar implements Observer {

    public final static int PWIDTH = 170;
    public final static int PHEIGHT = 50;
    private JLabel title;
    private JTextField note;
    private JScrollPane scroll;
    private JList propPanel;
    private ConnectedModel model;

    class ConnectedModel extends AbstractListModel {

        public int getSize() {
            return Application.getApplication().getNetwork().getConnectedMachine().size();
        }

        public Object getElementAt(int index) {
            IComputer comp = Application.getApplication().getNetwork().getConnectedMachine().get(index);
            URL img = Application.class.getResource("resources/images/" + (comp.isConnected() ? "pastille_verte.png" : "pastille_rouge.png"));

            return "<html> <img width=\"10\" height=\"10\" src=\"" + img + "\">" + comp.getName() + "</html>";
        }

        @Override
        public void fireContentsChanged(Object source, int index0, int index1) {
            super.fireContentsChanged(source, index0, index1);
        }
    }

    public ConnectedComputer() {
        super();
        setName("Connected Computers");
        setOrientation(JToolBar.VERTICAL);

        setSize(new Dimension(PWIDTH + 20, PHEIGHT));

        title = new JLabel("Connected Computer");
        title.setForeground(Color.blue);

        note = new JTextField("Inactive");
        note.setBackground(Color.lightGray);
        note.setForeground(Color.red);
        note.setFont(new Font("times", Font.BOLD, 14));
        model = new ConnectedModel();
        propPanel = new JList(model);
        scroll = new JScrollPane(propPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVisible(true);
        scroll.setEnabled(true);
        add(scroll, BorderLayout.CENTER);

        JPanel panelNorth = new JPanel(new BorderLayout());
        panelNorth.add(title, BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(panelNorth, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        /*SelectionManager.addGeneralSelectionListener(this);
         InnerWindow.addWorkAreaListener(this);*/
        Application.getApplication().getNetwork().addObserver(this);
        revalidate();
    }

    public void update(Observable o, Object arg) {
        model.fireContentsChanged(o, 0, model.getSize());
        this.updateUI();
    }

    @Override
    public String toString() {
        return "Connected Computers";
    }
}

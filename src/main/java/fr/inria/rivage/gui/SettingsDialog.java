package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 20, 2003
 * Time: 4:32:52 PM
 * To change this template use Options | File Templates.
 */
public class SettingsDialog extends JDialog implements ActionListener {
    private Application application;
    private JButton cancel;
    private JButton ok;
    private SettingsPanel settingsPanel;
    //local variable to keep the values selected in the Settings panel
    private JTextField timeInput;
    private String mode;


    public SettingsDialog(Application application) {
        super(application.getMainFrame(), "Settings", false);
        this.application = application;
        this.mode = "";
        //create components
        JPanel southPanel = new JPanel();
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        ok = new JButton("Ok");
        ok.addActionListener(this);
        southPanel.add(ok);
        southPanel.add(cancel);
        //create thesettings panel
        settingsPanel = new SettingsPanel();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(settingsPanel, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
        setSize(300, 180);
        setLocationRelativeTo(application.getMainFrame());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Ok")) {
            if (mode.equals("automatic")) {
                int time = 5;
                try {
                    time = Integer.parseInt(timeInput.getText());
                    if ((time < 5) || (time > 1000)) {
                        JOptionPane.showMessageDialog(application.getMainFrame(),
                                "The number shoul be in tthe range of [ 5 sec,1000 sec ].", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(application.getMainFrame(), "Please enter a number !!!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else if (mode.equals("manual")) {
                //save settings
                //application.setGarbagecollectorOperationMode(Application.MANUAL);
            }
        }
        dispose();
    }

    class SettingsPanel extends JPanel implements ActionListener {
        JRadioButton automatic;
        JRadioButton manual;
        JLabel labelTimeInput;
        JButton clear;

        public SettingsPanel() {
            // Concurrency
            super();
            JLabel label = new JLabel("Garbage collector settings");
            label.setForeground(Color.blue);
            label.setBounds(10, 10, 200, 25);

            automatic = new JRadioButton("Automaic");
            automatic.setActionCommand("automatic");
            automatic.addActionListener(this);
            automatic.setBounds(10, 40, 100, 25);

            manual = new JRadioButton("Manual");
            manual.setActionCommand("manual");
            manual.addActionListener(this);
            manual.setBounds(10, 60, 100, 25);

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(automatic);
            buttonGroup.add(manual);

            clear = new JButton("Clear");
            clear.addActionListener(this);
            clear.setBounds(120, 60, 80, 25);

            labelTimeInput = new JLabel("Update(sec)");
            labelTimeInput.setForeground(Color.blue);
            labelTimeInput.setBounds(120, 40, 90, 25);

           // timeInput = new JTextField(new String(((Application.GARBAGE_COLLECTOR_TIMER / 1000))+""));
            timeInput.setBounds(200, 40, 40, 20);

            /*switch (application.getGarbagecollectorOperationMode()) {
                case Application.MANUAL:
                    mode = "manual";
                    labelTimeInput.setVisible(false);
                    timeInput.setVisible(false);
                    clear.setVisible(true);
                    manual.grabFocus();
                    manual.setSelected(true);
                    break;
                case Application.AUTOMATIC:
                    mode = "automatic";
                    clear.setVisible(false);
                    labelTimeInput.setVisible(true);
                    timeInput.setVisible(true);
                    automatic.setSelected(true);
                    automatic.grabFocus();
                    break;
            }*/

            setLayout(null);
            add(label);
            add(automatic);
            add(manual);
            add(clear);
            add(labelTimeInput);
            add(timeInput);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals("automatic")) {
                clear.setVisible(false);
                labelTimeInput.setVisible(true);
                timeInput.setVisible(true);
                mode = "automatic";
                return;
            } else if (cmd.equals("manual")) {
                clear.setVisible(true);
                clear.grabFocus();
                labelTimeInput.setVisible(false);
                timeInput.setVisible(false);
                mode = "manual";
                return;
            } else if (cmd.equals("Clear")) {

                return;
            }
        }
    }
}

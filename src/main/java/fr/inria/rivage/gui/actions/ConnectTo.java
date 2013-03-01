/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class ConnectTo extends AbstractAction {

    ConnectTo() {
        putValue(AbstractAction.NAME, "Join");
        putValue(AbstractAction.SHORT_DESCRIPTION, "Send join ask");
        putValue(AbstractAction.SMALL_ICON, new ImageIcon(Application.class.getResource("resources/images/join-icon.png")));

    }

    public void actionPerformed(ActionEvent e) {
        JFrame frame=Application.getApplication().getMainFrame();
        String addr = JOptionPane.showInputDialog(frame,
                "Enter the address or the name of computer to connect",
                "Computer address",
                JOptionPane.QUESTION_MESSAGE);
        if (addr != null && !(addr.trim().equals(""))) {
            try {
                Application.getApplication().getNetwork().connectTo(addr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex);
            }
        }
    }
}

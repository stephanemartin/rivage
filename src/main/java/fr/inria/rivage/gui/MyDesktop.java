package fr.inria.rivage.gui;

import java.awt.Color;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;


/**
 * Created by IntelliJ IDEA.
 * User: Lorant Zeno Csaszar
 * Email: lcsaszar@inf.ethz.ch
 * Date: Feb 24, 2003
 * Time: 2:21:06 PM
 */
public class MyDesktop extends JDesktopPane {

    public MyDesktop() {
        setBackground(Color.white);
        setFocusable(true);
    }

    public void addWindow(InnerWindow w) {
        add(w);
        try {
            w.setSelected(true);
            w.setVisible(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

    }
}
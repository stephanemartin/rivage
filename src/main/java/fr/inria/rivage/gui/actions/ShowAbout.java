package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.tools.Configuration;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class ShowAbout extends AbstractAction {

    ShowAbout() {
        this.putValue(AbstractAction.NAME, "About");
        this.putValue(AbstractAction.SHORT_DESCRIPTION,
                "About this Application");
        this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_B);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        URL img2 = Application.class.getResource("resources/images/ethlogo.gif");
        URL img = Application.class.getResource("resources/images/logo_INRIA.png");
        String imagesrc = "<img src=\"" + img + "\">";
        String version = Configuration.VERSION_NUMBER;

        JOptionPane.showMessageDialog(Application.getApplication()
                .getMainFrame(), "<html><center>" + imagesrc
                + "<br> <font size=4 color=red>RIVAGE version "
                + version + "<br>Real-tIme Vector grAphic Group Editor</font>"
                + "<br><p>by </p><p><a href=\"http://www.stephanemartin.fr\">Stéphane Martin</a><br>"
                + "2013 INRIA</p>"
                + "<br><p>Based on GEditor by Globis Team</p>"
                + "<p>Claudia Ignat<br>"
                + "Moira Norrie<br> "
                + "Lorant Csaszar<br> "
                + "Stavroula Papadopoulou<br> "
                + "Tobias Kuhn<br> "
                + "Yves Jacoby</p>"
                + "<p>2003-2005 ETH Zurich<br><img src=\"" + img2 + "\"></font></p></center></html>",
                "Product version", JOptionPane.CLOSED_OPTION, null);
    }
}

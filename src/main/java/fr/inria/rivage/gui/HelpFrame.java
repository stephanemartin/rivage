package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 7, 2003
 * Time: 3:14:34 PM
 * To change this template use Options | File Templates.
 */
public class HelpFrame extends JDialog implements ActionListener{
    private JEditorPane html;

    public HelpFrame() {
        super();
        setTitle("Geditor help");
        try {
            String path = null;
            URL url = null;
            try {
                path = "resources/help/index.html";
                url = Application.class.getResource(path);
            } catch (Exception e) {
                url = null;
            }
            if (url != null) {
                html = new JEditorPane(url);
                html.setEditable(false);
                html.addHyperlinkListener(createHyperLinkListener());

                JScrollPane scroller = new JScrollPane();
                JViewport vp = scroller.getViewport();
                vp.add(html);
                JPanel panel = new JPanel();
                JButton button = new JButton("Close");
                button.addActionListener(this);
                panel.add(button);
                getContentPane().setLayout(new BorderLayout());
                getContentPane().add(panel,BorderLayout.SOUTH);
                getContentPane().add(scroller, BorderLayout.CENTER);
                setSize(700,550);
                setVisible(true);
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

    }

       public HyperlinkListener createHyperLinkListener() {
 	return new HyperlinkListener() {
            @Override
 	    public void hyperlinkUpdate(HyperlinkEvent e) {
 		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
 		    if (e instanceof HTMLFrameHyperlinkEvent) {
 			((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent(
 			    (HTMLFrameHyperlinkEvent)e);
 		    } else {
 			try {
 			    html.setPage(e.getURL());
 			} catch (IOException ioe) {
 			}
 		    }
 		}
 	    }
 	};
     }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}

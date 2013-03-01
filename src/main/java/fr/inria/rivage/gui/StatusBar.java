package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Status bar for the program.
 * @author yves
 *
 */
public class StatusBar extends JPanel {
    private final JLabel label;
    private StatusBarClearer sbc;

    public StatusBar(Application application,MainFrame parent) {
    	label = new JLabel();
    	setLayout(new BorderLayout());
    	add(label,BorderLayout.WEST);
        setPreferredSize(new Dimension(parent.getSize().width, 30));
        sbc = new StatusBarClearer();
        sbc.run();
    }

    public void display(String msg) {
    	sbc.interrupt();
    	sbc = new StatusBarClearer();
        label.setText(msg);
        sbc.start();
    }
    
    class StatusBarClearer extends Thread {
    	
		@Override
		public void run() {
			try{
				sleep(5000);
				label.setText("");
			} catch(InterruptedException ie){
				
			}
		}
    }
}


/*
 * Created on Oct 17, 2004
 */
package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * This is a very specialized version of a progress monitor. Do not try to use
 * it for something else that file opening.
 * 
 * @author Yves
 */
public class FileControllerInitMonitor extends JDialog {

	final JLabel text;

	final JProgressBar bar;

	public static FileControllerInitMonitor getNewMonitor(String filename) {
		return new FileControllerInitMonitor(filename);
	}

	private FileControllerInitMonitor(String filename) {
		super(Application.getApplication().getMainFrame(), "Loading file "
				+ filename, false);

		getContentPane().setLayout(new BorderLayout(5, 5));

		text = new JLabel("Initializing ...");
		text.setPreferredSize(new Dimension(300, 50));
		bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setPreferredSize(new Dimension(300, 30));

		getContentPane().add(text, BorderLayout.NORTH);
		getContentPane().add(bar, BorderLayout.CENTER);

		pack();
	}

	/**
	 * This method changes the text in the dialog. It can also be called from an
	 * outside thread, as it uses invokeLater to set the text.
	 * 
	 * @param newText the new text.
	 */
	public void setText(final String newText) {
		Runnable textsetter = new Runnable() {

            @Override
			public void run() {
				text.setText(newText);
			}

		};
		SwingUtilities.invokeLater(textsetter);
	}

}

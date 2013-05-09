/*
 * Created on Oct 16, 2004
 */
package fr.inria.rivage.engine.concurrency;

import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.tools.Configuration;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * @author Yves
 */
public class ConcurrencyChooser extends JDialog implements ActionListener {
    private static final Logger log = Logger.getLogger(ConcurrencyChooser.class.getName());
    

	JButton btnOK;
	JList lstCC;

	boolean accepted;

	FileController fileController;
	IConcurrencyController concurrencyController;

	public ConcurrencyChooser(JFrame parent, FileController fc) {
		super(parent, "Choose a concurrency controller", true);
		

		getContentPane().setLayout(new BorderLayout(3, 3));

		lstCC = new JList(Configuration.getConfiguration().CONCURRENCY_CONTROLLER_LIST);
		lstCC.setPreferredSize(new Dimension(300,200));
		lstCC.setSelectedIndex(0);
		btnOK = new JButton("OK");

		btnOK.addActionListener(this);

		getContentPane().add(lstCC, BorderLayout.CENTER);
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnPanel.add(btnOK);

		this.fileController = fc;

		getContentPane().add(btnPanel,BorderLayout.SOUTH);

		pack();

	}

	public IConcurrencyController getConcurrencyController() {
		if (accepted) {
			buildConcurrencyController();
			return concurrencyController;
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	private void buildConcurrencyController() {
		String completeName = "geditor.engine.concurrency." + lstCC.getSelectedValue();
		log.log(Level.INFO, "ConcurrencyController chosen: {0}", completeName);
		try {
			Class c = Class.forName(completeName);
			Constructor cons = c.getConstructor(new Class[]{FileController.class});
			concurrencyController = (IConcurrencyController) cons.newInstance(new Object[]{fileController});
		} catch (Exception e) {
			log.log(Level.SEVERE, "An error happened while creating the ConcurrencyController.{0}", e);
			concurrencyController = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOK) {
			accepted = true;
			setVisible(false);
		}
	}

}

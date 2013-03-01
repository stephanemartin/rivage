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
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 * @author Yves
 */
public class ConcurrencyChooser extends JDialog implements ActionListener {

	private Logger log;

	JButton btnOK;
	JList lstCC;

	boolean accepted;

	FileController fileController;
	IConcurrencyController concurrencyController;

	public ConcurrencyChooser(JFrame parent, FileController fc) {
		super(parent, "Choose a concurrency controller", true);
		log = Logger.getLogger(ConcurrencyChooser.class);

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
		log.info("ConcurrencyController chosen: " + completeName);
		try {
			Class c = Class.forName(completeName);
			Constructor cons = c.getConstructor(new Class[]{FileController.class});
			concurrencyController = (IConcurrencyController) cons.newInstance(new Object[]{fileController});
		} catch (Exception e) {
			log.error("An error happened while creating the ConcurrencyController.", e);
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

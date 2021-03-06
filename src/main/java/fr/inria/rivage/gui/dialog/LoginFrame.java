package fr.inria.rivage.gui.dialog;

import fr.inria.rivage.Application;
import fr.inria.rivage.tools.Configuration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: Apr 4, 2003 Time: 3:05:50
 * PM To change this template use Options | File Templates.
 */
public class LoginFrame extends JFrame implements ActionListener {
	private final static int FWIDTH = 350;

	private final static int FHEIGHT = 200;

	private JButton ok;

	private JButton cancel;

	private LoginPanel loginPanel;

	// private SettingsPanel settingsPanel;
	private JPanel backPanel;

	private String userName = "";

	private String password = "";

	public LoginFrame() {
		super("Welcome to GEditor " + Configuration.VERSION_NUMBER);
		setSize(FWIDTH, FHEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// creates panels
		loginPanel = new LoginPanel(this);
		// settingsPanel = new SettingsPanel();
		backPanel = new JPanel();
		backPanel.setLayout(new BorderLayout());
		backPanel.add(loginPanel);
		// create buttons
		ok = new JButton("Ok");
		ok.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		// create south panel
		JPanel southPanel = new JPanel();
		southPanel.add(ok);
		southPanel.add(cancel);

		// init frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(backPanel, BorderLayout.CENTER);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		setVisible(true);
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String[] strings = loginPanel.getValues();
		userName = strings[0];
		password = strings[1];
		if (cmd.equals("Ok")) {
			if (userName.equals("") || userName.startsWith(" "))
				JOptionPane
					.showMessageDialog(
							this,
							"<html><font size=3 color=blue>Wrong username<font size=3 color=red></html>",
							"Input error", JOptionPane.WARNING_MESSAGE);
			else if (password.equals("guest")) {
				setVisible(false);
				//Application.startApplication(userName);
				dispose();
			} else
				JOptionPane
					.showMessageDialog(
							this,
							"<html><font size=3 color=red>Wrong password </font></html>",
							"Input error", JOptionPane.WARNING_MESSAGE);
			loginPanel.resetPasswordField();
			return;
		}
		if (cmd.equals("Cancel")) {
			System.exit(0);
			return;
		}
	}

	class LoginPanel extends JPanel {
		private JTextField username;

		private JPasswordField pass;

		private ImageIcon backImage;

		public LoginPanel(LoginFrame loginFrame) {
			setBackground(Color.white);
			setLayout(null);
			JLabel label1 = new JLabel("Username ");
			label1.setBounds(10, 50, 80, 20);
			JLabel label2 = new JLabel("Password ");
			label2.setBounds(10, 50 + 25, 80, 20);
			username = new JTextField();
			username.setBounds(80, 50, 80, 20);
			pass = new JPasswordField();
			pass.setBounds(80, 50 + 25, 80, 20);
			pass.setActionCommand("Ok");
			pass.addActionListener(loginFrame);
			add(label1);
			add(label2);
			add(username);
			add(pass);
			username.grabFocus();
			backImage = new ImageIcon(Application.class
				.getResource("resources/images/collaborative.gif"));

		}

		public void resetPasswordField() {
			pass.setText("");
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, LoginFrame.FWIDTH, LoginFrame.FHEIGHT);
			g.drawImage(backImage.getImage(), 150, 0, this);
		}

		public String[] getValues() {
			return new String[] { username.getText(),
					String.valueOf(pass.getPassword()) };
		}
	}

}

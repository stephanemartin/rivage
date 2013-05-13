package fr.inria.rivage.gui.dialog;

import fr.inria.rivage.Application;
import fr.inria.rivage.net.queues.InputQueue;
import fr.inria.rivage.net.queues.OutputQueue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

/**
 * This frame allows the users of GEditor to chat with the other users that are
 * registered on the same server.
 * 
 * @author Tobias Kuhn
 */
public class ChatFrame extends JFrame implements ActionListener, WindowListener {
    private static final Logger LOG = Logger.getLogger(ChatFrame.class.getName());


	// the only <code>ChatFrame</code> object
	private static ChatFrame chatFrame;

	// the username and current room
	private String username, room;

	// swing objects
	private JPanel writePanel, messagesPanel, roomPanel, userPanel;

	private JTextArea writeTextArea, messagesTextArea;

	private JList roomList, userList;

	private JButton clearButton, sendButton, newRoomButton, joinRoomButton,
			updateButton;

	// the queue for sending messages
	private OutputQueue out;

	// the thread for receiving messages
	private ReceiveThread receiveThread;

	// all known rooms and all known users in the current room
	private Vector<String> users, rooms;

	// This class describes a broadcast data packet for the chat communication.
	private static class ChatBroadcast implements Serializable {

		// broadcast types
		// For telling all users who you are and in which room you currently
		// stay.
		public static final int INFO = 0;

		// For sending a chat message to be displayed at all the users in the
		// same room.
		public static final int MESSAGE = 1;

		// For indicating that you are a new user in this chat.
		public static final int JOIN_CHAT = 2;

		// For indicating that you are leaving the chat.
		public static final int EXIT_CHAT = 3;

		// For making all other users sending you an info message.
		public static final int PING = 4;

		// For telling the other users that you change the room.
		public static final int JOIN_ROOM = 5;

		// one of the broadcast types above
		public int type;

		// the name of the sending user
		public String user;

		// the current room of the sending user
		public String room;

		// the message text (only for message typed broadcast)
		public String text;

		public ChatBroadcast(int type, String user, String room, String text) {
			this.type = type;
			this.user = user;
			this.room = room;
			this.text = text;
		}

	}

	// This class describes a thread that is waiting for incoming messages and
	// notifies
	// the <code>ChatFrame</code>.
	private static class ReceiveThread extends Thread {

		InputQueue in;

		public ReceiveThread() {
			in = new InputQueue("ChatQueue");
			/*try {
				Application.getApplication().getNetwork()
						.registerInputQueue(in);
			} catch (NetworkException e) {
				log.fatal("Could not register input or output queues.", e);
			}*/
		}

		@Override
		public void run() {

			ChatBroadcast message;

			while (!isInterrupted()) {
				try {
					message = (ChatBroadcast) in.dequeue();
				} catch (InterruptedException ex) {
					continue;
				}
				chatFrame.messageReceived(message);
			}

		}

	}

	private ChatFrame() {

		

		addWindowListener(this);
		setSize(500, 500);
		setLocation(200, 100);
		username = Application.getApplication().getUser().getUserName();
		room = "Main";
		updateTitle();

		writePanel = new JPanel(new BorderLayout());
		writePanel.add(new JLabel("Write Message"), BorderLayout.NORTH);
		writeTextArea = new JTextArea();
		writeTextArea.setLineWrap(true);
		writeTextArea.setWrapStyleWord(true);
		writePanel.add(new JScrollPane(writeTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		JPanel writeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,
				5, 2));
		writeButtonPanel.add(clearButton = new JButton("Clear"));
		clearButton.addActionListener(this);
		writeButtonPanel.add(sendButton = new JButton("Send"));
		sendButton.addActionListener(this);
		writePanel.add(writeButtonPanel, BorderLayout.SOUTH);

		messagesPanel = new JPanel(new BorderLayout());
		messagesPanel.add(new JLabel("Messages"), BorderLayout.NORTH);
		messagesTextArea = new JTextArea();
		messagesTextArea.setLineWrap(true);
		messagesTextArea.setWrapStyleWord(true);
		messagesTextArea.setEnabled(false);
		messagesTextArea.setDisabledTextColor(Color.BLACK);
		messagesPanel.add(new JScrollPane(messagesTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				writePanel, messagesPanel);
		splitPane1.setDividerLocation(100);
		splitPane1.setDividerSize(5);
		splitPane1.setResizeWeight(0);

		roomPanel = new JPanel(new BorderLayout());
		roomPanel.add(new JLabel("Rooms"), BorderLayout.NORTH);
		roomList = new JList(rooms = new Vector<String>());
		rooms.add("Main");
		roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		roomPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);
		JPanel roomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,
				2));
		roomButtonPanel.add(joinRoomButton = new JButton("Join"));
		joinRoomButton.addActionListener(this);
		roomButtonPanel.add(newRoomButton = new JButton("Join New"));
		newRoomButton.addActionListener(this);
		roomPanel.add(roomButtonPanel, BorderLayout.SOUTH);

		userPanel = new JPanel(new BorderLayout());
		userPanel.add(new JLabel("Users"), BorderLayout.NORTH);
		userList = new JList(users = new Vector<String>());
		userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
		JPanel userButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,
				2));
		userButtonPanel.add(updateButton = new JButton("Update"));
		updateButton.addActionListener(this);
		userPanel.add(userButtonPanel, BorderLayout.SOUTH);

		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				roomPanel, userPanel);
		splitPane2.setDividerLocation(250);
		splitPane2.setDividerSize(5);
		splitPane2.setResizeWeight(.5);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				splitPane1, splitPane2);
		splitPane.setDividerLocation(300);
		splitPane.setDividerSize(5);
		splitPane.setResizeWeight(1);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPane, BorderLayout.CENTER);

	}

	/**
	 * Shows the chat frame.
	 */
	public static void showChatFrame() {
		if (chatFrame == null || !chatFrame.isVisible()) {
			chatFrame = new ChatFrame();
			chatFrame.init();
		} else {
			chatFrame.setVisible(true);
		}
	}

	// Initializes the network and makes the frame visible.
	private void init() {

		out = new OutputQueue("ChatQueue");

		/*try {
			//Application.getApplication().getNetwork().registerOutputQueue(out);
		} catch (NetworkException e) {
			log.fatal("Could not register input or output queues.", e);
		}*/

		receiveThread = new ReceiveThread();
		receiveThread.start();

		out.enqueue(new ChatBroadcast(ChatBroadcast.JOIN_CHAT, username,
				"Main", ""));

		setVisible(true);

	}

	// Gets called by the <code>ReceiveThread</code> object when a new message
	// has come.
	private void messageReceived(ChatBroadcast message) {

		// Make sure that the room is on the rooms list.
		if (!rooms.contains(message.room)) {
			rooms.add(message.room);
			roomList.setListData(rooms);
		}

		// Make sure that the user is on the users list if he is in the same
		// room.
		if (message.room.equals(room) && !users.contains(message.user)) {
			users.add(message.user);
			userList.setListData(users);
		}

		switch (message.type) {

		case ChatBroadcast.MESSAGE:
			// print the text if it's the right room
			if (message.room.equals(room))
				printText("<" + message.user + "> " + message.text);
			break;

		case ChatBroadcast.JOIN_CHAT:
			// send info message for the new user
			out.enqueue(new ChatBroadcast(ChatBroadcast.INFO, username, room,
					""));
			if (message.room.equals(room))
				printText(">>> " + message.user + " is joining the chat.");
			break;

		case ChatBroadcast.EXIT_CHAT:
			if (message.room.equals(room)) {
				// remove exited user
				users.remove(message.user);
				userList.setListData(users);
				printText(">>> " + message.user + " has left the chat.");
			}
			break;

		case ChatBroadcast.PING:
			out.enqueue(new ChatBroadcast(ChatBroadcast.INFO, username, room,
					""));
			break;

		case ChatBroadcast.JOIN_ROOM:
			if (!message.room.equals(room) && users.contains(message.user)) {
				// another user has left the current room
				users.remove(message.user);
				userList.setListData(users);
				printText(">>> " + message.user + " is gone to the room "
						+ message.room + ".");
			} else if (message.room.equals(room)) {
				// a user has entered the current room
				if (message.user.equals(username)) {
					printText(">>> You entered the room " + room + ".");
				} else {
					printText(">>> " + message.user + " entered this room.");
				}
				// send info message for let the entered user know who is in
				// here
				out.enqueue(new ChatBroadcast(ChatBroadcast.INFO, username,
						room, ""));
			}
			break;

		}

	}

	// Prints the given text at the end of the message history.
	private void printText(String text) {
		messagesTextArea.setText(messagesTextArea.getText() + "\n\n" + text);
		messagesTextArea.setCaretPosition(messagesTextArea.getText().length());
	}

	// Sets the title with the username and the current room.
	private void updateTitle() {
		setTitle("Chat: <" + username + "> in room <" + room + ">");
	}

	// Clears the users list.
	private void clearUsers() {
		users = new Vector<String>();
		userList.setListData(users);
	}

	// Clears the rooms list.
	private void clearRooms() {
		rooms = new Vector<String>();
		rooms.add("Main");
		roomList.setListData(rooms);
	}

    @Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == clearButton) {

			writeTextArea.setText("");

		} else if (e.getSource() == sendButton) {

			if (writeTextArea.getText().equals(""))
				return;
			// send chat message to all users
			out.enqueue(new ChatBroadcast(ChatBroadcast.MESSAGE, username,
					room, writeTextArea.getText()));
			writeTextArea.setText("");

		} else if (e.getSource() == newRoomButton) {

			String name = JOptionPane.showInputDialog(this,
					"Enter the name for the new room:", "New Room",
					JOptionPane.QUESTION_MESSAGE);
			if (name == null || name.equals(""))
				return;
			if (rooms.contains(name)) {
				JOptionPane.showMessageDialog(this,
						"This room already exists.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			room = name;
			clearUsers();
			updateTitle();
			out.enqueue(new ChatBroadcast(ChatBroadcast.JOIN_ROOM, username,
					room, ""));

		} else if (e.getSource() == joinRoomButton) {

			Object select = roomList.getSelectedValue();
			if (select == null)
				return;
			if (((String) select).equals(room))
				return;
			room = (String) select;
			clearUsers();
			updateTitle();
			out.enqueue(new ChatBroadcast(ChatBroadcast.JOIN_ROOM, username,
					room, ""));

		} else if (e.getSource() == updateButton) {

			clearUsers();
			clearRooms();
			out.enqueue(new ChatBroadcast(ChatBroadcast.PING, username, room,
					""));

		}

	}

    @Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		receiveThread.interrupt();
		out.enqueue(new ChatBroadcast(ChatBroadcast.EXIT_CHAT, username, room,
				""));
	}

    @Override
	public void windowActivated(WindowEvent e) {
	}

    @Override
	public void windowClosed(WindowEvent e) {
	}

    @Override
	public void windowDeactivated(WindowEvent e) {
	}

    @Override
	public void windowDeiconified(WindowEvent e) {
	}

    @Override
	public void windowIconified(WindowEvent e) {
	}

    @Override
	public void windowOpened(WindowEvent e) {
	}

}

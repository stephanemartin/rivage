package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.users.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class UsersToolbar extends JToolBar {
	//status
	public final static int ACTIVATED = 10;
	public final static int DEACTIVATED = 11;

	public final static int UWIDTH = 150;
	public final static int UHEIGHT = 150;

	protected Application application;
	protected JList list;
	protected JLabel title;
	protected JScrollPane scroll;

	public UsersToolbar(Application application) {
		super();
		setName("User list");
		setOrientation(JToolBar.VERTICAL);
		this.application = application;
		setPreferredSize(new Dimension(UWIDTH, UHEIGHT));
		//create label
		title = new JLabel("User List");
		title.setForeground(Color.blue);
		//init
		list = new JList();
		scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setLayout(new BorderLayout());
		add(title, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
	}
	
	public void setUserList(ArrayList<User> userList){
		if(userList == null){
			list.removeAll();
		} else {
			list.setListData(userList.toArray());
		}
	}
	
	public void addUserToList(User user){
		ArrayList<User> newlist = new ArrayList<User>();
		for(int i = 0 ; i<list.getModel().getSize();i++){
			newlist.add((User) list.getModel().getElementAt(i));
		}
		newlist.add(user);
		list.setListData(newlist.toArray());
	}
}
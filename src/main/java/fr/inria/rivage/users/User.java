package fr.inria.rivage.users;

import fr.inria.rivage.Application;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class User implements Serializable{

    private String username;
    private String userID;
    private ImageIcon image;

    public User(String username) {
        this.username = username;
        this.userID = username;
        this.image = new ImageIcon(Application.class.getResource("resources/images/file.gif"));
    }

    public String getUserName() {
        return username;
    }

    public String getUserID() {
        return username;
    }

    public ImageIcon getImage() {
        return image;
    }
	
	@Override
	public String toString() {
		return username;
	}
}

/*
 * Created on Aug 27, 2004
 */
package fr.inria.rivage.engine.manager.groupcontroller;

import fr.inria.rivage.users.User;
import java.io.Serializable;

/**
 * This is the packet that should be used by the GroupController to tell the
 * others in a group that the site with this siteID want to join the group.
 * 
 * @author Yves
 */
public class JoinRequestPacket implements Serializable {
	private long siteID;
	private String fileID;
	private User user;

	public JoinRequestPacket(long siteID, String fileID, User user) {
		this.fileID = fileID;
		this.siteID = siteID;
		this.user = user;
	}

	public long getSiteID() {
		return siteID;
	}

	public String getFileID() {
		return fileID;
	}
	public User getUser() {
		return user;
	}
}

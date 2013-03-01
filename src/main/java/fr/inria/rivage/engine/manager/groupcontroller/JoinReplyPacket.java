/*
 * Created on Sep 20, 2004
 */
package fr.inria.rivage.engine.manager.groupcontroller;

import fr.inria.rivage.users.User;
import java.io.Serializable;

/**
 * @author Yves
 */
public class JoinReplyPacket implements Serializable {
	private long fromSiteID;

	private long toSiteID;

	private String fileID;

	private long[] knownSites;

	private User user;

	private String ccName;

	private Serializable ccProps;

	/**
	 * Constructs a JoinReplyPacket. This packet is send after and in response
	 * to a JoinRequestPackage.
	 * 
	 * @param fromSiteID the site from which the packet is send
	 * @param toSiteID the site to which the packet is send
	 * @param fileID the file ID
	 * @param knownSites all the known sites in an array for the specific fileID
	 * @param user the current user on this site
	 * @param ccName the name of the concurrency controller class used
	 * @param ccProps the properties set on the current concurrency controller
	 */
	public JoinReplyPacket(long fromSiteID, long toSiteID, String fileID,
			long[] knownSites, User user, String ccName, Serializable ccProps) {
		this.fileID = fileID;
		this.fromSiteID = fromSiteID;
		this.toSiteID = toSiteID;
		this.knownSites = knownSites;
		this.user = user;
		this.ccName = ccName;
		this.ccProps = ccProps;
	}

	/**
	 * Returns the site from which this packet was send.
	 * 
	 * @return the siteID of the site which send this packet.
	 */
	public long getFromSiteID() {
		return fromSiteID;
	}

	/**
	 * The fileID
	 * 
	 * @return the file ID
	 */
	public String getFileID() {
		return fileID;
	}

	/**
	 * An array containing all the IDs of the known sites for the specific
	 * fileID
	 * 
	 * @return
	 */
	public long[] getKnownSites() {
		return knownSites;
	}

	/**
	 * Returns the destination site of this packet
	 * 
	 * @return the ID of the destination site
	 */
	public long getToSiteID() {
		return toSiteID;
	}

	/**
	 * Returns the object symbolizing the remote user.
	 * 
	 * @return the remote user's object
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Name of the concurrency controller used on the remote site.
	 * 
	 * @return the name of the concurrencycontroller class
	 */
	public String getCcName() {
		return ccName;
	}

	/**
	 * Properties of the concurrency controller on the remote site.
	 * 
	 * @return the properties.
	 */
	public Serializable getCcProps() {
		return ccProps;
	}
}

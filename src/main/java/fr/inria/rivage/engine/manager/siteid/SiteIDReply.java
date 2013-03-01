/*
 * Created on Sep 2, 2004
 */
package fr.inria.rivage.engine.manager.siteid;

import java.io.Serializable;

/**
 * Message to reply to someone an give him information about the current largest siteID.
 * @author Yves
 */
public class SiteIDReply implements Serializable {
	public byte[] myRandom;
	public byte[] targetRandom;
	public long siteID;
	public long maxKnownID;

	public SiteIDReply(long siteID, long maxKnownID, byte[] myRandom, byte[] targetRandom) {
		this.siteID = siteID;
		this.maxKnownID = maxKnownID;
		this.myRandom = myRandom;
		this.targetRandom = targetRandom;
	}
}

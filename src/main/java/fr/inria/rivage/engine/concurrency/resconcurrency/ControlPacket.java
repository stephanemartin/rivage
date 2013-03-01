package fr.inria.rivage.engine.concurrency.resconcurrency;

import java.io.Serializable;

public class ControlPacket implements Serializable {

	private long siteID;

	/*
	 * COUNT is send when the sites should just send back their IDs. PAUSE is
	 * send when the sites should be paused. START is to be send when the sites
	 * can restart.
	 */
	public static enum Type {
		COUNT, PAUSE, START, INFO, NONE, JOIN, QUIT;
	};

	private Type type;
	private Type inResponse;
	
	public ControlPacket(long siteID, Type type, Type inResponse) {
		this.type = type;
		this.siteID = siteID;
		this.inResponse = inResponse;
	}

	public Type getType() {
		return type;
	}

	public long getSiteID() {
		return siteID;
	}

	public Type getInResponse() {
		return inResponse;
	}
	
	

}

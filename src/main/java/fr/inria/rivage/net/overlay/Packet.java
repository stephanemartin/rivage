/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.overlay;

import java.io.Serializable;

/**
 * A Packet contains a queue ID, which represents the queue the packet was send from and also the ones
 * it should be delivered to and eventually a siteID, to identify the site it was send from.
 * @author Yves
 */
public class Packet implements Serializable{
	
	String queuename;
	long siteID;
	Serializable data;
	
	public Packet(String queuename, long siteID, Serializable data){
		this.queuename = queuename;
		this.data = data;
		this.siteID = siteID;
	}
	
	public String getQueueName() {
		return queuename;
	}
	public void setQueueName(String queueName) {
		queuename = queueName;
	}
	public Serializable getData() {
		return data;
	}
	public void setData(Serializable data) {
		this.data = data;
	}
	public long getSiteID() {
		return siteID;
	}
	public void setSiteID(long siteID) {
		this.siteID = siteID;
	}
}

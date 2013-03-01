/*
 * Created on Sep 2, 2004
 */
package fr.inria.rivage.engine.manager.siteid;

import java.io.Serializable;

/**
 * Message to ask the other in the network for a siteID.
 * @author Yves
 */
public class SiteIDDemand implements Serializable {
	public byte[] random;

	public SiteIDDemand(byte[] random) {
		this.random = random;
	}
}

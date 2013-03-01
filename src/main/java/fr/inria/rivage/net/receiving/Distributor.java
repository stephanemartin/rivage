/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.receiving;

/**
 * @author Yves
 */
public class Distributor extends Thread {

	/*BlockingQueue q;

	Hashtable<String, InputQueue> registeredqueues;

	Logger log;

	MulticastNetwork multicastNet;

	public Distributor(MulticastNetwork multicastNet, BlockingQueue q) {
		this.q = q;
		this.registeredqueues = new Hashtable<String, InputQueue>();
		this.multicastNet = multicastNet;
		log = Logger.getLogger(Distributor.class);
		log.debug("Distributor initialized.");
	}

	@SuppressWarnings("unchecked")
	public void registerInputQueues(InputQueue q) throws NetworkException {
		synchronized (registeredqueues) {
			if (registeredqueues.get(q.getName()) != null) {
				throw new NetworkException("A queue with name \"" + q.getName()
						+ "\" is already registered.");
			}
			registeredqueues.put(q.getName(), q);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Packet p = null;
		String target;
		InputQueue inq;
		while (!isInterrupted()) {
			try {
				log.debug("Waiting for message from receiver.");
				p = q.dequeue();
			} catch (InterruptedException ex) {
				log.error("Was interrupted while waiting.", ex);
			}
			if (p != null) {
				target = p.getQueueName();
				synchronized (registeredqueues) {
					inq = registeredqueues.get(target);
				}

				if (inq != null) {
					if (!(inq.getRefuseLocal() && p.getSiteID() == multicastNet
							.getSiteID())) {
						inq.enqueue(p.getData());
					}
				}
				p = null;
			}
		}
	}*/
}
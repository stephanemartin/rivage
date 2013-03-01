/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.sending;

/**
 * @author Yves
 */
public class Collector extends Thread {
	/*Semaphore sem;

	BlockingQueue bq;

	LinkedList<OutputQueue> queuelist;

	Logger log;

	MulticastNetwork multicastNet;

	public Collector(MulticastNetwork multicastNet, BlockingQueue bq) {
		log = Logger.getLogger(Collector.class);
		this.bq = bq;
		this.sem = new Semaphore();
		this.queuelist = new LinkedList<OutputQueue>();
		this.multicastNet = multicastNet;

		log.debug("Collector initialized.");
	}

	public void registerOutputQueues(OutputQueue q) throws NetworkException {
		q.setCounter(sem);
		synchronized (queuelist) {
			if (queuelist.contains(q)) {
				throw new NetworkException("A queue with name \"" + q.getName()
						+ "\" is already registered.");
			}
			queuelist.add(q);
		}
	}

	@Override
	public void run() {
		Iterator<OutputQueue> i;
		OutputQueue oq;
		Serializable data;
		Packet tmp;
		boolean found = false;
		while (!isInterrupted()) {
			sem.decrease();
			found = false;
			synchronized (queuelist) {
				i = queuelist.iterator();
				while (!found && i.hasNext()) {
					oq = (OutputQueue) i.next();
					data = oq.dequeue();

					if (data != null) {

						if ((!(data instanceof OpWrapper) /*|| (data instanceof OpWrapper && ((OpWrapper) data)
								.getOperation() instanceof CreateOperation)*)) {

						} else {
							try {
								sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						log.debug("New Object dequeued. passing to sender."
								+ data.toString());
						tmp = new Packet(oq.getName(),
								multicastNet.getSiteID(), data);
						bq.enqueue(tmp);
						found = true;
					}
				}
			}
		}
	}*/
}
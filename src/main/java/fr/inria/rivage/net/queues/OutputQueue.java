/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.queues;

import fr.inria.rivage.net.overlay.Semaphore;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Output queue should be used before the collector. It automatically sends a
 * signal to the collector when it received something. Collector should support
 * multiple of these queues.
 * 
 * @author Yves
 */
public class OutputQueue {

	private LinkedList<Serializable> queue;
	private String name;
	private Semaphore semaphore;

	public OutputQueue(String name) {
		this.name = name;
		this.queue = new LinkedList<Serializable>();
	}

	public synchronized void enqueue(Serializable p) {
		queue.addLast(p);
		semaphore.increase();
	}

	/**
	 * This method is non-blocking, as we may have to check more than one queue.
	 * If the queue contains something, we return it, else, the return value is
	 * <code>null</code>
	 */
	public synchronized Serializable dequeue() {
		if (queue.size() > 0)
			return (Serializable) queue.removeFirst();
		return null;
	}

	/**
	 * This method should be used prior to any usage of the queue. To set the
	 * semaphore right. This method should only be used by the collector.
	 * 
	 * @param s
	 *            the semaphore that should be increased on case of a packet
	 *            added to the queue and decreased if one is removed.
	 */
	public synchronized void setCounter(Semaphore s) {
		this.semaphore = s;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
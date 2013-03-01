/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.queues;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This is the queue that is used to get messages. When a message arrives on the
 * network, it is automatically enqueud here and can be read by any program.
 * 
 * @author Yves
 */
final public class InputQueue<T extends Serializable> {

	private String name;

	private LinkedList<T> queue;

	private boolean refuseLocal;

	/**
	 * Creates an input queue with the given name.
	 * 
	 * @param name the name of the new queue.
	 */
	public InputQueue(String name) {
		this(name, false);
	}

	/**
	 * Creates an input queue with the given name. Makes it possible to refuse
	 * packets send from local queue.
	 * 
	 * @param name the name of the queue.
	 * @param refuseLocal if <code>true</code> the queue will refuse packets
	 *           send from itself.
	 */
	public InputQueue(String name, boolean refuseLocal) {
		this.name = name;
		this.refuseLocal = refuseLocal;
		queue = new LinkedList<T>();
	}

	/**
	 * Puts a packet in the queue.
	 * 
	 * @param p the packet to put in the queue.
	 */
	public synchronized void enqueue(T p) {
		queue.addLast(p);
		notify();
	}

	/**
	 * Takes a packet out of the queue. Waits forever if none is received.
	 * 
	 * @return the dequeued packet
	 * @throws InterruptedException if the thred is interrupted.
	 */
	public synchronized T dequeue() throws InterruptedException {
		while (queue.size() <= 0)
			wait();
		return queue.removeFirst();
	}

	/**
	 * Blocks until an object can be dequeued or the timeout arrives.
	 * 
	 * @param millis the number of milliseconds before timeout
	 * @return the dequeued object.
	 * @throws InterruptedException happens if nothing could be dequeued or the
	 *             thread is interrupted.
	 */
	public synchronized T dequeue(long millis)
			throws InterruptedException {
		while (queue.size() <= 0) {
			wait(millis);
			if (queue.size() <= 0)
				throw new InterruptedException();
		}
		return queue.removeFirst();
	}

	/**
	 * Returns the name of the queue.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Tells if the queue rejects or accepts local packages.
	 * 
	 * @return true is it rejects, false if it accepts
	 */
	public boolean getRefuseLocal() {
		return refuseLocal;
	}
}

/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.queues;

import fr.inria.rivage.net.overlay.Packet;
import java.util.LinkedList;

/**
 * This is a queue that should be able to grow infinitely, so never blocking when writing, but it will eventually block when empty and some thread
 * wants to read. 
 * @author Yves
 */
public class BlockingQueue {

	private LinkedList<Packet> queue;
	
	public BlockingQueue(){
		queue = new LinkedList<Packet>();
	}
	
	public synchronized void enqueue(Packet p){
		queue.addLast(p);
		notify();
	}
	
	public synchronized Packet dequeue() throws InterruptedException{
		while(queue.size()<=0) wait();
		return (Packet) queue.removeFirst();
	}
	
}

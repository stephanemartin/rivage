/*
 * Created on Oct 19, 2004
 */
package fr.inria.rivage.engine.concurrency.noconcurrencycontroller;

/**
 * @author Yves
 */
public class Semaphore {
	int count;
	
	public Semaphore(){
		count = 0;
	}

	public synchronized void increase(){
		count++;
		notify();
	}
	
	public synchronized void decrease(){
		while(count<=0)
			try {
				wait();
			} catch (InterruptedException e) {
				/**
				 * ???
				 */
			}
		count--;
	}
	
	/**
	 * This should never be used to do anything. It's only to give an idea of how full the queue is.
	 * @return
	 */
	public synchronized int getValue(){
		return count;
	}
	
}

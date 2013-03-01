/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.overlay;

/**
 * This is a pseudo semaphore as it doesn't block from overflow.
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

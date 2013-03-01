/*
 * Created on Oct 20, 2004
 */
package fr.inria.rivage.engine.concurrency.utils;

import java.util.LinkedList;

/**
 * @author Yves
 */
public class BlockingQueue<T> {

    private LinkedList<T> queue;

    public BlockingQueue() {
        queue = new LinkedList<T>();
    }

    public synchronized void enqueue(T p) {
        queue.addLast(p);
        notify();
    }

    public synchronized void insertFront(T p) {
        queue.addFirst(p);
        notify();
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.size() <= 0) {
            wait();
        }
        return queue.removeFirst();
    }
}

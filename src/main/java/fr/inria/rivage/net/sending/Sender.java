/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.sending;

import fr.inria.rivage.net.queues.BlockingQueue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import org.apache.log4j.Logger;

/**
 * @author Yves
 */
public class Sender extends Thread {

	BlockingQueue queue;
	MulticastSocket multisock;
	int port;
	String address;
	Logger log;

	public Sender(String address, int port, BlockingQueue bq) throws IOException {
		super("Sender Thread");
		log = Logger.getLogger(Sender.class);
		
		this.queue = bq;
		this.port = port;
		this.address = address;
		multisock = new MulticastSocket(port);
		multisock.joinGroup(InetAddress.getByName(address));
		
		log.debug("Sender initialized.");
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			log.debug("Waiting for packet in queue.");
			oos.writeObject(queue.dequeue());
			oos.flush();

			log.debug("Building packet.");
			DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
					InetAddress.getByName(address), port);

			multisock.send(p);
			log.debug("Packet sent.");
			} catch (Exception e){
				log.error("An error happened while sending.",e);
			}
		}
	}

}
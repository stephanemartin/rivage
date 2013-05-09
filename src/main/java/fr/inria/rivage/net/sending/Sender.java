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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yves
 */
public class Sender extends Thread {

	BlockingQueue queue;
	MulticastSocket multisock;
	int port;
	String address;
    private static final Logger log = Logger.getLogger(Sender.class.getName());

        
	public Sender(String address, int port, BlockingQueue bq) throws IOException {
		super("Sender Thread");
		
		this.queue = bq;
		this.port = port;
		this.address = address;
		multisock = new MulticastSocket(port);
		multisock.joinGroup(InetAddress.getByName(address));
		
		log.info("Sender initialized.");
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			log.info("Waiting for packet in queue.");
			oos.writeObject(queue.dequeue());
			oos.flush();

			log.info("Building packet.");
			DatagramPacket p = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
					InetAddress.getByName(address), port);

			multisock.send(p);
			log.info("Packet sent.");
			} catch (Exception e){
				log.log(Level.SEVERE, "An error happened while sending.{0}", e);
			}
		}
	}

}
/*
 * Created on Jul 13, 2004
 */
package fr.inria.rivage.net.receiving;

import fr.inria.rivage.net.overlay.Packet;
import fr.inria.rivage.net.queues.BlockingQueue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import org.apache.log4j.Logger;

/**
 * The thread which receives the multicast packets.
 * 
 * @author Yves
 */
public class Receiver extends Thread {

	BlockingQueue q;
	MulticastSocket multisock;
	Logger log;

	/**
	 * Constructs a receiver thread.
	 * 
	 * @param q
	 *            the BlockingQueue the packets should be passed on too.
	 * @throws IOException
	 *             may be throws if que socket can't be initialized.
	 */
	public Receiver(String address, int port, BlockingQueue q) throws IOException {
		this.q = q;
		multisock = new MulticastSocket(port);
		multisock.joinGroup(InetAddress.getByName(address));
		log = Logger.getLogger(Receiver.class);
		log.debug("Receiver initialized.");
	}
	
	@Override
	public void run() {
		ObjectInputStream ois;
		while (!isInterrupted()) {
			try {
				DatagramPacket p = new DatagramPacket(new byte[65535], 65535);

				log.debug("Waiting for packet.");
				multisock.receive(p);

				ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));

				try {
					q.enqueue((Packet) ois.readObject());
					log.debug("Packet Dispatched.");
				} catch (ClassNotFoundException cnfe) {
					/**
					 * We ignore this type of exceptions. If something like this
					 * happen, ... we couldn't do anything else anyway.
					 */
					log.error("The packet we got was not valid.", cnfe);
				}

			} catch (IOException ex) {
				log.error("Strange exception, analyze log.",ex);
			}
		}
	}
}
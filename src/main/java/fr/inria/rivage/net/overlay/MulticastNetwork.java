/*
 * Created on Jul 14, 2004
 */
package fr.inria.rivage.net.overlay;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SiteIDThread;
import fr.inria.rivage.net.group.Message;
import fr.inria.rivage.net.queues.BlockingQueue;
import fr.inria.rivage.net.queues.InputQueue;
import fr.inria.rivage.net.queues.OutputQueue;
import fr.inria.rivage.net.receiving.Distributor;
import fr.inria.rivage.net.receiving.Receiver;
import fr.inria.rivage.net.sending.Collector;
import fr.inria.rivage.net.sending.Sender;
import java.io.IOException;
import java.util.List;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yves
 */
public class MulticastNetwork implements IOverlay {
    private static final Logger LOG = Logger.getLogger(MulticastNetwork.class.getName());

     
	Receiver receiver;
	Distributor distributor;
	BlockingQueue inputqueue;

	Collector collector;
	Sender sender;
	BlockingQueue outputqueue;
	
	SiteIDThread siteIDThread;

	boolean running;
	/**
	 * Initalizes the whole networking block for multicast networking.
	 * 
	 * @throws IOException
	 *             this exception is just thrown to tell you that the network
	 *             doesn't work, but no need to handle it for output, this was
	 *             already done.
	 */
	/*public MulticastNetwork() throws IOException {

		log = Logger.getLogger(MulticastNetwork.class);
		
		/**
		 * Initialize the receiving part.
		 *
		try {
			log.debug("Starting initialization of receiver part.");
			inputqueue = new BlockingQueue();
			receiver = new Receiver(Configuration.getConfiguration().MULTICAST_ADDRESS, Configuration.getConfiguration().MULTICAST_PORT, inputqueue);
			distributor = new Distributor(this, inputqueue);
			log.debug("Receiver part successfully initialized.");
		} catch (IOException ioe) {
			log.fatal("The receiver part could not be initialized.",ioe);
			throw ioe;
		}

		/**
		 * Initializes sender part.
		 *
		try {
			log.debug("Starting initialization of sender part.");
			outputqueue = new BlockingQueue();
			collector = new Collector(this, outputqueue);
			sender = new Sender(Configuration.getConfiguration().MULTICAST_ADDRESS, Configuration.getConfiguration().MULTICAST_PORT, outputqueue);
			log.debug("Sender part successfully initialized.");
		} catch (IOException ioe) {
			log.fatal("The sender part could not be initialized.",ioe);
			throw ioe;
		}
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see geditor.net.IOverlay#registerInputQueues(geditor.net.queues.InputQueue)
	 */
   /* @Override
	public void registerInputQueue(InputQueue q) throws NetworkException {
		distributor.registerInputQueues(q);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see geditor.net.IOverlay#registerOutputQueues(geditor.net.queues.OutputQueue)
	 */
/*    @Override
	public void registerOutputQueue(OutputQueue q) throws NetworkException {
		collector.registerOutputQueues(q);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see geditor.net.IOverlay#start()
	 */
    @Override
	public void start() {
		siteIDThread = SiteIDThread.getSiteThread(this);
		
		distributor.start();
		receiver.start();
		sender.start();
		collector.start();
		
		siteIDThread.start();
		
		running = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see geditor.net.IOverlay#stop()
	 */
    @Override
	public void stop() {
		try {
			receiver.interrupt();
			receiver.join();
			collector.interrupt();
			collector.join();
			distributor.interrupt();
			distributor.join();
			sender.interrupt();
			sender.join();
		} catch (InterruptedException ie) {
			LOG.log(Level.SEVERE, "Bad Bad Bad I was interrupted while shutting down networking services.{0}", ie);
		}
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see geditor.net.IOverlay#isRunning()
	 */
    @Override
	public boolean isRunning() {
		return running;
	}

    

   

    public void connectToMachine(IComputer m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<IComputer> getConnectedMachine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputQueue getInputQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OutputQueue getOutputQueue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void changeName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    public List<? extends IComputer> getknownMachine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void askDocument(ID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendToAll(Message mess) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void discovery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void informNewFile(FileController fc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteObserver(Observer o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IComputer getMe() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addMachine(IComputer computer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void notifyByComputer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String addSlaveMachine(IComputer computer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void connectTo(String addr) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
}
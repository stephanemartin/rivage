/*
 * Created on Oct 20, 2004
 */
package fr.inria.rivage.engine.concurrency.noconcurrencycontroller;

import fr.inria.rivage.engine.concurrency.utils.BlockingQueue;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.engine.operations.UnableToApplyException;
import fr.inria.rivage.net.queues.OutputQueue;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yves
 */
public class MainThread extends Thread {

	/*
	 * TODO NoConcurrencyController undo is not send.
	 */
	
	private BlockingQueue<OpObjectGen.OpPack> packetqueue;
	private FileController fileController;

	private OutputQueue output;
	
	private ArrayList<Operation> localOps;
	private ArrayList<Operation> globalOps;
    private static final Logger log = Logger.getLogger(MainThread.class.getName());
	
	
	public MainThread(FileController fileController, BlockingQueue<OpObjectGen.OpPack> packetqueue) {
		this.fileController = fileController;
		this.packetqueue = packetqueue;
		this.output = new OutputQueue("Concurrency"+fileController.getId());
		/*try {
			Application.getApplication().getNetwork().registerOutputQueue(output);
		} catch (NetworkException e) {
			log.fatal("Could not register input or output queues.",e);
		}
		*/
		localOps = new ArrayList<Operation>();
		globalOps = new ArrayList<Operation>();
	}
	
	@Override
	public void run() {
		while (!interrupted()) {
			try {
				OpObjectGen.OpPack oppack = packetqueue.dequeue();
				switch (oppack.getType()) {
					case OpObjectGen.OpPack.GLOBAL :
						globalhandle((OpObjectGen.GlobalOperation)oppack);
						break;
					case OpObjectGen.OpPack.LOCAL :
						localhandle((OpObjectGen.LocalOperation)oppack);
						break;
					case OpObjectGen.OpPack.UNDOGLOBAL :
						undoglobalhandle((OpObjectGen.UndoGlobalOperation)oppack);
						break;
					case OpObjectGen.OpPack.UNDOLOCAL :
						undolocalhandle((OpObjectGen.UndoLocalOperation)oppack);
						break;
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, "The main thread was interrupted with this exception.{0}", e);
			}
		}
	}

	private void globalhandle(OpObjectGen.GlobalOperation pack) {
		try {
			pack.getOperation().apply(fileController);
			globalOps.add(pack.getOperation());
			pack.getOperation().setApplied(true);
		} catch (UnableToApplyException e) {
			e.printStackTrace();
		}
	}

	private void localhandle(OpObjectGen.LocalOperation pack) {
		try {
			output.enqueue((Operation) pack.getOperation().clone());
			pack.getOperation().apply(fileController);
			localOps.add(pack.getOperation());
			pack.getOperation().setApplied(true);
		} catch (UnableToApplyException e) {
			e.printStackTrace();
		}
	}

	private void undolocalhandle(OpObjectGen.UndoLocalOperation pack) {

		Operation op = (Operation) localOps.get(localOps.size()-1).clone();
		try {
			op.apply(fileController);
			op.setApplied(false);
		} catch (UnableToApplyException e) {
			e.printStackTrace();
		}
	}

	private void undoglobalhandle(OpObjectGen.UndoGlobalOperation pack) {
		Operation op = (Operation) globalOps.get(globalOps.size()-1).clone();
		try {
			op.apply(fileController);
			op.setApplied(false);
		} catch (UnableToApplyException e) {
			e.printStackTrace();
		}
	}

}

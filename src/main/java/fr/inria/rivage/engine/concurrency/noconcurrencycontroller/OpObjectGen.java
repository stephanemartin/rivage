/*
 * Created on Oct 20, 2004
 */
package fr.inria.rivage.engine.concurrency.noconcurrencycontroller;

import fr.inria.rivage.engine.operations.Operation;

/**
 * This is a factory class for the operation. operations need to be wrapped in
 * order to be send to the concurrency controller.
 * 
 * @author Yves
 */
public class OpObjectGen {

	public OpObjectGen() {

	}

	public synchronized OpPack getLocalOperationPack(Operation op) {
		return new LocalOperation(op);
	}

	public synchronized OpPack getGlobalOperationPack(Operation op) {
		return new GlobalOperation(op);
	}

	public synchronized OpPack getUndoLocalOperationPack() {
		return new UndoLocalOperation();
	}

	public synchronized OpPack getUndoGlobalOperationPack() {
		return new UndoGlobalOperation();
	}
	
	abstract public static class OpPack {
		public final static int UNDOLOCAL = 1;
		public final static int UNDOGLOBAL = 2;
		public final static int LOCAL = 3;
		public final static int GLOBAL = 4;

		/**
		 * Returns the type of the packaged operation.
		 * @return the operation's type.
		 */
		abstract public int getType();
	}

	public static class UndoGlobalOperation extends OpPack {

		/*
		 * (non-Javadoc)
		 * 
		 * @see geditor.engine.concurrency.noconcurrencycontroller.OpObjectGen.OpPack#getType()
		 */	
		@Override
		public int getType() {
			return UNDOGLOBAL;
		}

	}

	public static class UndoLocalOperation extends OpPack {

		/*
		 * (non-Javadoc)
		 * 
		 * @see geditor.engine.concurrency.noconcurrencycontroller.OpObjectGen.OpPack#getType()
		 */	
		@Override
		public int getType() {
			return UNDOLOCAL;
		}

	}

	public static class LocalOperation extends OpPack {
		private Operation op;

		public LocalOperation(Operation op) {
			this.op = op;
		}

		public Operation getOperation() {
			return op;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see geditor.engine.concurrency.noconcurrencycontroller.OpObjectGen.OpPack#getType()
		 */	
		@Override
		public int getType() {
			return LOCAL;
		}
	}

	public static class GlobalOperation extends OpPack {
		private Operation op;

		public GlobalOperation(Operation op) {
			this.op = op;
		}

		public Operation getOperation() {
			return op;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see geditor.engine.concurrency.noconcurrencycontroller.OpObjectGen.OpPack#getType()
		 */	
		@Override
		public int getType() {
			return GLOBAL;
		}
	}
}

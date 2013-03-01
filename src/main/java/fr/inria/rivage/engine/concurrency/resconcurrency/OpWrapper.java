package fr.inria.rivage.engine.concurrency.resconcurrency;

import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.Operation;
import fr.inria.rivage.engine.operations.UnableToApplyException;
import java.io.Serializable;

public class OpWrapper implements Serializable, Cloneable {

	static public enum OpType {
		DO, UNDOLOCAL, UNDOGLOBAL, REDOLOCAL, REDOGLOBAL
	};
	
	private Operation operation;
	private long[] stateVector;
	private boolean local;
	private long fromSite;
	private OpType type;

	
	public OpWrapper(Operation operation, long[] stateVector, long fromSite, OpType type){
		this.operation = operation;
		this.stateVector = stateVector;
		this.fromSite = fromSite;
		this.type = type;
	}

	public Operation getOperation() {
		return operation;
	}

	public long[] getStateVector() {
		return stateVector;
	}	
	
	public void setLocal(boolean local){
		this.local = local;
	}

	public boolean isLocal() {
		return local;
	}

	public long getFromSite() {
		return fromSite;
	}

	public OpType getType() {
		return type;
	}
	
	@Override
	public Object clone(){
		OpWrapper o = new OpWrapper((Operation) operation.clone(),stateVector,fromSite,type);
		o.setLocal(local);
		return o;
	}

	public void apply(FileController fileController) throws UnableToApplyException {
		operation.apply(fileController);
	}

	public boolean isApplied() {
		return operation.isApplied();
	}

	public void setApplied(boolean b) {
		operation.setApplied(true);
	}
	
}

/*
 * Created on Jun 14, 2004
 */
package fr.inria.rivage.engine.operations;

/**
 * @author Yves
 */
public class UnableToApplyException extends Exception {
	
	public final static int CRITICAL_ERROR = 0;
	public final static int INFORMATION = 1;
	
	private int importance;
	
	public UnableToApplyException(String msg, int importance){
		super(msg);
		this.importance = importance;
	}
	public int getImportance() {
		return importance;
	}
}

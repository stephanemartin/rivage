package fr.inria.rivage.engine.tree;

/**
 * This exception gets thrown if a XML expression can't get
 * translated into a GEditor element.
 * @author Tobias Kuhn
 */
public class DecodeException extends Exception {

	/**
	 * Creates a new DecodeException.
	 * @param msg message of the exception
	 */
	public DecodeException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new DecodeException.
	 * @param msg message of the exception
	 * @param cause another Throwable that caused this exception
	 */
	public DecodeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}

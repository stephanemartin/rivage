package fr.inria.rivage.engine.concurrency.exceptions;

public class CCException extends Exception {

	public CCException() {
		super();
	}

	public CCException(String message, Throwable cause) {
		super(message, cause);
	}

	public CCException(String message) {
		super(message);
	}

	public CCException(Throwable cause) {
		super(cause);
	}

}

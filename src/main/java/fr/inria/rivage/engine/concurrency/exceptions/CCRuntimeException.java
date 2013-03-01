package fr.inria.rivage.engine.concurrency.exceptions;

public class CCRuntimeException extends RuntimeException{

	public CCRuntimeException() {
		super();
	}

	public CCRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CCRuntimeException(String message) {
		super(message);
	}

	public CCRuntimeException(Throwable cause) {
		super(cause);
	}

}

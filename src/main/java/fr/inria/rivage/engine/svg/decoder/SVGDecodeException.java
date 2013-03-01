package fr.inria.rivage.engine.svg.decoder;

/**
 * This exception gets thrown by the SVGDecoder when it is unable to
 * decode a SVG file.
 * @author Tobias Kuhn
 */
public class SVGDecodeException extends Exception {
	
	public SVGDecodeException(String message) {
		super(message);
	}
	
	public SVGDecodeException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

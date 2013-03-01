package fr.inria.rivage.engine.svg.encoder;

import java.awt.Color;
import java.awt.geom.AffineTransform;

/**
 * Some auxiliary methods for the SVG encoding.
 * @author Tobias Kuhn
 */
public class EncUtils {
	
	private EncUtils() {} // no instances allowed
	
	/**
	 * Transforms an AffineTransform object into a SVG text representation.
	 * @param af the AffineTransform object
	 * @return the SVG text representation
	 */
	public static String encTransform(AffineTransform af) {
		double[] m = new double[6];
		af.getMatrix(m);
		return "matrix(" + m[0] + "," + m[1] + "," + m[2] + "," + m[3] + "," + m[4] + "," + m[5] + ")";
	}
	
	/**
	 * Transforms a Color object into a SVG text representation.
	 * @param c the Color object
	 * @return the SVG text representation
	 */
	public static String encColor(Color c) {
		if (c == null || c.getAlpha() == 0) return "none";
		return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")";
	}

}

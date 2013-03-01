package fr.inria.rivage.engine.svg.decoder.attdec;

import fr.inria.rivage.elements.PointDouble;
import java.util.ArrayList;

/**
 * Provides decoding an SVG string that represents a list of points.
 * @author Tobias Kuhn
 */
public class PointsDec {
	
	private PointsDec() {} // no instances allowed

	/**
	 * Decodes an SVG point list.
	 * @param svgPoints the SVG point list
	 * @return an ArrayList of points
	 */
	public static ArrayList<PointDouble> decode(String svgPoints) {
		ArrayList<Double> numbers = NumbersDec.decode(svgPoints);
		
		ArrayList<PointDouble> points = new ArrayList<PointDouble>();
		for (int i = 0; i < numbers.size() / 2; i++) {
			PointDouble p = new PointDouble(numbers.get(2*i), numbers.get(2*i + 1));
			points.add(p);
		}
		
		return points;
	}

}

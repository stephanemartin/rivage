package fr.inria.rivage.engine.svg.decoder.attdec;

import java.util.ArrayList;


/**
 * Provides decoding an SVG string that represents a list of numbers.
 * @author Tobias Kuhn
 */
public class NumbersDec {
	
	private NumbersDec() {} // no instances allowed
	
	/**
	 * Decodes an SVG number list.
	 * @param numberString the SVG number list
	 * @return an ArrayList of double values
	 */
	public static ArrayList<Double> decode(String numberString) {
		ArrayList<Double> numbers = new ArrayList<Double>();
		numberString += " ";
		String s = "";
		for (int pos = 0; pos < numberString.length(); pos++) {
			char c = numberString.charAt(pos);
			if (c == ',' || Character.isWhitespace(c)) {
				if (s.length() > 0) {
					try {
						numbers.add(Double.parseDouble(s));
					} catch (NumberFormatException ex) {
						return null;
					}
					s = "";
				}
			} else {
				s += c;
			}
		}

		return numbers;
	}

}

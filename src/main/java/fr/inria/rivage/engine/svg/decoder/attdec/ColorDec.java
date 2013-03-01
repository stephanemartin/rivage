package fr.inria.rivage.engine.svg.decoder.attdec;

import fr.inria.rivage.engine.svg.decoder.DecUtils;
import java.awt.Color;
import java.util.HashMap;


/**
 * Provides decoding of an SVG color string.
 * @author Tobias Kuhn
 */
public class ColorDec {
	
	private ColorDec() {} // no instances allowed
	
	// stores color keywords
	private static HashMap<String, Color> keywords;
	
	/**
	 * Decodes an SVG color string.
	 * @param svgColor the SVG color string
	 * @return a Color object
	 */
	public static Color decode(String svgColor) {
		svgColor = DecUtils.clean(svgColor);
		
		if (keywords.get(svgColor) != null) {
			return keywords.get(svgColor);
	    } else if (svgColor.startsWith("rgb")) {
			int i1 = svgColor.indexOf("rgb(");
			int i2 = svgColor.indexOf(",", i1+4);
			int i3 = svgColor.indexOf(",", i2+1);
			int i4 = svgColor.indexOf(")", i3+1);
			
			if (i1 == -1 || i2 == -1 || i3 == -1 || i4 == -1) {
				return null;
			}
			
			int r, g, b;
			try {
				r = Integer.parseInt(svgColor.substring(i1 + 4, i2));
				g = Integer.parseInt(svgColor.substring(i2 + 1, i3));
				b = Integer.parseInt(svgColor.substring(i3 + 1, i4));
			} catch (NumberFormatException ex) {
				return null;
			}
			
			return new Color(r, g, b);
		} else if (svgColor.startsWith("#")) {
			try {
				String s = svgColor.substring(1);
				int i = Integer.parseInt(s, 16);
				return new Color(i);
			} catch (NumberFormatException ex) {
				return null;
			}
		}
		return null;
	}
	
	// initialize the keyword hash
	static {
		// For the sake of performance and memory management we provide only
		// a small subset of all color keywords defined in SVG.
		// For the complete list visit http://www.w3.org/TR/SVG/types.html
		keywords = new HashMap<String, Color>();
		keywords.put("black", new Color(0, 0, 0));
		keywords.put("blue", new Color(0, 0, 255));
		keywords.put("brown", new Color(165, 42, 42));
		keywords.put("gray", new Color(128, 128, 128));
		keywords.put("green", new Color(0, 128, 0));
		keywords.put("orange", new Color(255, 165, 0));
		keywords.put("pink", new Color(255, 192, 203));
		keywords.put("purple", new Color(128, 0, 128));
		keywords.put("red", new Color(255, 0, 0));
		keywords.put("white", new Color(255, 255, 255));
		keywords.put("yellow", new Color(255, 255, 0));
	}

}

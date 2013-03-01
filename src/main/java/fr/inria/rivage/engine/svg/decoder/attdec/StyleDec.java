package fr.inria.rivage.engine.svg.decoder.attdec;

import java.util.HashMap;

/**
 * Provides decoding of an SVG style string.
 * @author Tobias Kuhn
 */
public class StyleDec {
	
	private StyleDec() {} // no instances allowed
	
	/**
	 * Decodes an SVG style string.
	 * @param svgStyle the SVG style string
	 * @return a hash with the style attributes
	 */
	public static HashMap<String, Object> decode(String svgStyle) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		int b, e;
		
		b = svgStyle.indexOf("stroke:");
		if (b != -1) {
			e = svgStyle.indexOf(";", b);
			if (e == -1) e = svgStyle.length();
			hash.put("stroke", ColorDec.decode(svgStyle.substring(b+7, e)));
		}
		
		b = svgStyle.indexOf("fill:");
		if (b != -1) {
			e = svgStyle.indexOf(";", b);
			if (e == -1) e = svgStyle.length();
			hash.put("fill", ColorDec.decode(svgStyle.substring(b+5, e)));
		}
		
		b = svgStyle.indexOf("stroke-width:");
		if (b != -1) {
			e = svgStyle.indexOf(";", b);
			if (e == -1) e = svgStyle.length();
			try {
				hash.put("stroke-width", Float.parseFloat(svgStyle.substring(b+13, e)));
			} catch (NumberFormatException ex) {}
		}
		
		b = svgStyle.indexOf("font-size:");
		if (b != -1) {
			e = svgStyle.indexOf(";", b);
			if (e == -1) e = svgStyle.length();
			try {
				int i = (int) Float.parseFloat(svgStyle.substring(b+10, e));
				if (i <= 0) i = 1;
				hash.put("font-size", i);
			} catch (NumberFormatException ex) {}
		}
		
		return hash;
	}

}

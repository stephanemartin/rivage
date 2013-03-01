package fr.inria.rivage.engine.svg.decoder.attdec;

import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Provides decoding of an SVG transformation string.
 * @author Tobias Kuhn
 */
public class TransformDec {
	
	private TransformDec() {} // no instances allowed

	/**
	 * Decodes an SVG transformation string.
	 * @param svgTransform the SVG transformation string
	 * @return a AffineTransform object
	 */
	public static AffineTransform decode(String svgTransform) {
		AffineTransform af = new AffineTransform();
		
		while (true) {
			svgTransform = DecUtils.clean(svgTransform);
			
			if (svgTransform.startsWith("matrix")) {
				svgTransform = svgTransform.substring(6);
				svgTransform = DecUtils.clean(svgTransform);
				
				if (!svgTransform.startsWith("(")) return null;
				int p = svgTransform.indexOf(")");
				if (p == -1) return null;
				ArrayList<Double> n = NumbersDec.decode(svgTransform.substring(1, p));
				if (n.size() != 6) return null;
				
				af.concatenate(new AffineTransform(n.get(0), n.get(1), n.get(2), n.get(3), n.get(4), n.get(5)));
				svgTransform = svgTransform.substring(p+1);
			} else if (svgTransform.startsWith("scale")) {
				svgTransform = svgTransform.substring(5);
				svgTransform = DecUtils.clean(svgTransform);
				
				if (!svgTransform.startsWith("(")) return null;
				int p = svgTransform.indexOf(")");
				if (p == -1) return null;
				ArrayList<Double> n = NumbersDec.decode(svgTransform.substring(1, p));
				
				if (n.size() == 1) {
					af.scale(n.get(0), n.get(0));
				} else if (n.size() == 2) {
					af.scale(n.get(0), n.get(1));
				} else {
					return null;
				}

				svgTransform = svgTransform.substring(p+1);
			} else if (svgTransform.startsWith("translate")) {
				svgTransform = svgTransform.substring(9);
				svgTransform = DecUtils.clean(svgTransform);
				
				if (!svgTransform.startsWith("(")) return null;
				int p = svgTransform.indexOf(")");
				if (p == -1) return null;
				ArrayList<Double> n = NumbersDec.decode(svgTransform.substring(1, p));
				
				if (n.size() == 1) {
					af.translate(n.get(0), 0);
				} else if (n.size() == 2) {
					af.translate(n.get(0), n.get(1));
				} else {
					return null;
				}

				svgTransform = svgTransform.substring(p+1);
			} else if (svgTransform.startsWith("rotate")) {
				svgTransform = svgTransform.substring(6);
				svgTransform = DecUtils.clean(svgTransform);
				
				if (!svgTransform.startsWith("(")) return null;
				int p = svgTransform.indexOf(")");
				if (p == -1) return null;
				ArrayList<Double> n = NumbersDec.decode(svgTransform.substring(1, p));
				
				if (n.size() == 1) {
					af.rotate(GraphicUtils.degToRad(n.get(0)));
				} else if (n.size() == 3) {
					af.rotate(GraphicUtils.degToRad(n.get(0)), n.get(1), n.get(2));
				} else {
					return null;
				}

				svgTransform = svgTransform.substring(p+1);
			} else if (svgTransform.startsWith("skewX")) {
				svgTransform = svgTransform.substring(5);
				svgTransform = DecUtils.clean(svgTransform);
				
				if (!svgTransform.startsWith("(")) return null;
				int p = svgTransform.indexOf(")");
				if (p == -1) return null;
				ArrayList<Double> n = NumbersDec.decode(svgTransform.substring(1, p));
				if (n.size() != 1) return null;
				
				af.shear(Math.tan(GraphicUtils.degToRad(n.get(0))), 0);
				svgTransform = svgTransform.substring(p+1);
			} else if (svgTransform.startsWith("skewY")) {
				svgTransform = svgTransform.substring(5);
				svgTransform = DecUtils.clean(svgTransform);
				
				if (!svgTransform.startsWith("(")) return null;
				int p = svgTransform.indexOf(")");
				if (p == -1) return null;
				ArrayList<Double> n = NumbersDec.decode(svgTransform.substring(1, p));
				if (n.size() != 1) return null;

				af.shear(0, Math.tan(GraphicUtils.degToRad(n.get(0))));
				svgTransform = svgTransform.substring(p+1);
			} else {
				break;
			}
		}
		
		return af;
	}

}

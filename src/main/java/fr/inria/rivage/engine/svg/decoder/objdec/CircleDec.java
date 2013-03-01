package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.shapes.GEllipse;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.util.HashMap;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG circle element. (Circles get decoded as
 * ellipses.)
 * @author Tobias Kuhn
 */
public class CircleDec {
	
	private CircleDec() {} // no instances allowed
	
	/**
	 * Decodes the SVG circle element as an ellipse.
	 * @param el the SVG circle element
	 * @param styleHash the style for the circle
	 * @param decodeLogger logger for warnings
	 * @return an ellipse object representing the SVG circle element
	 */
	public static GEllipse decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("circle")) return null;
		
		GEllipse e = new GEllipse();
		Double cx = DecUtils.readDouble(el, "cx");
		Double cy = DecUtils.readDouble(el, "cy");
		Double r = DecUtils.readDouble(el, "r");
		if (cx == null || cy == null || r == null) {
			decodeLogger.putWarning("Cannot decode circle: illegal position or size.");
			return null;
		}
		e.getParameters().getBounds().setRect(new GBounds2D(cx - r, cy - r, 2*r, 2*r));
		DecUtils.setStyleForShape(e, styleHash, decodeLogger);
		return e;
	}

}

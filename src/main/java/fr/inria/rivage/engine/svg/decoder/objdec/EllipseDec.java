package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.shapes.GEllipse;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.util.HashMap;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG ellipse element.
 * @author Tobias Kuhn
 */
public class EllipseDec {
	
	private EllipseDec() {} // no instances allowed
	
	/**
	 * Decodes the SVG ellipse element.
	 * @param el the SVG ellipse element
	 * @param styleHash the style for the ellipse
	 * @param decodeLogger logger for warnings
	 * @return an ellipse object representing the SVG ellipse element
	 */
	public static GEllipse decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("ellipse")) return null;
		
		GEllipse e = new GEllipse();
		Double cx = DecUtils.readDouble(el, "cx");
		Double cy = DecUtils.readDouble(el, "cy");
		Double rx = DecUtils.readDouble(el, "rx");
		Double ry = DecUtils.readDouble(el, "ry");
		if (cx == null || cy == null || rx == null || ry == null) {
			decodeLogger.putWarning("Cannot decode ellipse: illegal position or size.");
			return null;
		}
		e.getParameters().getBounds().setRect(new GBounds2D(cx - rx, cy - ry, 2*rx, 2*ry));
		DecUtils.setStyleForShape(e, styleHash, decodeLogger);
		return e;
	}

}

package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.shapes.GRectangle;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.util.HashMap;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG rectangle element.
 * @author Tobias Kuhn
 */
public class RectangleDec {
	
	private RectangleDec() {} // no instances allowed

	/**
	 * Decodes the SVG rectangle element.
	 * @param el the SVG rectangle element
	 * @param styleHash the style for the rectangle
	 * @param decodeLogger logger for warnings
	 * @return a rectangle object representing the SVG rectangle element
	 */
	public static GRectangle decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("rect")) return null;
		
		GRectangle r = new GRectangle();
		Double x = DecUtils.readDouble(el, "x");
		Double y = DecUtils.readDouble(el, "y");
		Double width = DecUtils.readDouble(el, "width");
		Double height = DecUtils.readDouble(el, "height");
		if (x == null || y == null || width == null || height == null) {
			decodeLogger.putWarning("Cannot decode rectangle: illegal position or size.");
			return null;
		}
                if (true)
                throw new UnsupportedOperationException("Not yet");
		//r.setBounds2D(new GBounds2D(x, y, width, height));
		DecUtils.setStyleForShape(r, styleHash, decodeLogger);
		return r;
	}

}

package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.shapes.GLine;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.util.HashMap;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG line element.
 * @author Tobias Kuhn
 */
public class LineDec {
	
	private LineDec() {} // no instances allowed

	/**
	 * Decodes the SVG line element.
	 * @param el the SVG line element
	 * @param styleHash the style for the line
	 * @param decodeLogger logger for warnings
	 * @return a line object representing the SVG line element
	 */
	public static GLine decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("line")) return null;
		
		GLine l = new GLine();
		Double x1 = DecUtils.readDouble(el, "x1");
		Double y1 = DecUtils.readDouble(el, "y1");
		Double x2 = DecUtils.readDouble(el, "x2");
		Double y2 = DecUtils.readDouble(el, "y2");
		if (x1 == null || y1 == null || x2 == null || y2 == null) {
			decodeLogger.putWarning("Cannot decode line: illegal coordinates.");
			return null;
		}
		l.getParameters().getBounds().setRect(x1, y1, y2, y2);
            	DecUtils.setStyleForShape(l, styleHash, decodeLogger);
		return l;
	}

}

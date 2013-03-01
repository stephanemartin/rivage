package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.shapes.GPath;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG polygon element.
 * @author Tobias Kuhn
 */
public class PolygonDec {
	
	private PolygonDec() {} // no instances allowed
	
	/**
	 * Decodes the SVG polygon element.
	 * @param el the SVG polygon element
	 * @param styleHash the style for the polygon
	 * @param decodeLogger logger for warnings
	 * @return a polygon object representing the SVG polygon element
	 */
	public static GPath decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("polygon")) return null;
		
		GPath p = new GPath(null, null, Color.yellow, Color.yellow, null, true);
		ArrayList<PointDouble> points = DecUtils.readPoints(el);
		if (points.size() == 0) {
			decodeLogger.putWarning("Cannot decode polyline: illegal points");
			return null;
		}
		//p.setPoints(points);
		DecUtils.setStyleForShape(p, styleHash, decodeLogger);
		return p;
	}

}

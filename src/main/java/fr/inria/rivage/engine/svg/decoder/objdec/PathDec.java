package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import fr.inria.rivage.engine.svg.decoder.attdec.PathDescDec;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG path element.
 * @author Tobias Kuhn
 */
public class PathDec {
	
	private PathDec() {} // no instances allowed

	/**
	 * Decodes the SVG path element.
	 * @param el the SVG path element
	 * @param styleHash the style for the path
	 * @param decodeLogger logger for warnings
	 * @return a polygon or polyline or a group of them representing the SVG path element
	 */
	public static GObject decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("path")) return null;
		
		Attribute att = el.getAttribute("d");
		ArrayList<GObject> objs = PathDescDec.decode(att.getValue(), decodeLogger);
		if (objs.size() == 0) {
			return null;
		}
		if (objs.size() == 1) {
			GObject o = objs.get(0);
			DecUtils.setStyleForShape(o, styleHash, decodeLogger);
			return o;
		}
		GGroup g = new GGroup();
		for (GObject o : objs) {
			DecUtils.setStyleForShape(o, styleHash, decodeLogger);
			o.setParent(g);
			g.add(o);
		}
		return g;
	}

}

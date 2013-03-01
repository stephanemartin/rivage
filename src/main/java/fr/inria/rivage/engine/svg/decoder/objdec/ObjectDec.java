package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import fr.inria.rivage.engine.svg.decoder.attdec.TransformDec;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import org.jdom2.Attribute;
import org.jdom2.Element;

/**
 * Provides decoding of an arbitrary SVG element.
 * @author Tobias Kuhn
 */
public class ObjectDec {
	
	private ObjectDec() {} // no instances allowed

	/**
	 * Decodes an arbitrary SVG element.
	 * @param el the SVG element
	 * @param styleHash the style for the SVG element
	 * @param decodeLogger logger for warnings
	 * @return an object representing the SVG element
	 */
	@SuppressWarnings("unchecked")
	public static GObject decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		HashMap<String, Object> sh = (HashMap<String, Object>) styleHash.clone();
		DecUtils.storeStyle(el, sh);
		GObject obj = null;
		
		String type = el.getName();
		if (type.equals("svg") || type.equals("g")) {
			obj = GroupDec.decode(el, sh, decodeLogger);
		} else if (type.equals("ellipse")) {
			obj = EllipseDec.decode(el, sh, decodeLogger);
		} else if (type.equals("circle")) {
			obj = CircleDec.decode(el, sh, decodeLogger);
		} else if (type.equals("rect")) {
			obj = RectangleDec.decode(el, sh, decodeLogger);
		} else if (type.equals("line")) {
			obj = LineDec.decode(el, sh, decodeLogger);
		} else if (type.equals("polyline")) {
			obj = PolylineDec.decode(el, sh, decodeLogger);
		} else if (type.equals("polygon")) {
			obj = PolygonDec.decode(el, sh, decodeLogger);
		} else if (type.equals("path")) {
			obj = PathDec.decode(el, sh, decodeLogger);
		} else if (type.equals("text")) {
			obj = TextDec.decode(el, sh, decodeLogger);
		} else if (type.equals("image")) {
			decodeLogger.putWarning("Images are not imported.");
		} else if (type.equals("foreignObject")) {
			obj = AnnotationDec.decode(el);
		} else if (type.equals("desc")) {
			// ignore
		} else {
			decodeLogger.putWarning("Unknown object.");
		}
		
		Attribute att = el.getAttribute("transform");
		if (att != null && obj != null) {
			AffineTransform trans = TransformDec.decode(att.getValue());
			if (trans == null) {
				decodeLogger.putWarning("Illegal transform.");
			} else {
                            throw new UnsupportedOperationException("Not yet");
				//obj.transform(trans);
			}
		}
		
		return obj;
	}

}

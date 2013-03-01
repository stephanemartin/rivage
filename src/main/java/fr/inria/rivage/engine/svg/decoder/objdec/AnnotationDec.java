package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.shapes.GAnnotation;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import org.jdom2.Element;
import org.jdom2.Text;

/**
 * Provides decoding of a SVG foreign object, that represents an annotation.
 * @author Tobias Kuhn
 */
public class AnnotationDec {
	
	private AnnotationDec() {} // no instances allowed
	
	/**
	 * Tries to decode the foreign object as an annotation. If this does not
	 * succeed, null is returned.
	 * @param el the SVG foreign object element
	 * @return an annotation object or null if decoding is not successful
	 */
	public static GAnnotation decode(Element el) {
		if (!el.getName().equals("foreignObject")) return null;
		
		for (Object o1 : el.getContent()) {
			if (o1 instanceof Element) {
				Element annotationElement = (Element) o1;
				if (annotationElement.getName().equals("annotation")) {
					Double x = DecUtils.readDouble(el, "x");
					Double y = DecUtils.readDouble(el, "y");
					if (x == null || y == null) {
						return null;
					}
					GAnnotation a = new GAnnotation();
					if (true)
                                            throw new UnsupportedOperationException("Not yet");
                                        //a.setPos(new PointDouble(x, y));
					for (Object o2 : annotationElement.getContent()) {
						if (o2 instanceof Text) {
							a.setText(((Text) o2).getTextNormalize());
							return a;
						}
					}
                                        
					return null;
				}
			}
		}
		return null;
	}

}

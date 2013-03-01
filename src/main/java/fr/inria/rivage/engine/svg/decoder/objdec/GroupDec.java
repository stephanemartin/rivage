package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.util.HashMap;
import java.util.List;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG group element.
 * @author Tobias Kuhn
 */
public class GroupDec {
	
	private GroupDec() {} // no instances allowed

	/**
	 * Decodes the SVG group element and all its content.
	 * @param el the SVG group element
	 * @param styleHash the style for the group
	 * @param decodeLogger logger for warnings
	 * @return a group object representing the SVG group element
	 */
	public static GGroup decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		if (!el.getName().equals("g") && !el.getName().equals("svg")) return null;
		
		GGroup group = new GGroup();
		List content = el.getContent();
		for (Object o : content) {
			if (!(o instanceof Element)) continue;
			GObject go = ObjectDec.decode((Element) o, styleHash, decodeLogger);
			if (go != null) {
				go.setParent(group);
				group.add(go);
			}
		}
		if (group.size() == 0) return null;
		return group;
	}

}

package fr.inria.rivage.engine.svg.decoder.objdec;

import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.util.HashMap;
import org.jdom2.Element;

/**
 * Provides decoding of a SVG text element.
 * @author Tobias Kuhn
 */
public class TextDec {
	
	private TextDec() {} // no instances allowed

	/**
	 * Decodes the SVG text element.
	 * @param el the SVG text element
	 * @param styleHash the style for the text
	 * @param decodeLogger logger for warnings
	 * @return a text object or a group of text objects representing the SVG text element
	 */
	@SuppressWarnings("unchecked")
	public static GObjectShape decode(Element el, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
		/*if (!el.getName().equals("text")) return null;
		
		GGroup textGroup = new GGroup();
		
		Double x = DecUtils.readDouble(el, "x");
		Double y = DecUtils.readDouble(el, "y");
		
		List content = el.getContent();
		for (Object o : content) {
			String s;
			HashMap<String, Object> sh = null;
			
			if (o instanceof Element && ((Element) o).getName().equals("tspan")) {
				Element te = (Element) o;
				s = te.getValue();
				Double sx = DecUtils.readDouble(te, "x");
				Double sy = DecUtils.readDouble(te, "y");
				if (sx != null) x = sx;
				if (sy != null) y = sy;
				sh = (HashMap<String, Object>) styleHash.clone();
				DecUtils.storeStyle(te, sh);
			} else if (o instanceof Text) {
				s = ((Text) o).getTextNormalize();
				if (s.length() == 0) continue;
				sh = styleHash;
			} else {
				continue;
			}
			
			Color fill = (Color) sh.get("fill");
			if (fill == null) {
				decodeLogger.putWarning("No text color defined (black is used).");
				fill = Color.BLACK;
			}
			Integer fontsize = (Integer) sh.get("font-size");
			if (fontsize == null) {
				decodeLogger.putWarning("No text size defined (10 is used).");
				fontsize = 10;
			}
			
			GText t = new GText();
			t.setText(s);
			t.setFontSize(fontsize);
			Attribute fillAtt = el.getAttribute("fill");
			if (fillAtt != null) {
				t.setFrtColor(ColorDec.decode(fillAtt.getValue()));
			}
			
			AttributedString atstr = new AttributedString(s);
			atstr.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN, fontsize));
			FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
			TextMeasurer textMeasurer = new TextMeasurer(atstr.getIterator(), frc);
			TextLayout textLayout = new TextLayout(atstr.getIterator(), frc);
			double width = textMeasurer.getAdvanceBetween(0, s.length()) + 10;
			double yAscent = textLayout.getAscent();
			
			if (x == null) x = 0.0;
			if (y == null) y = 0.0;
			
			t.setBounds2D(new GBounds2D(x, y - yAscent, width, 0));
			t.setFrtColor(fill);
			t.setFontSize(fontsize);
			textGroup.addChild(t);
			t.setParent(textGroup);
			
			x += width;
		}
		
		if (textGroup.getChildrenCount() == 0) {
			return null;
		}
		return textGroup;*/
            return null;
	}

}

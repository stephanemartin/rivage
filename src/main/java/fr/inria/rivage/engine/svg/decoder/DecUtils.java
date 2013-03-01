package fr.inria.rivage.engine.svg.decoder;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.interfaces.IStrokable;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.svg.decoder.attdec.ColorDec;
import fr.inria.rivage.engine.svg.decoder.attdec.PointsDec;
import fr.inria.rivage.engine.svg.decoder.attdec.StyleDec;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

/**
 * Some auxiliary methods for the SVG decoding.
 *
 * @author Tobias Kuhn
 */
public class DecUtils {

    private DecUtils() {
    } // no instances allowed

    /**
     * Removes whitespaces and commas at the beginning of the string.
     *
     * @param s the input string
     * @return the cleaned string
     */
    public static String clean(String s) {
        for (int p = 0; p < s.length(); p++) {
            if (!Character.isWhitespace(s.charAt(p)) && !(s.charAt(p) == ',')) {
                return s.substring(p, s.length());
            }
        }
        return "";
    }

    /**
     * Reads a double value of the given XML element.
     *
     * @param el the XML element
     * @param attName the attribute to read
     * @return the double value or null if there is no valid value
     */
    public static Double readDouble(Element el, String attName) {
        Attribute att = el.getAttribute(attName);
        if (att == null) {
            return null;
        }
        try {
            return att.getDoubleValue();
        } catch (DataConversionException ex) {
        }
        return null;
    }

    /**
     * Reads the points of the attribute "points" of the given XML element.
     *
     * @param el the XML element
     * @return a list of the points
     */
    public static ArrayList<PointDouble> readPoints(Element el) {
        Attribute att = el.getAttribute("points");
        if (att == null) {
            return null;
        }
        return PointsDec.decode(att.getValue());
    }

    /**
     * Reads all the style attributes of the given XML element and stores it in
     * styleHash.
     *
     * @param el the XML element
     * @param styleHash the hash to store the style attributes
     */
    public static void storeStyle(Element el, HashMap<String, Object> styleHash) {
        Color stroke = null;
        Color fill = null;
        Float strokeWidth = null;
        Integer fontSize = null;

        Attribute styleAtt = el.getAttribute("style");
        if (styleAtt != null) {
            HashMap<String, Object> style = StyleDec.decode(styleAtt.getValue());
            stroke = (Color) style.get("stroke");
            fill = (Color) style.get("fill");
            strokeWidth = (Float) style.get("stroke-width");
            fontSize = (Integer) style.get("font-size");
        }

        Attribute strokeAtt = el.getAttribute("stroke");
        if (strokeAtt != null && !strokeAtt.getValue().equals("none")) {
            stroke = ColorDec.decode(strokeAtt.getValue());
        }

        Attribute fillAtt = el.getAttribute("fill");
        if (fillAtt != null && !fillAtt.getValue().equals("none")) {
            fill = ColorDec.decode(fillAtt.getValue());
        }

        Attribute strokeWidthAtt = el.getAttribute("stroke-width");
        if (strokeWidthAtt != null) {
            try {
                strokeWidth = strokeWidthAtt.getFloatValue();
            } catch (DataConversionException ex) {
            }
        }

        Attribute fontSizeAtt = el.getAttribute("font-size");
        if (fontSizeAtt != null) {
            try {
                fontSize = fontSizeAtt.getIntValue();
            } catch (DataConversionException ex) {
            }
        }

        if (stroke != null) {
            styleHash.put("stroke", stroke);
        }
        if (fill != null) {
            styleHash.put("fill", fill);
        }
        if (strokeWidth != null) {
            styleHash.put("stroke-width", strokeWidth);
        }
        if (fontSize != null) {
            styleHash.put("font-size", fontSize);
        }

    }

    /**
     * Sets the style of a graphical object.
     *
     * @param o the graphical object
     * @param styleHash the hash with the style attributes to set
     * @param decodeLogger logger for warnings
     */
    public static void setStyleForShape(GObject o, HashMap<String, Object> styleHash, DecodeLogger decodeLogger) {
        Color stroke = (Color) styleHash.get("stroke");
        Color fill = (Color) styleHash.get("fill");
        Float strokeWidth = (Float) styleHash.get("stroke-width");

        if (stroke == null && fill == null) {
            decodeLogger.putWarning("No stroke color and no fill color defined (black/gray is used).");
            stroke = Color.BLACK;
            fill = Color.GRAY;
        }

        /*if (o instanceof IColorable) {
         ((IColorable) o).setFrtColor(stroke);
         }*/
        /*if (o instanceof IColorable) {
         ((IColorable) o).setBckColor(fill);
         }*/
        if (o instanceof IStrokable) {
            if (strokeWidth == null) {
                ((IStrokable) o).setStroke(new SerBasicStroke(1)); // 1 as default
            } else {
                ((IStrokable) o).setStroke(new SerBasicStroke(strokeWidth));
            }
        }
    }
}

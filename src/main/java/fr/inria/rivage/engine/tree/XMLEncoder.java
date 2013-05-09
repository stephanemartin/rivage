/*
 * Created on Jul 23, 2004
 */
package fr.inria.rivage.engine.tree;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.GSnapPoint;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.SnapManager;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.concurrency.tools.Parameters.ParameterType;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * A class to transform pieces of the graphic tree or the complete tree in an
 * XML string.
 * 
 * @author Yves
 * @author Tobias Kuhn
 */
public class XMLEncoder {
    private static final Logger log = Logger.getLogger(XMLEncoder.class.getName());

	
	private HashMap<GSnapPoint, Integer> snapPointHash;
	private int snapPointCounter;

	private XMLEncoder() {
		snapPointHash = new HashMap<GSnapPoint, Integer>();
		snapPointCounter = 0;
	}
	
	public static String getAsString(Element xmlElement) {
		Document doc = new Document();
		doc.setRootElement(xmlElement);
		return new XMLOutputter(Format.getPrettyFormat()).outputString(doc);
	}
	
	public static Element getXMLElement(GDocument document) {
		XMLEncoder encoder = new XMLEncoder();
		return encoder.encodeDocument(document);
	}
	
	private Element encodeDocument(GDocument document) {
		Element xmlElement = new Element("document");
		for (GObject p : document) {
			xmlElement.addContent(encodePage((Page)p));
		}
		return xmlElement;
	}
	
	public static Element getXMLElement(Page page) {
		XMLEncoder encoder = new XMLEncoder();
		return encoder.encodePage(page);
	}
	
	private Element encodePage(Page page) {
		Element xmlElement = new Element("page");
		xmlElement.setAttribute("name", page.getParameters().getText());
		xmlElement.setAttribute("width", "" + page.getDimension().width);
		xmlElement.setAttribute("height", "" + page.getDimension().height);
		for (GObject l : page) {
			xmlElement.addContent(encodeLayer((GLayer)l));
		}
		return xmlElement;
	}
	
	public static Element getXMLElement(GLayer layer) {
		XMLEncoder encoder = new XMLEncoder();
		return encoder.encodeLayer(layer);
	}
	
	private Element encodeLayer(GLayer layer) {
		Element xmlElement = new Element("layer");
		xmlElement.setAttribute("name", layer.getParameters().getText());
		xmlElement.setAttribute("color", layer.getParameters().getColor(ParameterType.FgColor).getRGB() + "");
		xmlElement.addContent(encodeRoot((GObjectContainer)layer));
		return xmlElement;
	}
	
	public static Element getXMLElement(GObject object) {
		XMLEncoder encoder = new XMLEncoder();
		return encoder.encodeTreeElement(object);
	}

	/**
	 * Selects the right decoder method to encode the given object.
	 * 
	 * @param object the object to encode
	 * @return an XML element containing the object's content.
	 */
	private Element encodeTreeElement(GObject object) {
		Element node = null;
		if (object instanceof GObjectContainer) {
			node = encodeRoot((GObjectContainer) object);
		} else if (object instanceof GGroup) {
			node = encodeGroup((GGroup) object);
		} else if (object instanceof GObjectShape) {
			node = encodeObject((GObjectShape) object);
		}
		return node;
	}

	/**
	 * Encodes a tree element of type GObjectShape.
	 * 
	 * @param object the object to encode
	 * @return an XML element.
	 */
	private Element encodeObject(GObjectShape object) {
		return saveFields(object);
	}

	private Element encodeGroup(GGroup group) {
		Element node = new Element(GGroup.class.getName());

		
		Iterator<GObject> i = group.iterator();
		while (i.hasNext()) {
			Element child = encodeTreeElement(i.next());
			child.setAttribute("child", "true");
			node.addContent(child);
		}

		saveFields((GObject) group);

		return node;
	}

	private Element encodeRoot(GObjectContainer<GObject> root) {
		Element node = new Element(root.getClass().getName());
		for (GObject i : root){
			Element child = encodeTreeElement(i);
			child.setAttribute("child", "true");
			node.addContent(child);
		}
		return node;
	}

	private Element saveFields(GObject object) {
		String[] fields = object.getParameters().getNames();
		Element node = new Element(object.getClass().getName());

		/*
		 * Get the class of the object to call the getters methods -- check if
		 * primitive type or not and encode accordingly.
		 */

		Class objclass = object.getClass();
		for (int i = 0; i < fields.length; i++) {
			try {
				Element value = new Element(fields[i]);
				Method m = objclass.getMethod("get" + fields[i], new Class[] {});

				Class returntype = m.getReturnType();
				Object retval = m.invoke(object, new Object[] {});

				value.setAttribute("type", returntype.getName());
				if (returntype.isPrimitive()) {
					value.setText(retval.toString());
				} else {
					encodeObject(retval, value);
				}

				node.addContent(value);

			} catch (NoSuchMethodException nsme) {
				log.log(Level.SEVERE, "The method with name \"{0}\" for element \"{1}\" was not found.{2}", new Object[]{fields[i], object.getClass().getName(), nsme});
			} catch (IllegalArgumentException e) {
				log.log(
						Level.SEVERE, "The method with name \"{0}\" for element \"{1}\" takes arguments. This should not be. All getters should be without arguments.{2}", new Object[]{fields[i], object.getClass().getName(), e});
			} catch (IllegalAccessException e) {
				log.log(
						Level.SEVERE, "The method with name \"{0}\" for element \"{1}\" could not be accessed. Please check the access specifier{2}", new Object[]{fields[i], object.getClass().getName(), e});
			} catch (InvocationTargetException e) {
				log.log(Level.SEVERE, "The method with name \"{0}\" for element \"{1}\" threw an exception.{2}", new Object[]{fields[i], object.getClass().getName(), e});
			}
		}
		return node;
	}

	private void encodeColor(Color c, Element v) {
		v.setText(((Color) c).getRGB() + "");
	}

	private void encodeString(String s, Element v) {
		v.setText((String) s);
	}

	private void encodeGBounds2D(GBounds2D b, Element v) {
		Element x = new Element("x");
		Element y = new Element("y");
		Element width = new Element("width");
		Element height = new Element("height");
		x.addContent(b.getX() + "");
		y.addContent(b.getY() + "");
		width.addContent(b.getWidth() + "");
		height.addContent(b.getHeight() + "");
		v.addContent(x);
		v.addContent(y);
		v.addContent(width);
		v.addContent(height);
	}

	private void encodePoint(Point2D p, Element v) {
		v.setAttribute("x", p.getX() + "");
		v.setAttribute("y", p.getY() + "");
		if (p instanceof GSnapPoint) {
			v.setAttribute("snapID", getSnapPointID((GSnapPoint) p) + "");
		}
	}

	private void encodeAffineTransform(AffineTransform a, Element v) {
		double[] m = new double[6];
		a.getMatrix(m);
		Element m00 = new Element("m00");
		m00.addContent(m[0] + "");
		v.addContent(m00);
		Element m10 = new Element("m10");
		m10.addContent(m[1] + "");
		v.addContent(m10);
		Element m01 = new Element("m01");
		m01.addContent(m[2] + "");
		v.addContent(m01);
		Element m11 = new Element("m11");
		m11.addContent(m[3] + "");
		v.addContent(m11);
		Element m02 = new Element("m02");
		m02.addContent(m[4] + "");
		v.addContent(m02);
		Element m12 = new Element("m12");
		m12.addContent(m[5] + "");
		v.addContent(m12);
	}

	private void encodeSerBasicStroke(SerBasicStroke s, Element v) {
		v.setText(s.getLineWidth() + "");
	}
	
	private void encodeSnapManager(SnapManager sm, Element v) {
		for (GSnapPoint p : sm.getSnapPoints()) {
			Element el = new Element("snap");
			el.setAttribute("snapID", getSnapPointID(p) + "");
			el.setAttribute("relX", p.getRelPos().x + "");
			el.setAttribute("relY", p.getRelPos().y + "");
			v.addContent(el);
		}
	}

	private void encodeObject(Object o, Element v) {
		if (o instanceof SerBasicStroke) {
			encodeSerBasicStroke((SerBasicStroke) o, v);
		} else if (o instanceof AffineTransform) {
			encodeAffineTransform((AffineTransform) o, v);
		} else if (o instanceof Point2D) {
			encodePoint((Point2D) o, v);
		} else if (o instanceof GBounds2D) {
			encodeGBounds2D((GBounds2D) o, v);
		} else if (o instanceof String) {
			encodeString((String) o, v);
		} else if (o instanceof Color) {
			encodeColor((Color) o, v);
		} else if (o instanceof ArrayList) {
			encodeArrayList((ArrayList) o, v);
		} else if (o instanceof SnapManager) {
			encodeSnapManager((SnapManager) o, v);
		}
	}

	@SuppressWarnings("unchecked")
	private void encodeArrayList(ArrayList a, Element v) {
		if (a.isEmpty()) {
			return;
		}
		
		v.setAttribute("subtype", a.get(0).getClass().toString());
		if (a.get(0) instanceof PointDouble) {
			String s = "";
			for (PointDouble p : (ArrayList<PointDouble>) a) {
				s += p.getX() + " " + p.getY() + " ";
			}
			v.setAttribute("points", s);
		} else {
			for (int i = 0; i < a.size(); i++) {
				Element e = new Element("e" + i);
				encodeObject(a.get(i), e);
				v.addContent(e);
			}
		}
	}
	
	private int getSnapPointID(GSnapPoint sp) {
		Integer id = snapPointHash.get(sp);
		if (snapPointHash.get(sp) == null) {
			id = snapPointCounter++;
			snapPointHash.put(sp, id);
		}
		return id;
	}

}

/*
 * Created on Jul 27, 2004
 */
package fr.inria.rivage.engine.tree;

import fr.inria.rivage.elements.Page;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.GSnapPoint;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.SnapManager;
import fr.inria.rivage.elements.interfaces.ISnappable;
import fr.inria.rivage.elements.interfaces.ISnapper;
import fr.inria.rivage.elements.interfaces.ITreeElement;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filter;
import org.jdom2.input.SAXBuilder;

/**
 * @author Yves
 * @author Tobias Kuhn
 */
@SuppressWarnings("unchecked")
public class XMLDecoder {

	private Logger log = Logger.getLogger(XMLDecoder.class);
	
	private HashMap<SnapManager, Element> snapManagerHash;
	private HashMap<Integer, GSnapPoint> snapPointHash;
	
	private XMLDecoder() {
		snapManagerHash = new HashMap<SnapManager, Element>();
		snapPointHash = new HashMap<Integer, GSnapPoint>();
	}
	
	public static Element getXMLElement(String xmlString) throws DecodeException {
		try {
			SAXBuilder sb = new SAXBuilder();
			sb.setValidation(false);
			Document xmlDoc = sb.build(new StringReader(xmlString));
			
			return xmlDoc.getRootElement();
		} catch (Exception ex) {
			throw new DecodeException("Cannot decode XML string.", ex);
		}
	}
	
	public static GDocument getDocument(Element xmlElement) throws DecodeException {
		XMLDecoder decoder = new XMLDecoder();
		GDocument doc = decoder.decodeDocument(xmlElement);
		decoder.setAllSnapPoints();
		return doc;
	}
	
	private GDocument decodeDocument(Element xmlElement) throws DecodeException {
		if (!xmlElement.getName().equals("document")) {
			throw new DecodeException("Illegal element: Document element expected.");
		}throw new UnsupportedOperationException("Not yet");
		//GDocument document = new GDocument("");
		/*for (Object o : xmlElement.getContent()) {
			if (o instanceof Element) {
                            throw new UnsupportedOperationException("Not yet");
				//document.addPage(decodePage(document, (Element) o));
			}
		}*/
		//return document;
	}
	
	public static Page getPage(GDocument document, Element xmlElement) throws DecodeException {
		XMLDecoder decoder = new XMLDecoder();
		Page page = decoder.decodePage(document, xmlElement);
		decoder.setAllSnapPoints();
		return page;
	}
	
	private Page decodePage(GDocument document, Element xmlElement) throws DecodeException {
		if (!xmlElement.getName().equals("page")) {
			throw new DecodeException("Illegal element: Page element expected.");
		}
				
		String name = xmlElement.getAttributeValue("name");
		int width = Integer.parseInt(xmlElement.getAttributeValue("width"));
		int height = Integer.parseInt(xmlElement.getAttributeValue("height"));
		Dimension dimension = new Dimension(width, height);
		throw new UnsupportedOperationException("Not yet");
		/*Page page = new Page(document, document.generateNextGlobalId(), dimension, name, false);
		
		for (Object o : xmlElement.getContent()) {
			if (o instanceof Element) {
				page.addLayer(decodeLayer(page, (Element) o));
			}
		}
		return page;*/
	}
	
	public static GLayer getLayer(Page page, Element xmlElement) throws DecodeException {
		XMLDecoder decoder = new XMLDecoder();
		GLayer layer = decoder.decodeLayer(page, xmlElement);
		decoder.setAllSnapPoints();
		return layer;
	}
	
	private GLayer decodeLayer(Page page, Element xmlElement) throws DecodeException {
		if (!xmlElement.getName().equals("layer")) {
			throw new DecodeException("Illegal element: Layer element expected.");
		}
		
		String name = xmlElement.getAttributeValue("name");
		Color color = new Color(Integer.parseInt(xmlElement.getAttributeValue("color")));
				
		Element rootElement = null;
		for (Object o : xmlElement.getContent()) {
			if (o instanceof Element) {
				rootElement = (Element) o;
				break;
			}
		}
		if (rootElement == null) {
			throw new DecodeException("No root element found.");
		}
		ITreeElement te = decodeTreeElement(rootElement);
		
               		throw new UnsupportedOperationException("Not yet");

                /*if (!(te instanceof GRoot)) {
			throw new DecodeException("Invalid root element.");
		}
		GRoot root = (GRoot) te;
		try {
			page.getDocument().assignGlobalsIDs(root);
		} catch (IllegalTreeOpException ex) {
			throw new DecodeException("Cannot initialize root.", ex);
		}
		
		return new GLayer(page, page.getDocument().generateNextGlobalId(), name, root);*/
	}
	
	public static ITreeElement getTreeElement(Element xmlElement) throws DecodeException {
		XMLDecoder decoder = new XMLDecoder();
		ITreeElement el = decoder.decodeTreeElement(xmlElement);
		decoder.setAllSnapPoints();
		return el;
	}

	private ITreeElement decodeTreeElement(Element rootel) throws DecodeException {
		ITreeElement treeel = null;
                		throw new UnsupportedOperationException("Not yet");
/*
		if (rootel.getName().equals(GRoot.class.getName())) {
			treeel = decodeGRoot(rootel);
		} else if (rootel.getName().equals(GGroup.class.getName())) {
			treeel = decodeGGroup(rootel);
		} else {
			treeel = decodeIGObject(rootel);
		}

		return treeel;*/
	}

	private ITreeElement decodeIGObject(Element rootel) throws DecodeException {
		Object o = null;
		try {
			String type = rootel.getName();
			Class c = Class.forName(type);

			Constructor constructor = c.getConstructor(new Class[] {});

			o = constructor.newInstance(new Object[] {});
		} catch (Exception e) {
			throw new DecodeException(
					"An exception happened in decodeIGObject's reflection part.",
					e);
		}
		decodeFields((GObjectShape) o, rootel);

		return (ITreeElement) o;
	}

	private GObjectContainer decodeGGroup(Element rootel) throws DecodeException {
		log.debug("Decoding an object of type \"" + rootel.getName() + "\".");
		GGroup group = new GGroup();
		List l = rootel.getContent(new ChildElementFilter());
		Iterator i = l.iterator();
		int k = 0;
		while (i.hasNext()) 
		{
			GObjectShape obj = (GObjectShape) decodeTreeElement((Element) i.next());
			obj.setParent(group);
			//obj.setZ(k); k++;
			//group.addChild(obj);
		}

		decodeFields(group, rootel);

		return group;
	}

	private ITreeElement decodeGRoot(Element rootel) throws DecodeException {
		log.debug("Decoding an object of type \"" + rootel.getName() + "\".");

            throw new UnsupportedOperationException("Not yet");

	/*	//GRoot root = new GRoot();
		List l = rootel.getContent(new ChildElementFilter());
		Iterator i = l.iterator();
		
		int k = 0;
		while (i.hasNext()) 
		{
			GObjectShape obj = (GObjectShape) decodeTreeElement((Element) i.next());
			obj.setParent(root);
			obj.setZ(k); k++;			
			root.addChild(obj);
		}
		return root;*/
	}

	private void decodeFields(GObject obj, Element el) throws DecodeException {
		log.debug("Decoding fields for object \"" + obj.getClass().getName()
				+ "\".");
		List attribs = el.getContent(new FieldElementFilter());

		Iterator i = attribs.iterator();
		Class objclass = obj.getClass();

		while (i.hasNext()) {
			Element attrib = (Element) i.next();
			String name = attrib.getName();
			String type = attrib.getAttributeValue("type");
			Object argument = null;
			
			if (type.equals(SnapManager.class.getName())) {
				if (!(obj instanceof ISnappable)) {
					throw new DecodeException("Snappable object expected.");
				}
				snapManagerHash.put(((ISnappable) obj).getSnapManager(), attrib);
				continue;
			}

			log.debug("Decoding attribute with name \"" + name
					+ "\" and type \"" + type + "\".");

			Class c = null;
			if ((c = getBoxedPrimitiveType(type)) != null) {
				argument = decodePrimitive(c, attrib);
				try {
					Field f = c.getField("TYPE");
					c = (Class) f.get(argument);
				} catch (Exception e) {
					log.error("No such field ??? What's that type then ? ", e);
				}
			} else {
				try {
					c = Class.forName(type);
				} catch (ClassNotFoundException cnfe) {
					throw new DecodeException("The class could not be found.",
							cnfe);
				}
				argument = decodeComplex(c, attrib, obj);
			}

			/*
			 * Argument may be null, for example if we have an empty ArrayList.
			 * In that case, don't make the call.
			 */
			try {
				if (argument != null) {
					Method m = objclass.getMethod("set" + name,
							new Class[] { c });
					m.invoke(obj, new Object[] { argument });
				}
			} catch (Exception nsme) {
				throw new DecodeException(
						"An error happened calling the method to set the field.",
						nsme);
			}
		}
	}

	private Object decodePrimitive(Class c, Element attribute)
			throws DecodeException {
		try {
			Constructor con = c.getConstructor(new Class[] { String.class });
			return con.newInstance(new Object[] { attribute.getText() });
		} catch (Exception e) {
			throw new DecodeException(
					"An error happened while decoding a primitive.", e);
		}
	}

	private Object decodeComplex(Class c, Element attribute, GObject go)
			throws DecodeException {
		Object obj = null;

		if (c.getName().equals(Color.class.getName())) {
			obj = decodeColor(attribute);
		} else if (c.getName().equals(String.class.getName())) {
			obj = attribute.getText();
		} else if (c.getName().equals(GBounds2D.class.getName())) {
			obj = decodeGBounds2D(attribute);
		} else if (c.getName().equals(PointDouble.class.getName())) {
			double x = Double.parseDouble(attribute.getAttributeValue("x"));
			double y = Double.parseDouble(attribute.getAttributeValue("y"));
			obj = new PointDouble(x, y);
		} else if (c.getName().equals(GSnapPoint.class.getName())) {
			double x = Double.parseDouble(attribute.getAttributeValue("x"));
			double y = Double.parseDouble(attribute.getAttributeValue("y"));
			int snapID = Integer.parseInt(attribute.getAttributeValue("snapID"));
			if (!(go instanceof ISnapper)) {
				throw new DecodeException("Snapper object expected.");
			}
			obj = new GSnapPoint((ISnapper) go, x, y);
			snapPointHash.put(snapID, (GSnapPoint) obj);
		} else if (c.getName().equals(AffineTransform.class.getName())) {
			double m00 = Double.parseDouble(attribute.getChildText("m00"));
			double m10 = Double.parseDouble(attribute.getChildText("m10"));
			double m01 = Double.parseDouble(attribute.getChildText("m01"));
			double m11 = Double.parseDouble(attribute.getChildText("m11"));
			double m02 = Double.parseDouble(attribute.getChildText("m02"));
			double m12 = Double.parseDouble(attribute.getChildText("m12"));
			obj = new AffineTransform(new double[] { m00, m10, m01, m11, m02,
					m12 });
		} else if (c.getName().equals(SerBasicStroke.class.getName())) {
			obj = new SerBasicStroke(Float.parseFloat(attribute.getText()));
		} else if (c.getName().equals(ArrayList.class.getName())) {
			String subtype = attribute.getAttributeValue("subtype");
			/* First, check the type of the encoded elements if there are any */
			if (subtype != null) {
				if (subtype.equals(String.class.toString())) {
					ArrayList<String> strings = new ArrayList<String>();
					List<Element> children = attribute.getChildren();
					for (Element s : children) {
						strings.add(s.getText());
					}
					obj = strings;
				} else if (subtype.equals(PointDouble.class.toString())) {
					ArrayList<PointDouble> points = new ArrayList<PointDouble>();
					String[] s = attribute.getAttributeValue("points").split(" ");
					for (int i=0; i < s.length/2; i++) {
						points.add(new PointDouble(
								Double.parseDouble(s[i*2]),
								Double.parseDouble(s[i*2 + 1])));
					}
					obj = points;
				} else if (subtype.equals(GSnapPoint.class.toString())) {
					ArrayList<GSnapPoint> points = new ArrayList<GSnapPoint>();
					for (Element s : (List<Element>) attribute.getChildren()) {
						double x = Double.parseDouble(s.getAttributeValue("x"));
						double y = Double.parseDouble(s.getAttributeValue("y"));
						int snapID = Integer.parseInt(s.getAttributeValue("snapID"));
						if (!(go instanceof ISnapper)) {
							throw new DecodeException("Snapper object expected.");
						}
						GSnapPoint sp = new GSnapPoint((ISnapper) go, x, y);
						snapPointHash.put(snapID, sp);
						points.add(sp);
					}
					obj = points;
				}
			}
		}
		return obj;
	}

	private Object decodeGBounds2D(Element e) {
		GBounds2D gbounds = null;
		double x = Double.parseDouble(e.getChildText("x"));
		double y = Double.parseDouble(e.getChildText("y"));
		double width = Double.parseDouble(e.getChildText("width"));
		double height = Double.parseDouble(e.getChildText("height"));
		gbounds = new GBounds2D(x, y, width, height);
		return gbounds;
	}

	private Class getBoxedPrimitiveType(String type) {
		Class c = null;
		if (type.equals(Byte.TYPE.getName())) {
			c = Byte.class;
		} else if (type.equals(Short.TYPE.getName())) {
			c = Short.class;
		} else if (type.equals(Integer.TYPE.getName())) {
			c = Integer.class;
		} else if (type.equals(Long.TYPE.getName())) {
			c = Long.class;
		} else if (type.equals(Float.TYPE.getName())) {
			c = Float.class;
		} else if (type.equals(Double.TYPE.getName())) {
			c = Double.class;
		}
		return c;
	}
	
	private void setAllSnapPoints() throws DecodeException {
		for (SnapManager sm : snapManagerHash.keySet()) {
			Element el = snapManagerHash.get(sm);
			for (Object o : el.getContent()) {
				if (!(o instanceof Element)) continue;
				Element p = (Element) o;
				int snapID = Integer.parseInt(p.getAttributeValue("snapID"));
				double relX = Double.parseDouble(p.getAttributeValue("relX"));
				double relY = Double.parseDouble(p.getAttributeValue("relY"));
				GSnapPoint sp = snapPointHash.get(snapID);
				if (sp == null) continue;
				if (sp.isSnapping()) {
					throw new DecodeException("Illegal snap point.");
				}
				sm.snap(sp, relX, relY);
			}
		}
	}

	/**
	 * Decodes an XML element which is a color.
	 * 
	 * @param e an XML element.
	 * @return returns the encoded color or black if there was an error.
	 */
	private Color decodeColor(Element e) throws DecodeException {
		Color c = new Color(0, 0, 0); 
		String s = e.getText();
		if (s != null && s.length() > 0)
			c = new Color(Integer.parseInt(s), true);
		else
			c = null;
			//throw new DecodeException("The element " + e
			//		+ " could not be decoded to a color.");
		return c;
	}

	static class ChildElementFilter implements Filter {
        @Override
		public boolean matches(Object obj) {
			if (obj instanceof Element) {
				Element e = (Element) obj;
				Attribute att = e.getAttribute("child");
				if (att == null)
					return false;
				return att.getValue().equals("true");
			}
			return false;
		}

        @Override
        public List filter(List list) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object filter(Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter negate() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter or(Filter filter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter and(Filter filter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter refine(Filter filter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
	}

	static class FieldElementFilter implements Filter {
        @Override
		public boolean matches(Object obj) {
			if (obj instanceof Element) {
				Element e = (Element) obj;
				Attribute att = e.getAttribute("child");
				if (att == null)
					return true;
				return !(att.getValue().equals("true"));
			}
			return false;
		}

        @Override
        public List filter(List list) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object filter(Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter negate() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter or(Filter filter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter and(Filter filter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Filter refine(Filter filter) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
	}
	
}

package fr.inria.rivage.engine.svg.encoder;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GFile;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.elements.PointDouble;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;


/**
 * This class allows to export GEditor-graphics as SVG.
 * (Snap-points are not exportet.)
 * @author Tobias Kuhn
 */
public class SVGEncoder {

	private Page page;
	private File file;
    private static final Logger LOG = Logger.getLogger(SVGEncoder.class.getName());


	/**
	 * Creates a new encoder for the export of the given page into the
	 * specified SVG file (which will be generated). The export gets performed
	 * as you call create().
	 * @param page the page to encode
	 * @param file the SVG file to be generated
	 */
	public SVGEncoder(Page page, File file) {
		this.page = page;
		this.file = file;
	}
	
	/**
	 * Performs the export into the SVG file.
	 * @throws IOException if the creation of the SVG file fails
	 */
	public void create() throws IOException {		
		FileOutputStream out = new FileOutputStream(file);
		writeSVG(out);
		out.close();
	}
	
	private void writeSVG(OutputStream out) throws IOException {
		Document doc = new Document();
		
		Element rootElement = new Element("svg");
		rootElement.setAttribute("version", "1.1");
		rootElement.setAttribute("width", page.getDimension().width + "");
		rootElement.setAttribute("height", page.getDimension().height + "");
		rootElement.addNamespaceDeclaration(
				Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink"));
		throw new UnsupportedOperationException("Not yet");
		/*for (GObjectShape obj : page.getAllVisibleObjects()) {
			Element child = dispatch(obj);
			if (child != null) rootElement.addContent(child);
		}
		
		doc.setRootElement(rootElement);
		new XMLOutputter(Format.getPrettyFormat()).output(doc, out);*/
	}

	private Element dispatch(GObject object) throws IOException {
		Element node = null;
		if (object instanceof GGroup) {
			node = buildXMLTreeFromGGroup((GGroup) object);
		} else if (object instanceof GFile) {
			node = buildXMLTreeFromGFile((GFile) object);
		} else if (object instanceof GObjectShape) {
			node = buildXMLTreeFromIGObject((GObjectShape) object);
		}
		return node;
	}

	private Element buildXMLTreeFromIGObject(GObjectShape object) throws IOException {
		Element node = null;
		throw new UnsupportedOperationException("Not yet");
		/*if (object instanceof GRectangle) {
			GRectangle r = (GRectangle) object;
			node = new Element("rect");
			setBounds(node, r.getBounds2D());
			setFill(node, r.getBckColor());
			setStroke(node, r.getFrtColor(), r.getStroke().getLineWidth());
			setTransform(node, r.getTransform());
		} else if (object instanceof GEllipse) {
			GEllipse e = (GEllipse) object;
			node = new Element("ellipse");
			node.setAttribute("cx", e.getBounds2D().getCenterX() + "");
			node.setAttribute("cy", e.getBounds2D().getCenterY() + "");
			node.setAttribute("rx", e.getBounds2D().getWidth()/2 + "");
			node.setAttribute("ry", e.getBounds2D().getHeight()/2 + "");
			setFill(node, e.getBckColor());
			setStroke(node, e.getFrtColor(), e.getStroke().getLineWidth());
			setTransform(node, e.getTransform());
		} else if (object instanceof GLine) {
			GLine l = (GLine) object;
			node = new Element("line");
			node.setAttribute("x1", l.getPointAtIndex(1).getX() + "");
			node.setAttribute("y1", l.getPointAtIndex(1).getY() + "");
			node.setAttribute("x2", l.getPointAtIndex(2).getX() + "");
			node.setAttribute("y2", l.getPointAtIndex(2).getY() + "");
			setStroke(node, l.getFrtColor(), l.getStroke().getLineWidth());
		} else if (object instanceof GPolyLine) {
			GPolyLine p = (GPolyLine) object;
			node = new Element("polyline");
			setPoints(node, p.getPoints());
			setFill(node, null);
			setStroke(node, p.getFrtColor(), p.getStroke().getLineWidth());
			setTransform(node, p.getTransform());
		} else if (object instanceof GPolygon) {
			GPolygon p = (GPolygon) object;
			node = new Element("polygon");
			setPoints(node, p.getPoints());
			setFill(node, p.getBckColor());
			setStroke(node, p.getFrtColor(), p.getStroke().getLineWidth());
			setTransform(node, p.getTransform());
		} else if (object instanceof GText) {
			GText t = (GText) object;
			String s = t.getText();
			GBounds2D b = t.getBounds2D();
			node = new Element("text");
			node.setAttribute("font-family", "Arial");
			node.setAttribute("font-size", t.getFontSize() + "");
			setFill(node, t.getFrtColor());
			setTransform(node, t.getTransform());
			
			AttributedString as = new AttributedString(s);
			as.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN, t.getFontSize()));
			LineBreakMeasurer linebreaker = new LineBreakMeasurer(
					as.getIterator(), new FontRenderContext(null, true, true));
			double y = b.getY();
			while (linebreaker.getPosition() < s.length()) {
				TextLayout tl = linebreaker.nextLayout((float) b.getWidth());
				y += tl.getAscent();
				Element tspan = new Element ("tspan");
				tspan.setAttribute("x", b.getX() + "");
				tspan.setAttribute("y", y + "");
				int p = linebreaker.getPosition();
				tspan.addContent(s.substring(p - tl.getCharacterCount(), p));
				node.addContent(tspan);
				y += tl.getDescent() + tl.getLeading();
			}
		} else if (object instanceof GBitmap) {
			GBitmap b = (GBitmap) object;
			node = new Element("image");
			node.setAttribute(
					"href",
					file.getName() + ".img/" + b.getImageName(),
					Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink")
			);
			setBounds(node, b.getBounds2D());
			setTransform(node, b.getTransform());
			
			new File(file.getAbsolutePath() + ".img").mkdir();
			File imageFile = new File(file.getAbsolutePath() + ".img/" + b.getImageName());
			byte[] bytes;
			try {
				bytes = Application.getApplication().getServerNetwork()
					.loadFileAsByteArray(ServerNetwork.DATA_FILES, b.getImageName());
			} catch (ClassNotFoundException ex) {
				log.error("Could not load image from server", ex);
				return node;
			}
			
			FileOutputStream out = new FileOutputStream(imageFile);
			out.write(bytes);
			out.close();
		} else if (object instanceof GAnnotation) {
			GAnnotation a = (GAnnotation) object;
			node = new Element("foreignObject");
			node.setAttribute("x", a.getPos().getX() + "");
			node.setAttribute("y", a.getPos().getY() + "");
			node.setAttribute("width", "0");
			node.setAttribute("height", "0");
			
			Element annotationElement = new Element("annotation");
			annotationElement.addContent(a.getText());
			node.addContent(annotationElement);
		}

		return node;*/
	}

	private Element buildXMLTreeFromGGroup(GGroup group) throws IOException {
		Element node = new Element("g");
		
		for (GObject obj : group) {
			Element child = dispatch(obj);
			if (child != null) node.addContent(child);
		}

		return node;
	}
	
	private Element buildXMLTreeFromGFile(GFile file) throws IOException {
		Element node = new Element("g");
		//setTransform(node, file.getTransform());
		
		/*for (GObjectShape obj : file.getContent().getChildren()) {
			Element child = dispatch(obj);
			if (child != null) node.addContent(child);
		}*/

		return node;
	}
	
	private static void setBounds(Element node, GBounds2D b) {
		node.setAttribute("x", b.x + "");
		node.setAttribute("y", b.y + "");
		node.setAttribute("width", b.width + "");
		node.setAttribute("height", b.height + "");
	}
	
	private static void setPoints(Element node, ArrayList<PointDouble> points) {
		String value = "";
		for (PointDouble p : points) {
			value += p.x + "," + p.y + " ";
		}
		node.setAttribute("points", value);
	}
	
	private static void setFill(Element node, Color c) {
		node.setAttribute("fill", EncUtils.encColor(c));
	}
	
	private static void setStroke(Element node, Color color, double strokeWidth) {
		node.setAttribute("stroke", EncUtils.encColor(color));
		node.setAttribute("stroke-width", strokeWidth + "");
		//node.setAttribute("stroke-linecap", "square");
		//node.setAttribute("stroke-linejoin", "miter");
	}
	
	private static void setTransform(Element node, AffineTransform af) {
		node.setAttribute("transform", EncUtils.encTransform(af));
	}
	
}

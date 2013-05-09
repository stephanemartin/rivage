package fr.inria.rivage.engine.svg.decoder.attdec;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.shapes.GPath;
import fr.inria.rivage.engine.svg.decoder.DecUtils;
import fr.inria.rivage.engine.svg.decoder.DecodeLogger;
import java.awt.Color;
import java.util.ArrayList;


/**
 * With this decoder you can create a list of polygon and polyline shapes
 * from a SVG path description. The specification of the SVG path element
 * can be found at http://www.w3.org/TR/SVG/paths.html.
 * Curves are decoded as straight lines since we don't provide curves.
 * @author Tobias Kuhn
 */
public class PathDescDec {
	
	private String svgPath;
	private ArrayList<GObject> objs = new ArrayList<GObject>();
	private ArrayList<PointDouble> points = new ArrayList<PointDouble>();
	private PointDouble lastPoint = new PointDouble(0,0);
	private DecodeLogger decodeLogger;
	
	private PathDescDec(String svgPath, DecodeLogger decodeLogger) {
		this.svgPath = svgPath;
		this.decodeLogger = decodeLogger;
	}
	
	/**
	 * Decodes the SVG path element and returns a list of polygon and polyline
	 * shapes. Warnings are written to the decode logger.
	 * @param svgPath the SVG path element
	 * @param decodeLogger the decode logger where the warnings get written
	 * @return a list with polygon and polyline shapes
	 */
	public static ArrayList<GObject> decode(String svgPath, DecodeLogger decodeLogger) {
		PathDescDec pathDecoder = new PathDescDec(svgPath, decodeLogger);
		return pathDecoder.decode();
	}
	
	private ArrayList<GObject> decode() {
		
		while (true) {	
			svgPath = DecUtils.clean(svgPath);

			// END
			if (svgPath.equals("")) {
				if (points.size() > 0) {
					//GPolyLine p = new GPolyLine();
					//p.setPoints(points);
					//objs.plus(p);
				}
				break;
			}
			
			char c = svgPath.charAt(0);
			int nc = nextCommandIndex(svgPath);
			ArrayList<Double> d = NumbersDec.decode(svgPath.substring(1, nc));
			svgPath = svgPath.substring(nc);

			// MOVETO (absolute and relative)
			if (c == 'M' || c == 'm') {
				// if there are points left, create a polyline shape
				if (points.size() > 0) {
					//GPolyLine p = new GPolyLine();
					//p.setPoints(points);
					//objs.plus(p);
				}
				points = new ArrayList<PointDouble>();
				if (d.size() < 2) {
					decodeLogger.putWarning("Illegal moveto command.");
					continue;
				}
				if (c == 'M') { // absolute point description
					addPoint(new PointDouble(d.get(0), d.get(1)));
				} else { // relative point description
					addPoint(new PointDouble(lastPoint.x + d.get(0), lastPoint.y + d.get(1)));
				}
				// if there are more points, they are interpreted as lineto commands
				for (int i=1; i < d.size() / 2; i++) {
					if (c == 'M') { // absolute point description
						addPoint(new PointDouble(d.get(i*2), d.get(i*2 + 1)));
					} else { // relative point description
						addPoint(new PointDouble(lastPoint.x + d.get(i*2), lastPoint.y + d.get(i*2 + 1)));
					}
				}
				
			// CLOSEPATH
			} else if (c == 'Z' || c == 'z') {
				if (points.size() == 0 || d.size() > 0) {
					decodeLogger.putWarning("Illegal closepath command.");
					continue;
				}
				GPath p = new GPath(null, null, Color.yellow, Color.yellow, null, true)	;			//p.setPoints(points);
				objs.add(p);
				points = new ArrayList<PointDouble>();
				
			// LINETO (absolute)
			} else if (c == 'L') {
				for (int i=0; i < d.size() / 2; i++) {
					addPoint(new PointDouble(d.get(i*2), d.get(i*2 + 1)));
				}
			
			// LINETO (relative)
			} else if (c == 'l') {
				for (int i=0; i < d.size() / 2; i++) {
					addPoint(new PointDouble(lastPoint.x + d.get(i*2), lastPoint.y + d.get(i*2 + 1)));
				}
				
            // HORIZONTAL LINETO (absolute)
			} else if (c == 'H') {
				for (int i=0; i < d.size(); i++) {
					addPoint(new PointDouble(d.get(i), lastPoint.y));
				}
				
			// HORIZONTAL LINETO (relative)
			} else if (c == 'h') {
				for (int i=0; i < d.size(); i++) {
					addPoint(new PointDouble(lastPoint.x + d.get(i), lastPoint.y));
				}
				
            // VERTICAL LINETO (absolute)
			} else if (c == 'V') {
				for (int i=0; i < d.size(); i++) {
					addPoint(new PointDouble(lastPoint.x, d.get(i)));
				}
				
			// VERTICAL LINETO (relative)
			} else if (c == 'v') {
				for (int i=0; i < d.size(); i++) {
					addPoint(new PointDouble(lastPoint.x, lastPoint.y + d.get(i)));
				}
				
			// CURVETO (absolute)
			} else if (c == 'C') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 6; i++) {
					addPoint(new PointDouble(d.get(i*6 + 4), d.get(i*6 + 5)));
				}
				
			// CURVETO (relative)
			} else if (c == 'c') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 6; i++) {
					addPoint(new PointDouble(lastPoint.x + d.get(i*6 + 4), lastPoint.y + d.get(i*6 + 5)));
				}
				
			// SHORTHAND CURVETO (absolute)
			} else if (c == 'S') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 4; i++) {
					addPoint(new PointDouble(d.get(i*4 + 2), d.get(i*4 + 3)));
				}
				
			// SHORTHAND CURVETO (relative)
			} else if (c == 's') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 4; i++) {
					addPoint(new PointDouble(lastPoint.x + d.get(i*6 + 2), lastPoint.y + d.get(i*6 + 3)));
				}
			
			// QUADRATIC CURVETO (absolute)
			} else if (c == 'Q') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 4; i++) {
					addPoint(new PointDouble(d.get(i*4 + 2), d.get(i*4 + 3)));
				}
				
			// QUADRATIC CURVETO (relative)
			} else if (c == 'q') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 4; i++) {
					addPoint(new PointDouble(lastPoint.x + d.get(i*4 + 2), lastPoint.y + d.get(i*4 + 3)));
				}
				
			// SHORTHAND QUADRATIC CURVETO (absolute)
			} else if (c == 'T') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 2; i++) {
					addPoint(new PointDouble(d.get(i*2), d.get(i*2 + 1)));
				}
				
			// SHORTHAND QUADRATIC CURVETO (relative)
			} else if (c == 't') {
				decodeLogger.putWarning("Curves are imported as straight lines.");
				for (int i=0; i < d.size() / 2; i++) {
					addPoint(new PointDouble(lastPoint.x + d.get(i*2), lastPoint.y + d.get(i*2 + 1)));
				}
				
			// UNKNOWN
			} else {
				decodeLogger.putWarning("Unknown path command.");
			}
		}
				
		return objs;
	}
	
	private int nextCommandIndex(String s) {
		for (int i = 1; i < s.length(); i++) {
			if ("MmZzLlHhVvCcSsQqTt".indexOf(s.charAt(i)) > -1) {
				return i;
			}
		}
		return s.length();
	}
	
	private void addPoint(PointDouble p) {
		points.add(p);
		lastPoint = new PointDouble(p);
	}

}

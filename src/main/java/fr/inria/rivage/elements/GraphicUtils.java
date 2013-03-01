/*
 * Created on May 7, 2004
 */
package fr.inria.rivage.elements;

import fr.inria.rivage.tools.Configuration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * @author Yves
 */
public class GraphicUtils {
	
	private static Logger log = Logger.getLogger(GraphicUtils.class); 
	
	public static void drawHandle(Graphics2D g, double centerx, double centery) {
		int s = Configuration.getConfiguration().SEL_MARK_SIZE;
		setSelectionColor(g);
		AffineTransform a = g.getTransform();
		g.setTransform(new AffineTransform());
		Point2D p = new PointDouble();
		a.transform(new PointDouble(centerx, centery), p);
		g.fillRect((int) p.getX() - s/2, (int) p.getY() - s/2, s, s);
		g.setTransform(a);
		g.setPaintMode();
	}
	
	public static void drawHandle(Graphics2D g, Point2D p) {
		drawHandle(g, p.getX(), p.getY());
	}
	
        public static void drawColor(Graphics2D g,Color c,Shape sh){
            Color bak=g.getColor();
            g.setColor(c);
            g.draw(sh);
            g.setColor(bak);
            
        }
        
        
        
	/*public static GObjectContainer getDeepestCommonParent(GObject obj1, GObject obj2) {
		if (obj1 == null || obj2 == null) return null;
		ArrayList<GObjectContainer> parents = new ArrayList<GObjectContainer>();
		
		GObjectContainer parent1 = (obj1 instanceof GObjectContainer ? (GObjectContainer) obj1 : obj1.getParent());
		while (parent1 != null) {
			parents.add(parent1);
			parent1 = parent1.getParent();
		}
		
		GObjectContainer parent2 = (obj2 instanceof GObjectContainer ? (GObjectContainer) obj2 : obj2.getParent());
		while (parent2 != null) {
			if (parents.contains(parent2)) return parent2;
			parent2 = parent2.getParent();
		}
		return null;
	}*/
	
	public static GBounds2D getBounds(ArrayList<GObjectShape> objs) {
		if (objs.isEmpty()) return null;
		/*GBounds2D bounds = (GBounds2D) objs.get(0).getScreenBounds2D().clone();
		for (GObjectShape o : objs) {
			bounds.plus(o.getScreenBounds2D());
		}
		return bounds;*/
                throw new UnsupportedOperationException("not yet");
	}
	
	public static void setSelectionColor(Graphics2D g) {
		Configuration conf = Configuration.getConfiguration();
		g.setColor(conf.MARKEE_COLOR);
		g.setXORMode(conf.MARKEE_COLOR_XOR);
		g.setStroke(new BasicStroke(0));
	}

	public static Point2D reverseTransformPoint(AffineTransform af, double x,
			double y) {
		Point2D po = new PointDouble(x, y);
		try {
			af.inverseTransform(po, po);
		} catch (NoninvertibleTransformException ex) {
			log.error("Error in reverseTransformPoint, some point couldn't be reverse transformed.",ex);
		}
		return po;
	}
	
	public static Point2D reverseTransformPoint(AffineTransform af, Point2D p) {
		return reverseTransformPoint(af,p.getX(),p.getY());
	}
	
	public static double getAngleD(AffineTransform a) {
		return radToDeg(getAngleR(a));
	}
	
	public static double getAngleR(AffineTransform a) {
		double[] m = new double[4];
		a.getMatrix(m);
		double result = Math.atan(m[1]/m[0]);
		if (m[0] < 0) result += Math.PI; else if (m[1] < 0) result += 2*Math.PI;
		return result;
	}
	
	public static double degToRad(double angle) {
		return (angle / 360) * 2*Math.PI;
	}
	
	public static double radToDeg(double angle) {
		return (angle / (2*Math.PI)) * 360;
	}
	
	public static double cut(double value, int d) {
		return Math.rint(value * Math.pow(10, d)) / Math.pow(10, d);
	}
	public static Rectangle2D MakeRect(Point2D p1,Point2D p2){
            double leftupperx = (p1.getX() < p2.getX() ? p1.getX() : p2.getX());
            double leftuppery = (p1.getY() < p2.getY() ? p1.getY() : p2.getY());
            double rightlowerx = (p1.getX() > p2.getX() ? p1.getX() : p2.getX());
            double rightlowery = (p1.getY() > p2.getY() ? p1.getY() : p2.getY());
            return new Rectangle2D.Double(leftupperx, leftuppery, rightlowerx-leftupperx, rightlowery-leftuppery);
        }
}
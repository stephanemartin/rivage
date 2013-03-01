package fr.inria.rivage.elements.handlers.selection;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.tools.Configuration;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class is used by the GSelectionHandler. It describes a selected
 * graphical object.
 * @author Tobias Kuhn
 */
public class SelectedObject {
	
	private GObjectShape original;
	private GObjectShape copy;
	private ArrayList<SelectionMark> selMarks;
	
	/**
	 * Creates a new SelectedObject that represents the given graphical
	 * object. This graphical object is stored as a reference to the
	 * original object and as a copy.
	 * @param original the graphical object
	 */
	public SelectedObject(GObjectShape original) throws CloneNotSupportedException {
		this.original = original;
		this.copy = (GObjectShape) original.clone();
		this.selMarks = SelectionMark.createSelectionMarks(this);
	}
	
	/**
	 * Returns the original object.
	 * @return the original object.
	 */
	public GObjectShape getOriginal() {
		return original;
	}
	
	/**
	 * Returns the copy of the graphical object
	 * @return the copy of the object
	 */
	public GObjectShape getCopy() {
		return copy;
	}
	
	/**
	 * Returns the selection mark for this object at the given point. If
	 * there is no selection mark at this point, null is returned.
	 * @param point the position of the selection mark
	 * @param pointTolerance the tolerance around the point
	 * @return the selection mark at this point or null if there is no one.
	 */
	public SelectionMark getSelMark(Point2D point, double pointTolerance) {
		for (SelectionMark sm : selMarks) {
			if (!sm.isVisible()) continue;
			if (sm.getPosition().distance(point) <= pointTolerance) {
				return sm;
			}
		}
		return null;
	}
	
	/**
	 * Draws all the selection marks onto the given graphical device.
	 * @param g the graphical device to draw on
	 */
	public void drawSelectionMarks(Graphics2D g) {
		int min = Configuration.getConfiguration().MIN_SEL_RECT;
		
		Rectangle2D b = copy.getScreenBounds2D();
		GraphicUtils.setSelectionColor(g);
		
		AffineTransform a = g.getTransform();
		g.setTransform(new AffineTransform());
		PointDouble p1 = new PointDouble();
		PointDouble p2 = new PointDouble();
		a.transform(new PointDouble(b.getMinX(), b.getMinY()), p1);
		a.transform(new PointDouble(b.getMaxX(), b.getMaxY()), p2);
		double width = p2.x - p1.x;
		double height = p2.y - p1.y;
		if (width < min || height < min) {
			g.draw(new GBounds2D(p1.x - min/2, p1.y - min/2, width + min, height + min));
		} else {
			g.draw(new GBounds2D(p1.x, p1.y, width, height));
		}
		g.setTransform(a);
		g.setPaintMode();
		
		for (SelectionMark sm : selMarks) {
			if (!sm.isVisible()) continue;
			sm.draw(g);
		}
	}
	
	/**
	 * Refreshes the copy of the graphical object.
	 */
	public void refreshCopy() throws CloneNotSupportedException {
		copy = (GObjectShape) original.clone();
	}
	
}

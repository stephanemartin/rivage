package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * This class handles the zooming. Left mouse click zooms in, right
 * mouse click zooms out.
 * @author Tobias Kuhn
 */
public class GZoomer extends GHandler {
	
	private WorkArea wa;
	private Point2D firstPoint, lastPoint;
	private double zoomFactor = 1.3;
	
	GZoomer() {}
	
	@Override
	public void init(WorkArea wa) {
		this.wa = wa;
		firstPoint = null;
		lastPoint = null;
		wa.getSelectionManager().clearSelection();
		wa.setCursor(Cursors.zoom);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			wa.zoom(zoomFactor, e.getPoint());
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			wa.zoom(1 / zoomFactor, e.getPoint());
		}
		firstPoint = null;
		lastPoint = null;
		wa.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		lastPoint = wa.getDrawingPoint(e.getPoint());
		wa.lightRepaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		firstPoint = wa.getDrawingPoint(e.getPoint());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (lastPoint == null) return;
		Rectangle2D rect = getZoomRectangle();
		if (rect.getWidth()*rect.getHeight()*wa.getZoom()*wa.getZoom() > 50) {
			double factor = Math.min((wa.getWidth()/wa.getZoom())/rect.getWidth(), (wa.getHeight()/wa.getZoom())/rect.getHeight());
			wa.zoom(factor, wa.getScreenPoint(new PointDouble(rect.getCenterX(), rect.getCenterY())));
		}
		firstPoint = null;
		lastPoint = null;
		wa.repaint();
	}
	
	@Override
	public void draw(Graphics2D g) {
		GraphicUtils.setSelectionColor(g);
		if (lastPoint != null) {
			g.draw(getZoomRectangle());
		}
		g.setPaintMode();
	}
	
	private Rectangle2D getZoomRectangle() {
		double x = Math.min(firstPoint.getX(), lastPoint.getX());
		double y = Math.min(firstPoint.getY(), lastPoint.getY());
		double w = Math.abs(firstPoint.getX() - lastPoint.getX());
		double h = Math.abs(firstPoint.getY() - lastPoint.getY());
		return new Rectangle2D.Double(x, y, w, h);
	}
	
}

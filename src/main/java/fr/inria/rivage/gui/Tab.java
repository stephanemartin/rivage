package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import javax.swing.JScrollBar;


public class Tab extends JPanel implements AdjustmentListener {
	
	private final double MIN_ZOOM = .1;
	private final double MAX_ZOOM = 16;
	
	private WorkArea workArea;
	private double zoom;
	private JScrollBar hScrollBar, vScrollBar;
	
	public Tab(WorkArea workArea) {
		super(new BorderLayout());
		this.workArea = workArea;
		zoom = 1;
		
		if (workArea.getTab() != null) {
			throw new RuntimeException("Cannot create tab: Workarea already in use.");
		}
		workArea.setTab(this);
		
		add(hScrollBar = new JScrollBar(JScrollBar.HORIZONTAL), BorderLayout.SOUTH);
		hScrollBar.addAdjustmentListener(this);
		add(vScrollBar = new JScrollBar(JScrollBar.VERTICAL), BorderLayout.EAST);
		vScrollBar.addAdjustmentListener(this);
		
		add(workArea, BorderLayout.CENTER);
	}
	
	public WorkArea getWorkArea() {
		return workArea;
	}
	
	public void refresh() {
		Dimension drawingSize = workArea.getPage().getDimension();
		hScrollBar.setMaximum((int) (drawingSize.width * workArea.getZoom()));
		vScrollBar.setMaximum((int) (drawingSize.height * workArea.getZoom()));
		hScrollBar.setVisibleAmount(workArea.getWidth());
		vScrollBar.setVisibleAmount(workArea.getHeight());
		hScrollBar.setBlockIncrement(workArea.getWidth());
		vScrollBar.setBlockIncrement(workArea.getHeight());
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
		if (this.zoom < MIN_ZOOM) {
                this.zoom = MIN_ZOOM;
            }
		if (this.zoom > MAX_ZOOM) {
                this.zoom = MAX_ZOOM;
            }
		workArea.repaint();
		Application.getApplication().getMainFrame().getZoomToolbar().refresh();
	}
	
	public void zoom(double zoomFactor, Point2D centerPoint) {
		double newZoom = this.zoom * zoomFactor;
		if (newZoom < MIN_ZOOM) {
                newZoom = MIN_ZOOM;
            }
		if (newZoom > MAX_ZOOM) {
                newZoom = MAX_ZOOM;
            }
		zoomFactor = newZoom / this.zoom;
		if (this.zoom == newZoom) {
                return;
            }
		this.zoom = newZoom;
				
		refresh();
		
		hScrollBar.setValue((int) ((centerPoint.getX()+hScrollBar.getValue())*zoomFactor - getWidth()/2));
		vScrollBar.setValue((int) ((centerPoint.getY()+vScrollBar.getValue())*zoomFactor - getHeight()/2));
		
		workArea.repaint();
		Application.getApplication().getMainFrame().getZoomToolbar().refresh();
	}

    @Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		workArea.repaint();	
	}
	
	public int getScrollPosX() {
		return hScrollBar.getValue();
	}

	public int getScrollPosY() {
		return vScrollBar.getValue();
	}

}

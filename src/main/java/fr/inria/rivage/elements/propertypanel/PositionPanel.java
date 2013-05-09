package fr.inria.rivage.elements.propertypanel;

import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.gui.PropertyPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This is a panel for a set of objects that implement the
 * interface IMovable, in order to be able to change their position, size and
 * angle.
 * 
 * @author Tobias Kuhn
 */
public class PositionPanel extends JPanel implements ActionListener, IPropPanel {

	private ArrayList<GObjectShape> objs = new ArrayList<GObjectShape>();
	private ArrayList<ID> ids = new ArrayList<ID>();
	private GBounds2D bounds;
	
	private LabeledTextField posx, posy, width, height, rotation;
	private PropertyPanel pp;

	/**
	 * Creates a new panel for the set of movable objects.
	 * @param objs the movable objects
	 */
	public PositionPanel(PropertyPanel pp) {
		this.pp = pp;
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		add(content);
		
		content.add(posx = new LabeledTextField("X Pos:", this));
		content.add(posy = new LabeledTextField("Y Pos:", this));
		content.add(width = new LabeledTextField("Width:", this));
		content.add(height = new LabeledTextField("Height:", this));
		content.add(rotation = new LabeledTextField("Rotation:", this));
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		/*if (objs.isEmpty()) return;
		
		IConcurrencyController cc = Application.getApplication().getCurrentFileController().getConcurrencyController();
		
		for (ID id : ids)
		{
			if (e.getSource() == posx) {
				double dx = Float.parseFloat(posx.getText()) - bounds.x;
				cc.doOperation(new TranslateOperation(id, dx, 0));
			} else if (e.getSource() == posy) 
			{
				double dy = Float.parseFloat(posy.getText()) - bounds.y;
				cc.doOperation(new TranslateOperation(id, 0, dy));
			} else if (e.getSource() == width) {
				double fx = Float.parseFloat(width.getText()) / bounds.width;
				cc.doOperation(new ScaleOperation(id, bounds.getCenter(), fx, 1));
			} else if (e.getSource() == height) {
				double fy = Float.parseFloat(height.getText()) / bounds.height;
				cc.doOperation(new ScaleOperation(id, bounds.getCenter(), 1, fy));
			} else if (e.getSource() == rotation) {
				GObjectShape obj = objs.get(0);
				double angle = GraphicUtils.degToRad(Float.parseFloat(rotation.getText())) - obj.getAngle();
				cc.doOperation(new RotateOperation(id, bounds.getCenter(), angle));
			}
		}*/
	}

    @Override
	public void refreshObject() {
		/*objs.clear();
		ids.clear();
		bounds = null;
		
		for (GObjectShape o : pp.getObjects()) {
			objs.add(o);
			ids.add(o.getId());
			if (bounds == null) {
				bounds = o.getScreenBounds2D();
			} else {
				bounds.add(o.getScreenBounds2D());
			}
		}
		
		if (objs.isEmpty()) return;
		pp.addTab("Position", this);
		
		posx.setText(GraphicUtils.cut(bounds.getX(), 2) + "");
		posy.setText(GraphicUtils.cut(bounds.getY(), 2) + "");
		if (isResizable()) {
			width.setVisible(true);
			width.setVisible(true);
			width.setText(GraphicUtils.cut(bounds.getWidth(), 2) + "");
			height.setText(GraphicUtils.cut(bounds.getHeight(), 2) + "");
		} else {
			width.setVisible(false);
			width.setVisible(false);
		}
		if (hasAngle()) {
			rotation.setVisible(true);
			rotation.setText(GraphicUtils.cut(GraphicUtils.radToDeg(objs.get(0).getAngle()), 2) + "");
		} else {
			rotation.setVisible(false);
		}*/
	}
	
	/**
	 * Indicates whether the set of objects is resizable or not.
	 * It's not resizable only if there the list contains only one object and this
	 * object is not resizable. Otherwise the set of objects is resizable even if
	 * it contains unresizable objects.
	 * @return true if the set of objects is resizable
	 */
	private boolean isResizable() {
		//return (objs.size() > 1 || objs.get(0).isResizable());
                throw new UnsupportedOperationException(("fuck"));
	}
	
	/**
	 * Indicates whether the set of objects has an angle or not.
	 * It has an angle only if the list contains only one object and this object has
	 * an angle. Otherwise there is no angle defined.
	 * @return true if the set of objects has an angle
	 */
	private boolean hasAngle() {
            throw new UnsupportedOperationException(("fuck"));
		//return (objs.size() == 1 && objs.get(0).hasAngle());
	}
	
}
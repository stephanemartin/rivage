/*
 * Created on Aug 26, 2004
 */
package fr.inria.rivage.elements.propertypanel;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GraphicUtils;
import fr.inria.rivage.elements.interfaces.IStrokable;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.gui.PropertyPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This is a panel for a set of objects that implement the interface IStrokable,
 * in order to be able to change their line width.
 * 
 * @author Yves
 * @author Tobias Kuhn
 */
public class StrokePanel extends JPanel implements ActionListener, IPropPanel {

	private List<GObject> objs = new ArrayList();

	private PropertyPanel pp;

	private LabeledTextField strokew;

	/**
	 * Creates a new panel for the set of strokable objects.
	 * 
	 * @param objs
	 *            the strokable objects
	 */
	public StrokePanel(PropertyPanel pp) {
		this.pp = pp;

		setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		add(content);

		content.add(strokew = new LabeledTextField("Stroke width:", this));
	}

    @Override
	public void actionPerformed(ActionEvent e) {
		if (objs.isEmpty())
			return;

		if (e.getSource() == strokew) {
			float strokeSize = Float.parseFloat(strokew.getText());
                        
			IConcurrencyController cc = Application.getApplication()
					.getCurrentFileController().getConcurrencyController();
			for (GObject obj : objs) 
			{
                            
			/*	cc.doAndSendOperation(new ModifyOperation(obj, Parameters.ParameterType.Stroke
						strokeSize));*/
			}
		}
	}

    @Override
	public void refreshObject() {
		objs.clear();
		//objs.addAll(pp.getObjects());
		if (objs.isEmpty())
			return;
		//pp.addTab("Stroke", this);

		if (! (objs.get(0) instanceof IStrokable)) 
		{
			strokew.setText ("");
			return;
		}
		double stroke = GraphicUtils.cut(((SerBasicStroke) ((IStrokable)objs.get(0))
				.getStroke()).getLineWidth(), 2);
		for (GObject obj : objs) 
		{
			if (!(obj instanceof IStrokable)) 
			{
				strokew.setText ("");
				return;			
			}
			
			double nstroke = GraphicUtils.cut(
					((SerBasicStroke) ((IStrokable)obj).getStroke()).getLineWidth(), 2);
			if (stroke != nstroke) {
				strokew.setText("");
				return;
			}
			stroke = nstroke;
		}

		strokew.setText(stroke + "");
	}
}
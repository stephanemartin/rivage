package fr.inria.rivage.elements.propertypanel;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.gui.PropertyPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * This panel displays the ID and the z-position of an object.
 * 
 * @author Tobias Kuhn
 */
public class DebugPanel extends JPanel implements ActionListener, IPropPanel {

	private GObject obj;
	private PropertyPanel pp;
	
	private LabeledTextField idField, zField;

	public DebugPanel(PropertyPanel pp) {
		this.pp = pp;
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		add(content);
		
		content.add(idField = new LabeledTextField("ID:", this));
		content.add(zField = new LabeledTextField("Z Pos:", this));
		
		idField.setEditable(false);
		zField.setEditable(false);
	}

    @Override
	public void actionPerformed(ActionEvent e) {
	}

    //@Override
	/*public void refreshObject() {
		if (pp.getObjects().size() != 1) return;
		
		obj = pp.getObjects().get(0);
		pp.addTab("Debug", this);
		
		idField.setText(obj.getId() + "");
		//zField.setText(obj.getZ() + "");
	}*/

    public void refreshObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
}
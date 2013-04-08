package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;

public class ThicknessBar extends JToolBar implements
        ActionListener, CurrentWorkAreaListener, SelectionChangeListener {

    private JComboBox cb;
    private JComboBox cb2;
    private float[][] predefinedStrokeStyle = {{10.0f}, {10.0f, 5.0f}};
    private float[] predefinedStrokeStylePhase = {10.0f, 10.0f};
    private SerBasicStroke pedefinedStroke[];
    private SerBasicStroke stroke;
    private boolean enabled = false;

    public ThicknessBar() {
        super("Thickness", JToolBar.HORIZONTAL);

        pedefinedStroke = new SerBasicStroke[predefinedStrokeStyle.length];



        cb = new JComboBox(new Integer[]{1, 2, 4, 6, 8, 12, 16, 18, 20, 22});
        cb.setEditable(true);
        cb.addActionListener(this);
        
        add(cb);
        //cb.setMaximumSize(new Dimension(10, 20));
        //cb.setMaximumSize(new Dimension(cb.getPreferredSize().height,30));        


        add(new JLabel("px"));
        cb2 = new JComboBox(pedefinedStroke);
        //cb2.setRenderer(new Renderer());

        cb2.addActionListener(this);

        add(cb2);
         setMaximumSize(getPreferredSize());


        InnerWindow.addWorkAreaListener(this);

        refresh();
    }

    public void refresh() {
        try {
            cb.setEnabled(true);
            cb2.setEnabled(true);
            WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
            GGroup group = wa.getSelectionManager().getOpenGroup();
            if (group.size() > 0) {
                stroke = (SerBasicStroke) group.getParameters().getObject(Parameters.ParameterType.Stroke);
                cb.getEditor().setItem(stroke.getLineWidth());
            }
            cb2.getEditor().setItem(stroke);
        } catch (NullPointerException ex) {

            //cb.getEditor().setItem(zoomPct);
            cb.setEnabled(false);
            cb2.setEnabled(false);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = cb.getEditor().getItem().toString();
        try {
            float z = Float.parseFloat(s);
            WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
            wa.updateColors(null, null, stroke);
        } catch (NumberFormatException ex) {
            //cb.getEditor().setItem(strokeSize);
        }
    }

    @Override
    public void currentWorkAreaChanged() {
        refresh();
    }

    public Stroke genStroke() {
        return null;
    }

    public void selectionChanged() {
        refresh();
    }

    class Renderer extends JPanel implements ListCellRenderer {

        SerBasicStroke str;

        public Renderer() {
        }

        @Override
        public void paint(Graphics g) {
            // if (enabled) {
            g.setColor(Color.WHITE);
            /*} else {
             g.setColor(Color.LIGHT_GRAY);
             }*/
            double y = this.getHeight() / 2.0;
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(str);
            Line2D line = new Line2D.Double(0, y, this.getWidth(), y);
            g2.draw(line);
            g.setColor(Color.GRAY);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // return new Renderer((SerBasicStroke) value);
            this.str = (SerBasicStroke) value;
            return this;
        }
    }
}
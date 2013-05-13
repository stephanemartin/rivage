package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

final public class StrokeBar extends JToolBar implements
        ActionListener, TreeChangeListener, SelectionChangeListener, CurrentWorkAreaListener {

    private JComboBox<Float> cb;
    private StrokeCombo sc;
    //private float[] predefinedStrokeStylePhase = {10.0f, 10.0f};
    private SerBasicStroke stroke;
    private JoinSel js;

    public StrokeBar() {
        super("Thickness", JToolBar.HORIZONTAL);
        //pedefinedStroke = new SerBasicStroke[predefinedStrokeStyle.length];
        Float[] sets = new Float[24];
        float init = 0.5f;
        for (int i = 0; i < sets.length; i++) {
            sets[i] = init;
            init += 0.5f;
        }
        cb = new JComboBox(sets);
        cb.setSelectedIndex(3);
        cb.setEditable(true);
        cb.addActionListener(this);

        add(cb);
        //cb.setMaximumSize(new Dimension(10, 20));
        //cb.setMaximumSize(new Dimension(cb.getPreferredSize().height,30));        


        add(new JLabel("px "));
        sc = new StrokeCombo(2);
        add(sc);
        js = new JoinSel(this);



        setMaximumSize(getPreferredSize());
        SelectionManager.addGeneralSelectionListener(this);
        InnerWindow.addWorkAreaListener(this);
        refresh();
    }

    public void refresh() {
        try {
            cb.setEnabled(true);
            sc.setEnabled(true);
            js.setEnabled(true);
            WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
            GGroup group = wa.getSelectionManager().getOpenGroup();
            if (group.size() > 0) {
                stroke = (SerBasicStroke) group.getParameters().getObject(Parameters.ParameterType.Stroke);
                cb.getEditor().setItem(stroke.getLineWidth());
                js.setJoint(stroke.getLineJoin());
                
            }
            sc.setSelected(stroke);
        } catch (NullPointerException ex) {

            //cb.getEditor().setItem(zoomPct);
            cb.setEnabled(false);
            sc.setEnabled(false);
            js.setEnabled(false);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = cb.getEditor().getItem().toString();
        try {
            float z = Float.parseFloat(s);
            WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
            // wa.updateColors(null, null, stroke);
            sc.updateSize(z);
        } catch (NumberFormatException ex) {
            //cb.getEditor().setItem(strokeSize);
        }
    }

    @Override
    public void currentWorkAreaChanged() {
        refresh();
    }

    public void selectionChanged() {
        if (Application.getApplication().getCurrentFileController().getCurrentWorkArea().getSelectionManager().getOpenGroup().size() > 0) {
            refresh();
        }
    }

    public Stroke getStrock() {
        return sc.getSelectedStroke();
    }

    public void treeChanged() {
        //refresh();
    }

    class JoinSel implements ActionListener {

        ButtonGroup radioGroup = new ButtonGroup();
        JToggleButton[] buttons;
        int[] values = new int[]{BasicStroke.JOIN_MITER, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_ROUND};
        String[] text = new String[]{ "miter","bevel", "round"};

        public JoinSel(JComponent parent) {
            buttons = new JToggleButton[values.length];
            boolean coche = true;
            for (int i = 0; i < values.length; i++) {
                ImageIcon im=new ImageIcon(Application.class.getResource("resources/images/"+text[i]+"16.png"));
                String nameDesc="Set join style to "+text[i];
                buttons[i] = new JToggleButton(im,coche);
                buttons[i].setToolTipText(nameDesc);
                        /*new JRadioButton(new ButtonsJoin(text[i]));*/
                //buttons[i].setSelected(coche);
                coche = false;
                buttons[i].setActionCommand("" + values[i]);
                buttons[i].addActionListener(this);
                parent.add(buttons[i]);
                radioGroup.add(buttons[i]);
            }
        }
        public void setEnabled(boolean b){
           for (int i=0;i<buttons.length;i++){
               buttons[i].setEnabled(b);
           }
        }
        public void setJoint(int i) {
            for (int j = 0; j < values.length; j++) {
                if (values[j] == i) {
                    buttons[j].setSelected(true);
                }
            }
        }

        public void actionPerformed(ActionEvent ae) {
            int i = Integer.parseInt(radioGroup.getSelection().getActionCommand());
            StrokeBar.this.sc.setJoinType(i);
        }
    }
}
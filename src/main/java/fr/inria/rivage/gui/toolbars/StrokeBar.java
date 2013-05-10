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
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;

final public class StrokeBar extends JToolBar implements
        ActionListener, TreeChangeListener, SelectionChangeListener, CurrentWorkAreaListener {

    private JComboBox<Float> cb;
    private StrokeCombo sc;
    
    //private float[] predefinedStrokeStylePhase = {10.0f, 10.0f};
    
    private SerBasicStroke stroke;
    

    public StrokeBar() {
        super("Thickness", JToolBar.HORIZONTAL);
        //pedefinedStroke = new SerBasicStroke[predefinedStrokeStyle.length];
        Float[] sets=new Float[24];
        float init=0.5f;
        for(int i=0 ;i<sets.length;i++){
            sets[i]=init;
            init+=0.5f;
        }
        cb = new JComboBox(sets);
        cb.setSelectedIndex(3);
        cb.setEditable(true);
        cb.addActionListener(this);

        add(cb);
        //cb.setMaximumSize(new Dimension(10, 20));
        //cb.setMaximumSize(new Dimension(cb.getPreferredSize().height,30));        


        add(new JLabel("px "));
        sc=new StrokeCombo(2);
        add(sc);
                //cb2.setRenderer(new Renderer());

        
        setMaximumSize(getPreferredSize());
        SelectionManager.addGeneralSelectionListener(this);
        InnerWindow.addWorkAreaListener(this);
        refresh();
    }
    
            
    public void refresh() {
        try {
            cb.setEnabled(true);
            sc.setEnabled(true);
            WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
            GGroup group = wa.getSelectionManager().getOpenGroup();
            if (group.size() > 0) {
                stroke = (SerBasicStroke) group.getParameters().getObject(Parameters.ParameterType.Stroke);
                cb.getEditor().setItem(stroke.getLineWidth());
            }
            sc.setSelected(stroke);
        } catch (NullPointerException ex) {

            //cb.getEditor().setItem(zoomPct);
            cb.setEnabled(false);
            sc.setEnabled(false);

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
        if(Application.getApplication().getCurrentFileController().getCurrentWorkArea().getSelectionManager().getOpenGroup().size()>0){
        refresh();
        }
    }

    public Stroke getStrock(){
        return sc.getSelectedStroke();
    }

    public void treeChanged() {
        //refresh();
    }
}
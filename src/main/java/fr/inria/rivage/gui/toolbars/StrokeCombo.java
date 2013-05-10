/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2013 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class StrokeCombo extends JPanel {

    private JComboBox selector;
    ComboModel model;

    @Override
    public void setEnabled(boolean b){
        this.selector.setEnabled(b);
    }
    public static float[] mult(float size,float[] f) {
            if (f == null) {
                return null;
            }
            float ret[] = new float[f.length];
            for (int i = 0; i < f.length; i++) {
                ret[i] = f[i] * size;
            }
            return ret;
        }
    
    public void updateSize(float size) {
        model.setSize(size);
        SerBasicStroke str = (SerBasicStroke) model.getSelectedItem();
        if (str != null) {

            model.setSelectedItem(new SerBasicStroke(size,
                    str.getEndCap(),
                    str.getLineJoin(),
                    str.getMiterLimit(),
                    mult(size/str.getLineWidth(),str.getDashArray()),
                    str.getDashPhase()));
        }
        model.update(this);
        upSelObject();
    }

    public void upSelObject(){
        WorkArea wa=Application.getApplication().getCurrentFileController().getCurrentWorkArea();
        Parameters p= wa.getSelectionManager().getSelParameters();
        if(p!=null){
            p.setObject(Parameters.ParameterType.Stroke, this.getSelectedStroke());
            p.sendMod();
            wa.treeChanged();
        }
    }
    
    public void setSelected(Stroke st) {
        if (st == null) {
            st = (Stroke) model.getElementAt(0);
        }
        model.setSelectedItem(st);
        model.update(st);
    }

    public StrokeCombo(int initSize) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(150, 18));
        setMinimumSize(new Dimension(150, 18));
        model = new ComboModel(initSize);
        selector = new JComboBox(model);
        selector.setRenderer(new StrokeRenderer());
        add(selector);
        selector.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent ae) {
                upSelObject();
            }
            
        });
    }

    protected final JComboBox getSelector() {
        return this.selector;
    }

    public Stroke getSelectedStroke() {
        return (Stroke) this.selector.getSelectedItem();
    }

    static class ComboModel implements ComboBoxModel {

        private float[][] predefinedStrokeStyle = {
            null,
            {5.0f, 2.0f},
            {2.0f, 2.0f},
            {5.0f, 2.0f, 2.0f, 2.0f},
            {5.0f, 2.0f, 2.0f, 2.0f, 2.0f, 2.0f},
            {2.0f, 5.0f},
            {5.0f, 5.0f},
            {10.0f, 5.0f},
            {5.0f, 5.0f, 2.0f, 5.0f},
            {10.0f, 5.0f, 2.0f, 5.0f}
        };
        private int[] pcap={BasicStroke.CAP_BUTT,BasicStroke.CAP_ROUND};
        Object selected;
        LinkedList<ListDataListener> lls = new LinkedList();
        float size;

        

        public void setSize(float size) {
            this.size = size;
        }

        public ComboModel(int initSize) {
            super();
            this.size = initSize;
        }

        public void setSelectedItem(Object o) {
            this.selected = o;
            //System.out.println("test");
        }

        public Object getSelectedItem() {
            return selected;
        }

        public int getSize() {
            return predefinedStrokeStyle.length*pcap.length;
        }

        public Object getElementAt(int i) {
            /* if (i == 0) {
             return new SerBasicStroke(size);
             } else {*/
            return new SerBasicStroke(size,  pcap[i%pcap.length], BasicStroke.JOIN_MITER,size<1?1: size, mult(size,predefinedStrokeStyle[i/pcap.length]), 0);
            //}
        }

        public void update(Object o) {
            for (ListDataListener ll : lls) {
                ll.contentsChanged(new ListDataEvent(o, ListDataEvent.CONTENTS_CHANGED, 0, predefinedStrokeStyle.length - 1));
            }
            //this.fireContentsChanged(o, 0, predefinedStrokeStyle.length - 1);
        }
        
        public void addListDataListener(ListDataListener ll) {
            lls.add(ll);
        }

        public void removeListDataListener(ListDataListener ll) {
            lls.remove(ll);
        }
    }

    
}

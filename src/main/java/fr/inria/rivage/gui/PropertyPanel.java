/*
 * Created on May 7, 2004
 */
package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.tools.Parameter;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.toolbars.StrokeRenderer;
import fr.inria.rivage.tools.JColorShow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * This is the class creates a property panel for a set of objects where the
 * user can change the properties like position, size, stroke, colors and text.
 *
 * @author Yves
 * @author Tobias Kuhn
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class PropertyPanel extends JPanel {

    //private ArrayList<IPropPanel> panels = new ArrayList<IPropPanel>();
    class Model extends AbstractTableModel {

        Object[] param;

        public Model(Parameters param) {
            this.param = param.getValues().toArray();
        }

        public int getRowCount() {

            return param.length;

        }

        public int getColumnCount() {
            return 2;
        }
        
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return Parameters.Names[((Parameter) param[rowIndex]).getType().ordinal()];
            } else {
                return ((Parameter) param[rowIndex]);
            }
        }

        @Override
        public boolean isCellEditable(int i, int i1) {
            return i1 == 1;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            super.setValueAt(aValue, rowIndex, columnIndex); //To change body of generated methods, choose Tools | Templates.

            Parameter p = (Parameter) param[rowIndex];
            p.localUpdate(aValue, 0);
            p.sendMod();
            Application.getApplication().getCurrentFileController().getCurrentWorkArea().treeChanged();
        }
    }

    class Maj implements Observer {

        public void update(Observable o, Object o1) {
            // System.out.println("UPDATE --------");
            if (PropertyPanel.this.model != null) {
                PropertyPanel.this.model.fireTableDataChanged();
            }
        }
    }
    private Maj maj = new Maj();
    private Model model;
    //private JTabbedPane jtpane;
    //private List<GObject> objs = new ArrayList<GObject>();
    //private Parameters param;
    private JTable table;
    //private String col[] = {"Propertie", "Value"};
    //private ArrayList<GObject> atomObjs = new ArrayList<GObject>();
    Parameters param;

    public PropertyPanel() {

        setLayout(new BorderLayout());

        update();
    }

    final public void update() {

        //refreshLists();
        //List<GObject> objs;

        //objs.clear();
        //atomObjs.clear();
        if (param != null) {
            param.deleteObserver(maj);
        }
        try {
            param = Application.getApplication().getCurrentFileController()
                    .getCurrentWorkArea().getSelectionManager().getSelParameters();
        } catch (NullPointerException ex) {
            return;
        }

        //addAtomicObjects(objs, atomObjs);



        this.removeAll();
        if (param != null) {
            model = new Model(param);
            table = new JTable(model);
            param.addObserver(maj);
            table.setDefaultEditor(Parameter.class, new MetaEditor());
            //table.setDefaultEditor(Color.class, new );
            //table.setDefaultEditor(Color.class, new ColorEditor());
            
            table.setDefaultRenderer(Parameter.class, new ParameterRenderer());
            /*table.setDefaultRenderer(Color.class, new ColorRenderer());
            table.setDefaultRenderer(Point2D.class, new Point2DRenderer());
            table.setDefaultRenderer(PointDouble.class, new Point2DRenderer());
            table.setDefaultRenderer(Stroke.class, new StrokeRender());
            table.setDefaultRenderer(Double.class, new DoubleRenderer());*/

            this.add(table);
        }
        this.updateUI();


    }
}
/**
 * MetaEditor allow to switch to the appropriated editor depends of data type
 * Normaly, the JTable use same editor kind on column given by the model
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
class MetaEditor /*extends AbstractCellEditor*/
        implements TableCellEditor{
    TableCellEditor cell;
    public Object getCellEditorValue() {
        return cell.getCellEditorValue();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        cell=null;
        Object element=((Parameter)value).getElement();
        if (element instanceof Color)
            cell=new ColorEditord();
        else if(element instanceof String){
            cell=new DefaultCellEditor(new JTextField((String)element));
        }
        return cell==null?null:cell.getTableCellEditorComponent(table,element,isSelected,row,column);
    }   

    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return cell.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing() {
        return cell.stopCellEditing();
    }

    public void cancelCellEditing() {
         cell.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l) {
       cell.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        cell.removeCellEditorListener(l);
    }
}

class ColorEditord extends AbstractCellEditor
        implements TableCellEditor,
        ActionListener {

    Color currentColor;
    JButton button;
    JColorChooser colorChooser;
    JDialog dialog;
    protected static final String EDIT = "edit";

    public ColorEditord() {
        init();
    }
    private void init(){
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        //Set up the dialog that the button brings up.
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button,
                "Pick a Color",
                true, //modal
                colorChooser,
                this, //OK button handler
                null); //no CANCEL button handler
    }
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);

            fireEditingStopped(); //Make the renderer reappear.

        } else { //User pressed dialog's "OK" button.
            currentColor = colorChooser.getColor();
        }
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return currentColor;
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        currentColor = (Color) value;
        return button;
    }
}

class ColorRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new JColorShow((Color) value);
    }
}

class Point2DRenderer implements TableCellRenderer {

    public static final double PRECIS = 100;

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Point2D point = (Point2D) value;
        return new JLabel("" + ((int) point.getX() * PRECIS) / PRECIS + " x " + ((int) point.getY() * PRECIS) / PRECIS);
    }
}

class StrokeRender implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new StrokeRenderer((Stroke) value);
    }
}

class DoubleRenderer implements TableCellRenderer {
    public static final double PRECIS = 100;
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new JLabel("" + ((int) (((Double) value).doubleValue() * 360 * PRECIS / (2 * Math.PI))) / PRECIS + " °");
    }
}
class AngularRenderer implements TableCellRenderer {
    public static final double PRECIS = 100;
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return new JLabel("" + ((int) (((Double) value).doubleValue() * 360 * PRECIS / (2 * Math.PI))) / PRECIS + " °");
    }
}
class ParameterRenderer implements TableCellRenderer {

    public static final double PRECIS = 100;

    public ParameterRenderer() {
    }

    public Component getTableCellRendererComponent(
            JTable table, Object paramO,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        Parameter param = (Parameter) paramO;
        Object element = param.getElement();


        if (element == null) {
            return new JLabel("null");
        } else if (element instanceof Color) {
            return new JColorShow((Color) element);
        } else if (element instanceof Point2D) {
            Point2D point = (Point2D) element;
            return new JLabel("" + ((int) point.getX() * PRECIS) / PRECIS + " x " + ((int) point.getY() * PRECIS) / PRECIS);
        } else if (param.getType() == Parameters.ParameterType.Angular) {
            if (param.getElement() == null) {
                return new JLabel("0 °");
            } else {
                return new JLabel("" + ((int) (((Double) element).doubleValue() * 360 * PRECIS / (2 * Math.PI))) / PRECIS + " °");
            }
        }else if(element instanceof Stroke){
            return new StrokeRenderer((Stroke)element);
        } else if (element instanceof Double) {
            return new JLabel("" + ((int) (((Double) element).doubleValue() * PRECIS)) / PRECIS);
        }

        return new JLabel(element.toString());
       
    }
}
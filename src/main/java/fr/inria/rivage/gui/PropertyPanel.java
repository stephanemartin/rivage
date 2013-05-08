/*
 * Created on May 7, 2004
 */
package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.tools.Parameter;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.tools.JColorShow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
        //jtpane = new JTabbedPane();
        //jtpane.setFocusable(false);
        // need to wrap the TabbedPaneUI to catch strange
        // IndexOutOfBoundsExceptions...
		/*jtpane.setUI(new TabbedPaneUIWrapper((TabbedPaneUI) UIManager
         .getUI(jtpane)));

         /*panels.add(new PositionPanel(this));
         panels.add(new ColorPanel(this));
         panels.add(new TextPanel(this));
         panels.add(new StrokePanel(this));
         panels.add(new DebugPanel(this));*/

        //add(jtpane, BorderLayout.CENTER);

        update();
    }

    public void update() {

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
            table.setDefaultEditor(Parameter.class, new ParameterEditor());
            //table.setDefaultEditor(Color.class, new ColorEditor());
            table.setDefaultRenderer(Parameter.class,
                    new ParameterRenderer());

            this.add(table);
        }
        this.updateUI();

        //this.add(table);
        //Component currentTab = jtpane.getSelectedComponent();
		/*try {
         // strange IndexOutOfBoundsException happens here...
         jtpane.removeAll();
         } catch (IndexOutOfBoundsException ex) {
         }
         for (IPropPanel p : panels) {
         p.refreshObject();
         }
         try {
         jtpane.setSelectedComponent(currentTab);
         } catch (IllegalArgumentException ex) {
         }*/
    }

    /*public void addTab(String text, JPanel p) {
     try {
     // strange IndexOutOfBoundsException happens here...
     jtpane.addTab(text, p);
     } catch (IndexOutOfBoundsException ex) {
     }
     }*/
    //addAtomicObjects(objs, atomObjs);
    /*public List<GObject> getObjects() {
     return (ArrayList<GObject>) objs;
     }*/

    /*public ArrayList<GObjectShape> getAtomObjects() {
     return (ArrayList<GObjectShape>) atomObjs.clone();
     }*/
    /**
     * Adds all atomic objects that the input list contains (in any level) to
     * the output list.
     *
     * @gObjects in the input list
     * @gObjects out the output list
     */
    /*private void addAtomicObjects(List<GObject> in,
     List<GObject> out) {
     for (GObject obj : in) {
     if (obj instanceof GObjectContainer) {
     addAtomicObjects(((GObjectContainer)obj).getChildren(), out);
     } else {
     out.add(obj);
     }
     }
     }*/
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

class ParameterEditor extends AbstractCellEditor implements TableCellEditor {

    public ParameterEditor() {
    }
    Parameter param;

    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int i, int i1) {
        param = (Parameter) o;
        Object element = param.getElement();
        if (element == null) {
            return null;
        } else if (element instanceof Color) {
            return new ColorEditor();
        }
        
        /*else if (element instanceof Point2D) {
            return new CoordEditor();
        } else if (param.getType() == Parameters.ParameterType.Angular) {
            return new AngularEditor();
        }*/

        return null;
    }

    public Object getCellEditorValue() {
        return param;
    }

    class CoordEditor extends JTextArea {

        CoordEditor(Parameter param) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private CoordEditor() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    class ColorEditor extends JButton implements ActionListener {

        public static final String EDIT = "edit";
        JColorChooser colorChooser;
        JDialog dialog;

        ColorEditor() {
            JButton button = new JButton();
            this.setActionCommand(EDIT);
            button.addActionListener(this);
            button.setBorderPainted(false);
            this.addActionListener(this);
            colorChooser = new JColorChooser();
            dialog = JColorChooser.createDialog(button,
                    "Pick a Color",
                    true, //modal
                    colorChooser,
                    this, //OK button handler
                    null); //no CANCEL button handler

        }

        public void actionPerformed(ActionEvent ae) {
            if (EDIT.equals(ae.getActionCommand())) {
                //The user has clicked the cell, so
                //bring up the dialog.
                
                this.setBackground((Color)param.getElement());
                 colorChooser.setColor((Color)param.getElement());
                 dialog.setVisible(true);
                 
                fireEditingStopped(); //Make the renderer reappear.

            } else { //User pressed dialog's "OK" button.
                 param.localUpdate(colorChooser.getColor(), 0);
                 param.sendMod();
                 Application.getApplication().getCurrentFileController().getCurrentWorkArea().treeChanged();
            }
        }
    }

    class AngularEditor extends JTextArea {

        public AngularEditor(Parameter param) {
        }

        private AngularEditor() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
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
            //Color newColor = (Color) element;
            /*JPanel jpanel=new JPanel();
            JLabel label = new JLabel();
            label.setBackground(newColor);
            label.setOpaque(true);
            jpanel.add(label);*/
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
        } else if (element instanceof Double) {
            return new JLabel("" + ((int) (((Double) element).doubleValue() * PRECIS)) / PRECIS);
        }

        /*else if (element instanceof Double){
         return new JLabel(((Double)element).toString());
         }*/
        return new JLabel(element.toString());
        /*setBackground(newColor);
         if (isBordered) {
         if (isSelected) {
         if (selectedBorder == null) {
         selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
         table.getSelectionBackground());
         }
         setBorder(selectedBorder);
         } else {
         if (unselectedBorder == null) {
         unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
         table.getBackground());
         }
         setBorder(unselectedBorder);
         }
         }
        
        
         setToolTipText("RGB value: " + newColor.getRed() + ", "
         + newColor.getGreen() + ", "
         + newColor.getBlue());
         return this;*/

    }
}
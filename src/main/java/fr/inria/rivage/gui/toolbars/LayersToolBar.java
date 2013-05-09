/*
 * Created on Jan 13, 2005
 */
package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.gui.GButton;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.actions.Actions;
import fr.inria.rivage.gui.listener.ActiveLayerListener;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.LayerChangeListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author papadops 13.01.2005
 */
public class LayersToolBar extends JToolBar implements ActionListener,
        TreeChangeListener, SelectionChangeListener, LayerChangeListener,
        ActiveLayerListener, CurrentWorkAreaListener {

    // status
    public final static int ACTIVATED = 10;
    public final static int DEACTIVATED = 11;
    public final static int TWIDTH = 250;
    public final static int THEIGHT = 200;
    protected Application application;
    // protected JList list;
    protected JLabel title;
    protected JScrollPane scroll;
    // panels
    // private JPanel layersPanel;
    private JPanel buttonsPanel;
    private GButton createButton;
    private GButton deleteButton;
    // private GButton moveObjectButton;
    private JButton addObjectButton;
    private GButton moveToFront;
    private GButton moveToBack;
    private JTable table;
    private LayerTableModel model;

    public LayersToolBar() {
        super();
        setOrientation(JToolBar.VERTICAL);
        setPreferredSize(new Dimension(TWIDTH, THEIGHT));
        // setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // create label
        title = new JLabel("Layers List");
        title.setForeground(Color.blue);
        add(title, BorderLayout.NORTH);

        setOrientation(JToolBar.VERTICAL);

        // create label
        title = new JLabel("Layers List");
        title.setForeground(Color.blue);
        add(title, BorderLayout.NORTH);

        model = new LayerTableModel();

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Set up renderer and editor for the Favorite Color column...
        table.setDefaultRenderer(Color.class, new ColorRenderer(true));
        table.setDefaultRenderer(GLayer.class, new NameRenderer());
        table.setDefaultEditor(Color.class, new ColorEditor());

        // ...and for objects' and name's column
        table.setDefaultEditor(ArrayList.class, new SelectionEditor());
        table.setDefaultEditor(GLayer.class, new SelectionEditor());

        // buttons panel
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());

        createButton = Actions.CREATE_LAYER.createButton();
        deleteButton = Actions.DELETE_LAYER.createButton();
        ///moveObjectButton = Actions.MOVE_OBJS_TO_LAYER.createButton();

        moveToFront = Actions.UP_LAYER.createButton();

        //moveToFront.addActionListener(this);
        moveToBack = Actions.DOWN_LAYER.createButton();
        //moveToBack.addActionListener(this);
        buttonsPanel.add(createButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(moveToFront);
        buttonsPanel.add(moveToBack);

        scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        setLayout(new BorderLayout());

        /*GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 1;
         gbc.gridy = 1;
         gbc.gridwidth = 2;
         buttonsPanel.add(createButton, gbc.clone());
         gbc.gridx = 3;
         buttonsPanel.add(deleteButton, gbc.clone());
         gbc.gridx = 5;
         buttonsPanel.add(moveToFront, gbc.clone());
         gbc.gridx = 1;
         gbc.gridy = 2;
         gbc.gridwidth = 4;
         gbc.fill = GridBagConstraints.HORIZONTAL;
         //buttonsPanel.add(moveObjectButton, gbc.clone());
         gbc.gridx = 5;
         gbc.fill = GridBagConstraints.NONE;*/
        //buttonsPanel.add(moveToBack, gbc.clone());

        add(scroll, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
        add(title, BorderLayout.NORTH);

        SelectionManager.addGeneralSelectionListener(this);
        //Page.addGeneralLayerListener(this);
        WorkArea.addActiveLayerListener(this);
        InnerWindow.addWorkAreaListener(this);

        revalidate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        FileController fc = Application.getApplication()
                .getCurrentFileController();
        Page page = fc.getCurrentWorkArea().getPage();

        int index = table.getSelectedRow();
        int newIndex = -1;

        if (index <= 0) {
            return;
        }

        if (cmd.equals("Up")) {
            newIndex = index + 1;
        } else if (cmd.equals("Do")) {
            newIndex = index - 1;
        }

        int selRow = newIndex;
        int c = page.size();
        if (index < 1 || newIndex < 1 || index >= c || newIndex >= c) {
            return;
        }

        //ID layerID = page.getChildren().get(index).getId();

        //IConcurrencyController cc = fc.getConcurrencyController();
        //cc.doAndSendOperation(new SetLayerPosOperation(layerID, newIndex));

        table.setRowSelectionInterval(selRow, selRow);
        notifyAll();
    }

    class LayerTableModel extends AbstractTableModel {

        public void layerChanged() {
            fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            WorkArea wa = Application.getApplication()
                    .getCurrentFileController().getCurrentWorkArea();
            GLayer g = (GLayer) wa.getPage().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    g.setVisible((Boolean) aValue);
                    wa.repaint();
                    /*if (wa.getActiveLayer() == g && ((Boolean) aValue) == false) {
                     //g.getParameters().setBoolean(Parameters.ParameterType.Visible, true);
                        
                        
                     } else {
                        
                     //g.getParameters().setBoolean(Parameters.ParameterType.Visible, (Boolean) aValue);
                     }*/

                    break;
                case 1:
                    wa.setActiveLayer(g);
                    break;
                case 2:
                    g.getParameters().setObject(Parameters.ParameterType.Text, (String) aValue);
                    g.getParameters().sendMod();
                    break;

                /*  case 3:
                 g.getParameters().setObject(Parameters.ParameterType.FgColor, (Color) aValue);
                 break;*/
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return Boolean.class;
                case 1:
                    return Boolean.class;
                case 2:
                    return String.class;
                /*case 3:
                 return Color.class;*/
                case 3:
                    return ArrayList.class;
            }
            return null;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Visible";
                case 1:
                    return "Active";
                case 2:
                    return "Name";
                /*case 3:
                 return "Color";*/
                case 3:
                    return "Objs";
            }
            return null;
        }

        @Override
        public int getRowCount() {
            try {
                return Application.getApplication().getCurrentFileController()
                        .getCurrentWorkArea().getPage().size();
            } catch (NullPointerException ex) {
            }
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            WorkArea wa = Application.getApplication()
                    .getCurrentFileController().getCurrentWorkArea();
            GLayer layer = (GLayer) wa.getPage().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return layer.isVisible();
                case 1:
                    return wa.getActiveLayer() == layer;
                case 2:
                    return layer.getParameters().getString(Parameters.ParameterType.Text);
                /*case 3:
                 return layer.getParameters().getColor(Parameters.ParameterType.FgColor);*/
                case 3:
                    return layer.size();
            }
            return null;
        }
    }

    class ColorEditor extends AbstractCellEditor implements TableCellEditor,
            ActionListener {

        Color currentColor;
        JButton button;
        JColorChooser colorChooser;
        JDialog dialog;
        protected static final String EDIT = "edit";

        public ColorEditor() {
            // Set up the editor (from the table's point of view),
            // which is a button.
            // This button brings up the color chooser dialog,
            // which is the editor from the user's point of view.
            button = new JButton();
            button.setActionCommand(EDIT);
            button.addActionListener(this);
            button.setBorderPainted(false);

            // Set up the dialog that the button brings up.
            colorChooser = new JColorChooser();
            dialog = JColorChooser.createDialog(button, "Pick a Color", true, // modal
                    colorChooser, this, // OK button handler
                    null); // no CANCEL button handler
        }

        /**
         * Handles events from the editor button and from the dialog's OK
         * button.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (EDIT.equals(e.getActionCommand())) {
                // The user has clicked the cell, so
                // bring up the dialog.
                button.setBackground(currentColor);
                colorChooser.setColor(currentColor);
                dialog.setVisible(true);

                // Make the renderer reappear.
                fireEditingStopped();
            } else { // User pressed dialog's "OK" button.
                currentColor = colorChooser.getColor();
                // TODO implement with operation
                Application.getApplication().getCurrentFileController()
                        .getCurrentWorkArea().getPage().get(
                        table.getSelectedRow()).getParameters().setObject(Parameters.ParameterType.FgColor, currentColor);
            }
        }

        // Implement the one CellEditor method that AbstractCellEditor doesn't.
        @Override
        public Object getCellEditorValue() {
            return currentColor;
        }

        // Implement the one method defined by TableCellEditor.
        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            currentColor = (Color) value;
            return button;
        }
    }

    class NameRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof GLayer) {
                return new JLabel(((GLayer) value).getParameters().getText());
            }
            return new JLabel("Oops");
        }
    }

    class ColorRenderer extends JLabel implements TableCellRenderer {

        Border unselectedBorder = null;
        Border selectedBorder = null;
        boolean isBordered = true;

        public ColorRenderer(boolean isBordered) {
            this.isBordered = isBordered;
            setOpaque(true); // MUST do this for background to show up.
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object color, boolean isSelected, boolean hasFocus, int row,
                int column) {
            Color newColor = (Color) color;
            setBackground(newColor);
            if (isBordered) {
                if (isSelected) {
                    if (selectedBorder == null) {
                        selectedBorder = BorderFactory.createMatteBorder(2, 5,
                                2, 5, table.getSelectionBackground());
                    }
                    setBorder(selectedBorder);
                } else {
                    if (unselectedBorder == null) {
                        unselectedBorder = BorderFactory.createMatteBorder(2,
                                5, 2, 5, table.getBackground());
                    }
                    setBorder(unselectedBorder);
                }
            }

            return this;
        }
    }

    private void refresh() {
        createButton.refresh();
        deleteButton.refresh();
        moveToBack.refresh();
        moveToFront.refresh();
        // moveObjectButton.refresh();

        model.layerChanged();

        adjustSelLayers();
    }

    @Override
    public void treeChanged() {
        refresh();
    }

    @Override
    public void selectionChanged() {
        refresh();
    }

    @Override
    public void layerChanged(Type type) {
        refresh();
    }

    @Override
    public void activeLayerChanged() {
        refresh();
    }

    @Override
    public void currentWorkAreaChanged() {
        refresh();
    }

    class SelectionEditor extends AbstractCellEditor implements
            TableCellEditor, ActionListener {

        JButton button;
        Color curColor;
        protected static final String SEL = "sel";

        public SelectionEditor() {
            // Set up the editor (from the table's point of view),
            // which is a button.
            button = new JButton();
            button.setActionCommand(SEL);
            button.addActionListener(this);
            button.setBorderPainted(true);
            // setHighlight(table.getColumnModel().getColumn(4));
        }

        /**
         * Handles events from the editor button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (SEL.equals(e.getActionCommand())) {
                // The user has clicked the cell, select the objs that belong to
                // this layer
                button.setBackground(curColor);
                WorkArea wa = Application.getApplication()
                        .getCurrentFileController().getCurrentWorkArea();
                wa.getSelectionManager().setSelObjects(((GObjectContainer) wa.getPage().get(table.getSelectedRow())).getCollection());

                // Make the renderer reappear.
                fireEditingStopped();
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable arg0, Object arg1,
                boolean arg2, int arg3, int arg4) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
    ArrayList<GLayer> selLayers = new ArrayList<GLayer>();

    private void adjustSelLayers() {
        selLayers.clear();
        FileController fc = Application.getApplication()
                .getCurrentFileController();
        if (fc == null) {
            return;
        }
        WorkArea wa = fc.getCurrentWorkArea();
        ArrayList<GObject> selObjs = wa.getSelectionManager().getSelObjects();
        for (GObject l : wa.getPage()) {
            if (!l.getParameters().getBooleanD(Parameters.ParameterType.Visible)) {
                continue;
            }
            for (GObject o : ((GObjectContainer<GObject>) l)) {
                if (selObjs.contains(o) & !selLayers.contains(l)) {
                    selLayers.add((GLayer) l);
                    ((DefaultListSelectionModel) table.getSelectionModel())
                            .setSelectionInterval(table.getSelectedRow(), table
                            .getSelectedRow());
                }
            }
        }
        selRows(selLayers);
    }

    public void selRows(ArrayList<GLayer> layers) {

        WorkArea wa = Application.getApplication()
                .getCurrentFileController().getCurrentWorkArea();
        //GLayer layer = (GLayer) wa.getPage().get(rowIndex);

        for (int i = 0; i < table.getRowCount(); i++) {

            if (layers.contains((GLayer) wa.getPage().get(i))) {

                /**
                 * TODO use the "layers" list to highlight the corresponding
                 * cells in the table
                 */
                ((DefaultListSelectionModel) table.getSelectionModel())
                        .addSelectionInterval(i, i);

            }
        }

    }

    public void setHighlight(TableColumn col) {
        col.setCellRenderer(new Highlighter());
    }

    class Highlighter extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable t, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {

            setBackground(Color.white);
            setOpaque(true);
            return this;
        }
    }
}
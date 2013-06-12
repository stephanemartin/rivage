package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.handlers.Handlers;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.InnerWindow;
import fr.inria.rivage.gui.MainFrame;
import fr.inria.rivage.gui.StrokeBar;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class DrawToolBar extends JToolBar implements
        ActionListener, CurrentWorkAreaListener {

    private class ModeButton extends JToggleButton {

        GHandler modeHandler;
        boolean enabled = true;

        ModeButton(GHandler modeHandler, ImageIcon image) {
            super(image);
            this.modeHandler = modeHandler;
        }
        public void hs(){
            this.enabled=false;
            this.setEnabled(enabled);
        }
        @Override
        public void setEnabled(boolean b) {
            if (enabled) {
                super.setEnabled(b);
            }
        }
    }
    //protected ColorBar colorBar, colorBckBar;
    protected StrokeBar strokeBar;
    protected MainFrame parent;
    protected Application application;
    private ArrayList<ModeButton> buttons = new ArrayList<ModeButton>();

    public DrawToolBar(Application application, MainFrame parent) {
        super(JToolBar.VERTICAL);
        this.parent = parent;
        this.application = application;
        initButton(Handlers.SELECTION, "resources/images/select.gif", "Select and Resize Objects");
        //initButton(Handlers.MOVE_POINT, "resources/images/pointmove.gif", "Move a point on a path");
        //initButton(Handlers.ROTATE, "resources/images/rotate.gif", "Rotate an object");
        //initButton(Handlers.SNAP, "resources/images/connect.gif", "use snappoints");
        initButton(Handlers.ZOOM, "resources/images/zoom.gif", "zoom");
        addSeparator();
        initButton(Handlers.CREATE_LINE, "resources/images/line.gif", "Draw Line");
        initButton(Handlers.CREATE_ELLIPSE, "resources/images/ellipse.gif", "Draw Ellipse");
        initButton(Handlers.CREATE_RECTANGLE, "resources/images/rectangle.gif", "Draw Rectangle");
        initButton(Handlers.CREATE_TEXT, "resources/images/text.gif", "Draw text");
        initButton(Handlers.CREATE_FREEHAND, "resources/images/pencil1_b.gif", "Free hand drawing");
        initButton(Handlers.CREATE_POLYGON, "resources/images/polygon.gif", "Draw polygon");
        initButton(Handlers.CREATE_POLYLINE, "resources/images/freehand.gif", "Draw open polygon");
        initButton(Handlers.CREATE_ANNOTATION, "resources/images/annotation.gif", "set an annotation",false);
        
        addSeparator();
        initButton(Handlers.INSERT_BITMAP, "resources/images/bitmap.gif", "import a image file");
        initButton(Handlers.USE_TEMPLATE, "resources/images/templates.gif", "use a template",false);
        initButton(Handlers.IMPORT_FILE, "resources/images/importfile.gif", "link a file",false);
        
        addSeparator();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setFocusable(false);
        /*colorBar = new ColorBar(parent, "default line color");
         colorBar.setLocation(0,0);*/
        /*panel.add(colorBar);
         colorBckBar = new ColorBar(parent, "default fill color");
         colorBckBar.setLocation(0,24);
         panel.add(colorBckBar);*/
        /*strokeBar = new StrokeBar(parent, "default line stroke");
        strokeBar.setLocation(0, 48);
        panel.add(strokeBar);*/
        add(panel);

        InnerWindow.addWorkAreaListener(this);
    }

    private void initButton(GHandler modeHandler, String iconpath, String tooltiptext) {
        initButton(modeHandler, iconpath, tooltiptext, true);
    }

    private void initButton(GHandler modeHandler, String iconpath, String tooltiptext, boolean enabled) {
        ModeButton mb = new ModeButton(modeHandler, new ImageIcon(Application.class.getResource(iconpath)));
        mb.setSelected(false);
        mb.setEnabled(false);
        mb.addActionListener(this);
        mb.setFocusable(false);
        mb.setToolTipText(tooltiptext);
        if(!enabled){
            mb.hs();
        }
        buttons.add(mb);
        add(mb);
    }

    public void setActive(boolean s) {
        for (ModeButton mb : buttons) {
            mb.setEnabled(s);
        }
        /*colorBar.setEnabled(s);
         colorBckBar.setEnabled(s);*/
//        strokeBar.setEnabled(s);
    }

    public void update() {
        for (ModeButton mb : buttons) {
            mb.setSelected(false);
        }
        FileController fc = application.getCurrentFileController();
        WorkArea wa = null;
        if (fc != null) {
            wa = fc.getCurrentWorkArea();
        }
        if (wa == null) {
            setActive(false);
        } else {
            setActive(true);
            for (ModeButton mb : buttons) {
                if (mb.modeHandler == wa.getMode()) {
                    mb.setSelected(true);
                    return;
                }
            }
        }
    }

    /*public void setFrtColor(Color color) {
     this.colorBar.setColor(color);
     }
	
     public Color getFrtColor() {
     return colorBar.getColor();
     }
	
     public void setBckColor(Color color) {
     this.colorBckBar.setColor(color);
     }
	
     public Color getBckColor() {
     return colorBckBar.getColor();
     }*/
/*    public void setStroke(Stroke stroke) {
        this.strokeBar.setStroke(stroke);
    }

    public Stroke getStroke() {
        return strokeBar.getStroke();
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.setDrawingMode(((ModeButton) e.getSource()).modeHandler);
    }

    @Override
    public void currentWorkAreaChanged() {
        FileController fc = application.getCurrentFileController();
        if (fc == null) {
            setActive(false);
        } else {
            setActive(true);
            /*setFrtColor(fc.getCurrentWorkArea().getCurrentFrtColor());
             setBckColor(fc.getCurrentWorkArea().getCurrentBckColor());*/
            /*setStroke(fc.getCurrentWorkArea().getCurrentStroke());*/
            update();
        }
    }
}
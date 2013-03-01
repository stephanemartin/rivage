package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.Application;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.tools.JColorShow;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.JColorChooser;
import javax.swing.JToolBar;

public class ColorChooserTool extends JToolBar {

    public final static int NBCOLOR = 40;
    public final static int COLORMAX = (int) Math.pow(2, 24) - 1;
    public final static int TWIDTH = 250;
    public final static int THEIGHT = 200;
    public final static int NBGRAY = 5;
    private JColorShow backGroud;
    private JColorShow foreGroud;
    // WorkArea wa;
    Color[] defaultColor;/*={Color.WHITE,Color.BLUE,Color.CYAN,Color.DARK_GRAY,
     Color.GRAY,Color.GREEN,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,
     Color.PINK,Color.PINK,Color.RED,Color.YELLOW,Color.BLACK};*/


    class ColorMouseListener implements MouseListener {

        //JColorShow itSelf;
        public ColorMouseListener( /*JColorShow itSelf*/) {
            //  this.itSelf = itSelf;
        }

        public void mouseClicked(MouseEvent e) {
            JColorShow itSelf = (JColorShow) e.getSource();
            if (e.getClickCount() > 1) {
                Color c = JColorChooser.showDialog(ColorChooserTool.this,
                        "Choose a colour", itSelf.getColor());
                if (c != null) {
                    itSelf.setColor(c);
                }

            } else {
                ColorChooserTool.this.stateChanged(e.getButton() != MouseEvent.BUTTON3,
                        itSelf.getColor());

            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    final JColorShow mkButton(Color color) {
        JColorShow njc = new JColorShow(color);
        njc.addMouseListener(new ColorMouseListener());
        return njc;
    }

    public ColorChooserTool() {


        /*  cc.setBorder(BorderFactory
         .createTitledBorder("Choose Object Color"));*/
        //  setPreferredSize(new Dimension(TWIDTH, THEIGHT));
        //cc.setPreferredSize(new Dimension(TWIDTH, THEIGHT));



        this.setOrientation(JToolBar.HORIZONTAL);
        foreGroud = new JColorShow(Color.BLACK);
        backGroud = new JColorShow(Color.WHITE);

        foreGroud.setSize(15, 30);
        foreGroud.setLabel("F");
        backGroud.setSize(15, 30);
        backGroud.setLabel("B");


        Box currentColor = Box.createVerticalBox();
        Box colorsBox = Box.createHorizontalBox();

        currentColor.add(foreGroud);
        currentColor.add(backGroud);
        //defaultColor=new Color[NBCOLOR];
        colorsBox.add(currentColor);



        for (int i = 0; i < NBCOLOR; i++) {
            //int nb=(int)(i*(double)COLORMAX/((double)NBCOLOR));
            Color nc = Color.getHSBColor(i / (float) NBCOLOR, 0.85f, 1.0f);
            colorsBox.add(mkButton(nc));
        }
        for (int i = NBGRAY - 1; i >= 0; i--) {
            int c = (i * 255) / (NBGRAY - 1);
            Color nc = new Color(c, c, c);
            colorsBox.add(mkButton(nc));
        }
        Color nc=new Color(0, 0, 0,0);
        colorsBox.add(mkButton(nc));
        // System.out.println(""+(int)(NBCOLOR*(double)COLORMAX/((double)NBCOLOR)));
        this.add(colorsBox);
        //ok = new JButton("Ok");
        //ok.addActionListener(this);
        //cancel = new JButton("Cancel");
        //cancel.addActionListener(this);
        // intermadiar
			/*JPanel panel = new JPanel();
         panel.add(ok);
         panel.add(cancel);*/
        // frame init
			/*getContentPane().setLayout(new BorderLayout());
         getContentPane().add(cc, BorderLayout.CENTER);
         getContentPane().add(panel, BorderLayout.PAGE_END);
         newColor = textField.getBackground();
         pack();*/
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       /* setLocation((screenSize.width - getWidth()) / 2,
         (screenSize.height - getHeight()) / 2);*/
    }

    public void colorUpdate(Color frt, Color bck) {
        if (frt != null) {
            this.foreGroud.setColor(frt);
        }
        if (bck != null) {
            this.backGroud.setColor(bck);
        }
        this.updateUI();
    }

    public void colorUpdate() {
        WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
        this.colorUpdate(wa.getCurrentFrtColor(), wa.getCurrentBckColor());
    }

    public void stateChanged(boolean frtColor, Color color) {
        JColorShow jc = frtColor ? this.foreGroud : this.backGroud;
        jc.setColor(color);

        if (Application.getApplication().getCurrentFileController() == null) {
            return;
        }
        WorkArea wa = Application.getApplication().getCurrentFileController().getCurrentWorkArea();
        Parameters param = wa.getSelectionManager().getSelParameters();



        if (param != null) {
            Parameters.ParameterType p = frtColor ? Parameters.ParameterType.FgColor : Parameters.ParameterType.BgColor;
            param.setObject(p, color, false);

            param.sendMod();
            wa.treeChanged();
        }


        wa.updateColors(frtColor ? this.foreGroud.getColor() : null,
                frtColor ? null : this.backGroud.getColor(), null);
    }

    @Override
    public String toString() {
        return "Colors";
    }
}

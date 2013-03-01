package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.operations.UnsnapOperation;
import fr.inria.rivage.gui.WorkArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;


   /**
 * An annotation is shown as a small symbol (the annotation mark) and, only if
 * the mouse is on the annotation mark, the accordant text.
 *
 * @author Tobias Kuhn
 * @see geditor.elements.handlers.GAnnotationConstructor
 */
public class GAnnotation extends GObjectShape  {

    /**
     * The index of the center point of the annotation mark. (The only defined
     * point.)
     */
    public static final int CENTER_INDEX = 0;
    // gap between text and border
    private static final double GAP = 4;
    // distance of the text box and the mark (x and y)
    private static final double DIST = 4;
    // fore color of the text field (border and text)
    private static final Color TEXT_F_COLOR = Color.BLACK;
    // back color of the text field
    private static final Color TEXT_B_COLOR = Color.WHITE;
    // font of the text
    private static final Font FONT = new Font("Arial", Font.PLAIN, 11);
   // private GBounds2DP bounds;
    //private IGroup parent;
    //private GSnapPoint pos;
    //private String text;
   // private boolean mouseIn = false;
    //private float textWidth = 150;

    /**
     * Creates a new annotation.
     *
     * @param id the id
     * @param parent the parent <code>IGroup</code> object.
     * @param pos the position of the annotation mark
     * @param text the annotation text
     */
    public GAnnotation(Point2D pos) {
        //this.pos = new GSnapPoint(this, pos);
        //this.text = text;
    }

    /**
     * Creates a new plain annotation. This constructor is used for restoring an
     * annotation stored in XML.
     */
    public GAnnotation() {
        //this(new PointDouble(), "");
    }

    @Override
    public void draw(Graphics2D g2) {
        AffineTransform oldTrans = g2.getTransform();
        PointDouble p = new PointDouble();
        oldTrans.transform(this.parameters.getBounds().getP1(), p);
        g2.setTransform(new AffineTransform());
        Color oldColor = g2.getColor();

        ImageIcon icon = new ImageIcon(Application.class
                .getResource("resources/images/annotation2.gif"));
        icon.paintIcon(null, g2, (int) p.getX() - 8, (int) p.getY() - 8);

        g2.setColor(oldColor);
        g2.setTransform(oldTrans);
    }
/*
    @Override
    public void drawSelectionMark(Graphics2D g2, GHandler mode) {
        AffineTransform oldTrans = g2.getTransform();
        PointDouble p = new PointDouble();
        oldTrans.transform(this.bounds.getP1(), p);
        g2.setTransform(new AffineTransform());

        if (mode == Handlers.SNAP || mode == Handlers.MOVE_POINT) {
            GraphicUtils.drawHandle(g2, p.getX(), p.getY());
        }

        g2.setTransform(oldTrans);
    }

    @Override
    public void drawTopLayer(Graphics2D g2) {
        if (!mouseIn) {
            return;
        }

        AffineTransform oldTrans = g2.getTransform();
        PointDouble p = new PointDouble();
        oldTrans.transform(bounds.getP1(), p);
        g2.setTransform(new AffineTransform());

        ImageIcon icon = new ImageIcon(Application.class
                .getResource("resources/images/annotation.gif"));
        icon.paintIcon(null, g2, (int) p.getX() - 8, (int) p.getY() - 8);

        drawText(g2, p);

        g2.setTransform(oldTrans);
    }

    // Draws the annotation text and the background rectangle.
    private void drawText(Graphics2D g, Point2D pos) {
        String text = this.getText();
        if (text == null || text.equals("")) {
            return;
        }

        g.setFont(FONT);

        AttributedString atstr = new AttributedString(text);
        atstr.addAttribute(TextAttribute.FONT, FONT);
        LineBreakMeasurer linebreaker = new LineBreakMeasurer(atstr
                .getIterator(), g.getFontRenderContext());

        double x = pos.getX();
        double y = pos.getY();
        double yLine = 0;

        g.setColor(TEXT_B_COLOR);
        g.fillRect((int) (x + DIST), (int) (y + DIST),
                (int) (textWidth + 2 * GAP), (int) (GAP + 1));

        while (linebreaker.getPosition() < text.length()) {
            TextLayout tl = linebreaker.nextLayout(textWidth);

            g.setColor(TEXT_B_COLOR);
            g.fillRect((int) (x + DIST), (int) (y + DIST + GAP + yLine + 1),
                    (int) (textWidth + 2 * GAP), (int) (tl.getAscent()
                    + tl.getDescent() + tl.getLeading() + GAP));

            yLine += tl.getAscent();
            g.setColor(TEXT_F_COLOR);
            tl.draw(g, (float) (x + DIST + GAP),
                    (float) (y + DIST + GAP + yLine));
            yLine += tl.getDescent() + tl.getLeading();
        }

        g.drawRect((int) (x + DIST), (int) (y + DIST),
                (int) (textWidth + 2 * GAP), (int) (yLine + 2 * GAP));

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /* @Override
     public void transform(AffineTransform trans) {
     if (!pos.isSnapping()) {
     pos.setLocation(trans.transform(pos, null));
     }
     }*/
    /**
     * Returns the center point of the annotation mark.
     *
     * @return the center point
     */
    /*public PointDouble bounds.getP1() {
     return new PointDouble(pos);
     }

     public void setPos(PointDouble pos) {
     this.pos.setLocation(pos);
     }
	
     public GSnapPoint getSnapPoint() {
     return pos;
     }

     public void setSnapPoint(GSnapPoint pos) {
     this.pos = pos;
     }*/
    /**
     * Sets the annotation text.
     *
     * @param text the annotation text
     */
    public void setText(String text) {
        this.getParameters().setObject(Parameters.ParameterType.Text, text);
    }

    /**
     * Returns the annotation text.
     *
     * @return the annotation text.
     */
   
    public String getText() {
        return this.getParameters().getString(Parameters.ParameterType.Text);
    }

    /*@Override
    public GObject getObjectByPoint(Point2D p, double tolerance) {
        return (bounds.getP1().distance(p) <= tolerance * 2) ? this : null;
    }

    @Override
    public int getPointIndex(Point2D p, double tolerance) {
        if (insideObject(p, tolerance)) {
            return CENTER_INDEX;
        }
        return -1;
    }

    /*@Override
    public void setPointAtIndex(int index, double x, double y) {
        throw new UnsupportedOperationException("Not implemented yet");
        /*	if (index == CENTER_INDEX) {
         this.bounds.setLocation(x, y);
         }*/

    @Override
    public Shape makeShape() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
/* @Override
    public Point2D getPointAtIndex(int index) {
        if (index == CENTER_INDEX) {
            return new PointDouble(this.bounds.getP1());
        }
        return null;
    }*/

    
    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        return new Popup(wa);
    }

    /**
     * Shows the dialog for editing the annotation text.
     *
     * @return true if the dialog has not been canceled.
     */
    public boolean showDialog() {
        Dialog dialog = new Dialog();
        return dialog.ok;
    }

    /*@Override
    public String[] getToSaveFieldList() {
        return new String[]{"SnapPoint", "Text"};
    }

    @Override
    public GBounds2D getBounds2D() {
        return new GBounds2D(bounds.getP1(), 0, 0);
    }

    @Override
    public GBounds2D getScreenBounds2D() {
        return getBounds2D();
    }

    @Override
    public void mouseIn(boolean in) {
        mouseIn = in;
    }

    @Override
    public GSnapPoint getSnapPoint(int pointIndex) {
        /*	if (pointIndex == CENTER_INDEX)
         return bounds.getP1();*
        return null;
    }

    /*@Override
    public ArrayList<GSnapPoint> getSnapPoints() {
        ArrayList<GSnapPoint> list = new ArrayList<GSnapPoint>();
        //list.plus(pos);
        return list;
    }
/*
    @Override
    public void unsnapAll() {
        //pos.setSnappedPoint(null);
    }

    @Override
    public boolean isMovable() {
        return true;//!pos.isSnapping();
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public boolean isRotatable() {
        return false;
    }

    /* @Override
     public boolean hasAngle() {
     return false;
     }

     @Override
     public double getAngle() {
     return 0;
     }
     */
  /*  @Override
    public boolean isFontResizable() {
        return false;
    }

    @Override
    public int getFontSize() {
        return 0;
    }

    @Override
    public void setFontSize(int fontSize) {
    }

    @Override
    public void accept(IGObjectVisitor visitor) {
        visitor.accept(this);
    }
*/
    // The dialog for editing the text of the annotation.
    private class Dialog extends JDialog implements ActionListener {

        private JTextPane textPane;
        private boolean ok = false; // gets true when the OK-button gets clicked

        public Dialog() {
            super(Application.getApplication().getMainFrame(), "Annotation",
                    true);
            setSize(300, 200);
            setLocation(300, 200);
            getContentPane().add(
                    new JScrollPane(textPane = new JTextPane(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                    BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton okButton = new JButton("OK");
            okButton.addActionListener(this);
            buttonPanel.add(okButton);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(this);
            buttonPanel.add(cancelButton);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            textPane.setText(getText());

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("OK")) {
                setText(textPane.getText());
                ok = true;
            }
            setVisible(false);
        }
    }

    // The popup menu for the annotation.
    private class Popup extends JPopupMenu implements ActionListener {

        private WorkArea wa;

        public Popup(WorkArea wa) {
            this.wa = wa;
            JMenuItem editItem = new JMenuItem("edit text");
            editItem.addActionListener(this);
            add(editItem);
            JMenuItem snapItem = new JMenuItem("unsnap");
            snapItem.addActionListener(this);
            add(snapItem);
            //snapItem.setEnabled(pos.isSnapping());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("edit text")) {
                showDialog();
                wa.repaint();
            } else if (e.getActionCommand().equals("unsnap")) {
                IConcurrencyController cc = wa.getFileController()
                        .getConcurrencyController();
                //pos.setSnappedPoint(null);
                cc.doAndSendOperation(new UnsnapOperation(getId(), GAnnotation.CENTER_INDEX));
            }
        }
    }
}

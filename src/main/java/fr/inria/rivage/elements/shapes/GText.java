package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.handlers.GTextHandler;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.operations.ModifyOperation;
import fr.inria.rivage.gui.TextDialog;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.GlyphVector;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class GText extends GObjectShape {

    //private Color frtColor;
    //private String text;
    //private AffineTransform af;
    // private static final int TOP_RIGHT_POINT = 2;
    //private static final int TOP_LEFT_POINT = 3;
    private Font font;
    boolean hide = false;
    //private SnapManager snapManager;

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    private class MyPopup extends JPopupMenu {

        final WorkArea wa;

        public MyPopup(WorkArea wa) {
            this.wa = wa;
            Actlist a = new Actlist();
            JMenuItem i1 = new JMenuItem("font Menu");
            i1.setActionCommand("font+");
            JMenuItem i2 = new JMenuItem("fontsize -");
            i2.setActionCommand("font-");
            JMenuItem i3 = new JMenuItem("change text");
            i3.setActionCommand("chtext");
            this.add(i1);
            this.add(i2);
            this.add(i3);
            i1.addActionListener(a);
            i2.addActionListener(a);
            i3.addActionListener(a);
        }

        class Actlist implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                IConcurrencyController cc = wa.getFileController()
                        .getConcurrencyController();
                if (e.getActionCommand().equals("font+")) {
                    font = new Font(font.getFontName(), font.getStyle(), font.getSize() + 1);
                    cc.doAndSendOperation(new ModifyOperation(GText.this, Parameters.ParameterType.Font, font));
                } else if (e.getActionCommand().equals("font-")) {
                    font = new Font(font.getFontName(), font.getStyle(), font.getSize() - 1);
                    cc.doAndSendOperation(new ModifyOperation(GText.this, Parameters.ParameterType.Font, font));
                } else if (e.getActionCommand().equals("chtext")) {
                    TextDialog textDialog = new TextDialog("Text", (String) parameters.getObject(Parameters.ParameterType.Text));
                    if (textDialog.getText() != null) {

                        cc.doAndSendOperation(new ModifyOperation(GText.this, Parameters.ParameterType.Text, textDialog.getText()));
                    }
                }
            }
        }
    }

    public GText(GObjectContainer parent, PointDouble point, Color color, Color backColor, String text, Font font) {
        super(parent);
        this.getParameters().setObject(Parameters.ParameterType.Font, font);
        this.getParameters().setObject(Parameters.ParameterType.TopLeft, point);
        this.getParameters().setObject(Parameters.ParameterType.FgColor, color);
        this.getParameters().setObject(Parameters.ParameterType.BgColor, backColor);
        this.getParameters().setObject(Parameters.ParameterType.Text, "Place your text here ...");
        this.getParameters().acceptMod();
        //this.frtColor = color;
        //this.text = text;
        //bounds = new GBounds2D(upleft.getX(), upleft.getY(), width, height);
        //af = new AffineTransform();
        //fontsize = 14;
        //  snapManager = new SnapManager(this, true, SnapManager.RECTANGLE_POINTS);
    }

    @Override
    public Shape makeShape() {
        Font f=(Font) this.getParameters().getObject(Parameters.ParameterType.Font);
        FontMetrics fm=Application.getApplication().getCurrentFileController().getCurrentWorkArea().getFontMetrics(f);
        GlyphVector gv= f.createGlyphVector(fm.getFontRenderContext(), this.getParameters().getText());
        
        return gv.getOutline((float)bounds.getX(),(float) bounds.getY());
        //System.out.println("bounds"+bounds.getTopLeft());
        //return new Rectangle2D.Double(bounds.getX(),bounds.getY(),200,200);
        
        //return new Rectangle2D.Double(, y, w, h)
        
        //throw new UnsupportedOperationException("not normal");
        
        //GlyphVector v=f.createGlyphVector(this., v)
    }

    /*@Override
    public void draw(Graphics2D g2) {
        if (hide) {
            return;
        }
        Color color = parameters.getColor(Parameters.ParameterType.FgColor);
        if (color == null) {
            return;
        }

        AffineTransform oldaf = g2.getTransform();
        g2.setFont((Font) this.getParameters().getObject(Parameters.ParameterType.Font));
        AffineTransform afg=getgRendreres().getGlobal();
        if (afg != null) {
            g2.transform(afg);
        }
        g2.setColor(color);
        PointDouble p = this.getParameters().getPoint(Parameters.ParameterType.TopLeft);
        System.out.println(""+p);
        g2.drawString(this.getParameters().getText(), (int) p.x, (int) p.y);
        // drawParagraph(g2, (String) parameters.getObject(Parameters.ParameterType.Text), (float) bounds.getWidth());
        g2.setTransform(oldaf);
    }*/

    public GText() {
    }

    public GBounds2D getBounds(Graphics g) {
        return getBounds(g, this.getParameters().getText());
    }
    public Font getFont(){
        return (Font) this.getParameters().getObject(Parameters.ParameterType.Font);
    }

    public GBounds2D getBounds(Graphics g, String str) {

        PointDouble TopLeft = this.getParameters().getPoint(Parameters.ParameterType.TopLeft);
        FontMetrics fm = g.getFontMetrics(this.getFont());
        GBounds2D rec = new GBounds2D(fm.getStringBounds(str, g));
        rec = new GBounds2D(rec.getTopLeftPoint().plus(TopLeft), rec.getDimension());
        return rec;
    }

    public void drawParagraph(Graphics2D g, String paragraph, float width) {
        /*if (paragraph == null || paragraph.equals("")) {
         height = 0;
         return;
         }
         AttributedString atstr = new AttributedString(paragraph);
         atstr.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN,
         fontsize));
         LineBreakMeasurer linebreaker = new LineBreakMeasurer(atstr
         .getIterator(), g.getFontRenderContext());
         float y = 0.0f;
         while (linebreaker.getPosition() < paragraph.length()) {
         TextLayout tl = linebreaker.nextLayout(width);
         y += tl.getAscent();
         tl.draw(g, (float) bounds.getX(), (float) bounds.getY() + y);
         y += tl.getDescent() + tl.getLeading();
         }
         bounds.height = y;*/
    }

    @Override
    public JPopupMenu getPopup(WorkArea wa) {
        return new MyPopup(wa);
    }

    @Override
    public GHandler getModifier() {
        return new GTextHandler(this, false);
    }
   
}
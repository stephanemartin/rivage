/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
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
package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GBounds2D;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.shapes.GText;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.gui.WorkArea;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class GTextHandler extends GHandler {

    private GText text;
    private GTextMod jtext;
    private boolean isNew;
    private WorkArea wa;
    private ButtonForText bft;

    public GTextHandler(GText text, boolean isNew) {
        this.text = text;
        this.isNew = isNew;


    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        wa.setLayout(null);
        jtext = new GTextMod(text);
        Font font = (Font) text.getParameters().getObject(Parameters.ParameterType.Font);
        if (font != null) {
            jtext.setFont(font);
        }
        bft = new ButtonForText();

        /*JTextField jt=new JTextField("test");
         wa.add(jt);
         jt.setSize(new Dimension(200,200));
         jt.setLocation(text.getParameters().getPoint(Parameters.ParameterType.TopLeft).getPoint());
        
        
         jt.setBackground(Color.red);
         // jtext.setBounds(text.getParameters().getBounds().getBounds());
         */
        wa.add(jtext);
        jtext.revalidate();
        /*wa.updateUI();*/

        wa.repaint();
        jtext.repaint();
        text.setHide(true);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.bft.mouseClick(e);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        PointDouble p = text.getParameters().getPoint(Parameters.ParameterType.TopLeft);
        bft.draw(g);
        // g.drawImage(img.getImage(), (int) p.getX() , (int) p.getY()- img.getIconHeight(), null);
        //g.draw(jtext.getBounds());
    }

    @Override
    public void cleanUP() {
        wa.remove(jtext);
        text.setHide(false);
    }

    class GTextMod2 extends GHandler {

        TextLayout textLayout;
        GText gText;
        String text;
        WorkArea wa;
        TextHitInfo thi;
        TextHitInfo thi2;
        Shape select;
        boolean shift;

        public GTextMod2(GText gtext) {
            this.gText = gtext;
            this.text = gtext.getParameters().getText();

        }

        @Override
        public void draw(Graphics2D g) {
            if (textLayout == null) {
                this.textLayout = new TextLayout(text, gText.getFont(), g.getFontRenderContext());

            }
            PointDouble d = gText.getParameters().getPoint(Parameters.ParameterType.TopLeft);
            textLayout.draw(g, (float) d.x, (float) d.y);
            if (select != null) {
                g.draw(select);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            PointDouble p = wa.getDrawingPoint(e.getPoint());
            thi = textLayout.hitTestChar((float) p.getX(), (float) p.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            PointDouble p = wa.getDrawingPoint(e.getPoint());
            thi2 = textLayout.hitTestChar((float) p.getX(), (float) p.getY());
            select = textLayout.getVisualHighlightShape(thi, thi2);

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDragged(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // PointDouble p=wa.getDrawingPoint(e.getPoint());
            // TextHitInfo thi=textLayout.hitTestChar((float)p.getX(), (float)p.getY());
            // thi.

            super.mouseClicked(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            shift = e.isShiftDown();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
        }

        @Override
        public void init(WorkArea wa) {
            this.wa = wa;
        }
    }

    class GTextMod extends JTextField {

        boolean graphicsb = true;

        @Override
        protected void fireActionPerformed() {
            super.fireActionPerformed();

        }
        AffineTransform af;

        public void setPosition(PointDouble p) {
        }

        @Override
        public int viewToModel(Point pt) {
            return super.viewToModel(pt);
        }

        @Override
        protected void processMouseEvent(MouseEvent e) {
            super.processMouseEvent(adaptMouse(e));
        }

        public MouseEvent adaptMouse(MouseEvent e) {
            MouseEvent e2 = e;
            try {
                Point2D p = e.getPoint();
                PointDouble pr = wa.getScreenPoint(af.inverseTransform(wa.getDrawingPoint(p), new PointDouble()));
                e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), (int) pr.getX(), (int) pr.getY(), e.getClickCount(), false, e.getButton());

            } catch (NoninvertibleTransformException ex) {
                Logger.getLogger(GTextHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            return e2;
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e) {
            super.processMouseMotionEvent(adaptMouse(e));
        }

        public GTextMod(GText text) {
            PointDouble p = new PointDouble(text.getParameters().getPoint(Parameters.ParameterType.TopLeft));
            af = text.getgRendreres().getGlobal();
            //af.transform(p, p);
            this.setLocation(p.getPoint());
            this.setSize(new Dimension(200, 200));
            Insets in = this.getInsets();

            //p=p.plus(-in.left-in.right,-in.top-in.bottom);
            this.setLocation(p.getPoint());
            this.setOpaque(false);

            //autoResize();
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            this.setText(text.getParameters().getText());

            this.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    updateSize();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateSize();
                }

                public void changedUpdate(DocumentEvent e) {
                    updateSize();
                }
            });
            //this.setBorder(null);
            //this.setVisible(true);
            // RepaintManager.currentManager(this).addDirtyRegion(this,(int) p.x, (int)p.y, this.getSize().width, this.getSize().height);


            /* RepaintManager repaintManager = new RepaintManager() {
             public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
             schedulePaint();
             }

             public void addDirtyRegion(Window window, int x, int y, int w, int h) {
             schedulePaint();
             }

             public void paintDirtyRegions() {
             schedulePaint();
             }

             private void schedulePaint() {
             SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
             GTextMod.this.paint(GTextMod.this.getGraphics());
             }
             });
             }
             };
             RepaintManager.setCurrentManager(repaintManager);
             this.ge;
             */

        }

        /*@Override
         protected void paintComponent(Graphics g) {
         BufferedImage im = new BufferedImage(wa.getWidth(), wa.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
         super.paintComponent(im.getGraphics());
         AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
         im = op.filter(im, null);
         g.drawImage(im, 0, 0, null);
         }*/
        final void updateSize() {
            Insets in = this.getInsets();
            int margx = in.bottom + in.top;
            int margy = in.right + in.left;
            Dimension dim = text.getBounds(this.getGraphics(), this.getText()).getDimension().plus(margx, margy).getDimension();
            this.setSize(dim);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (graphicsb) {
                updateSize();
                graphicsb = false;
            }
            /*AffineTransform bak = g2.getTransform();
             AffineTransform test = new AffineTransform();
             test.concatenate(af);
             test.concatenate(g2.getTransform());
             g2.transform(test);
             */
            super.paint(g);

            /*AffineTransform bak = ((Graphics2D) g).getTransform();
             //((Graphics2D) g).transform(af);*/


            Font f = (Font) text.getParameters().getObject(Parameters.ParameterType.Font);
            FontMetrics fm = Application.getApplication().getCurrentFileController().getCurrentWorkArea().getFontMetrics(f);
            GlyphVector gv = f.createGlyphVector(fm.getFontRenderContext(), this.getText());
            Parameters.ParameterBounds bounds = text.getParameters().getBounds();
            GObjectShape.draw(g2, text.getParameters(), text.getgRendreres(), gv.getOutline((float) bounds.getX(), (float) bounds.getY()), bounds.getCenter());

            /*BufferedImage im = new BufferedImage(wa.getWidth(), wa.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
             super.paint(im.getGraphics());
            
             AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
             im = op.filter(im, null);
             g.drawImage(im, 0, 0, null);
             */
            //g2.setTransform(bak);
        }
        /*final void autoResize() {
         this.getGraphics();
         this.setSize(text.getBounds(this.getGraphics()).getDimension().getDimension());
         System.out.println("resize !");
         }*/
    }

    class ButtonForText {

        private ImageIcon img = new ImageIcon(Application.class.getResource("resources/images/check-and-cross-icons2.png"));
        PointDouble position;
        GBounds2D bound;
        PointDouble posScreen;

        ButtonForText() {
            GBounds2D boundP = text.getEuclidBounds();


            Double y;
            if (boundP.getY() < img.getIconHeight()) {
                y = boundP.getHeight() + boundP.getY();
            } else {
                y = boundP.getY() - img.getIconHeight();
            }
            position = new PointDouble(boundP.getX(), y);



        }

        void draw(Graphics g) {

            if (bound == null) {
                posScreen = wa.getScreenPoint(position);
                bound = new GBounds2D(posScreen.x, posScreen.y, img.getIconWidth(), img.getIconHeight());
            }
            Graphics2D g2 = (Graphics2D) g.create();
            double zoom = 1 / wa.getZoom();
            g2.scale(zoom, zoom);
            //g2.setTransform(new AffineTransform());
            g2.drawImage(img.getImage(), (int) posScreen.getX(), (int) posScreen.getY(), null);
            g2.dispose();
        }

        void mouseClick(MouseEvent e) {

            Point2D point = e.getPoint();
            //System.out.println("e" + e.getPoint() + "bound" + bound);

            if (bound.contains(point)) {
                GHandler next;
                if (point.getX() < bound.getCenterX()) {
                    //Accept
                    text.setHide(false);
                    text.getParameters().setObject(Parameters.ParameterType.Text, jtext.getText());
                    if (isNew) {
                        wa.getFileController().getConcurrencyController().doAndSendOperation(
                                wa.getCreateOperation(text));
                        next = Handlers.CREATE_TEXT;
                    } else {
                        text.getParameters().sendMod();
                        next = Handlers.SELECTION;
                    }
                    wa.setMode(next);
                } else {
                    if (isNew) {
                        wa.setMode(Handlers.CREATE_TEXT);
                    } else {
                        wa.setMode(Handlers.SELECTION);
                    }
                }
            }
        }
    }
}

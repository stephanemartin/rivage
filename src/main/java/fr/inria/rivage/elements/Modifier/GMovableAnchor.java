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
package fr.inria.rivage.elements.Modifier;

import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.elements.renderer.AffineTransformRenderer;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.tools.Configuration;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
 public class GMovableAnchor extends GHandler {

    AffineTransformRenderer rend;
    GObjectShape gobjetShape;
    double sizeDraw = Configuration.getConfiguration().SEL_MARK_SIZE;
    Color color = Color.BLUE;
    Color XorColor = Color.ORANGE;
    PointDouble origin;

    public static enum ShapePoint {

        Square, Losange, Circle, Cross,Null
    };
    ShapePoint shapePoint = ShapePoint.Square;
    PointSetterGetter pointSetterGetter;
    WorkArea wa;

    public GMovableAnchor(PointSetterGetter pointSetterGetter) {
        this.pointSetterGetter = pointSetterGetter;
        this.pointSetterGetter.setgMovableAnchor(this);

    }

    public GMovableAnchor(PointSetterGetter pointSetterGetter, ShapePoint shape) {
        this(pointSetterGetter);
        this.shapePoint = shape;
    }

    public GMovableAnchor(PointSetterGetter pointSetterGetter, ShapePoint shape, Color color) {
        this(pointSetterGetter, shape);
        this.color = color;
    }

    @Override
    public void init(WorkArea wa) {
        this.wa = wa;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    Shape mkShape() {
        PointDouble p = pointSetterGetter.getPoint();
        sizeDraw = Configuration.getConfiguration().SEL_MARK_SIZE/wa.getZoom();
        double x = p.getX();
        double y = p.getY();
        double halfSizedraw = sizeDraw / 2.0;
        switch (shapePoint) {
            case Circle:
                return new Ellipse2D.Double(x - halfSizedraw, y - halfSizedraw, sizeDraw, sizeDraw);
            case Cross:
                //halfSizedraw=40;
                double factWidth = 2.6;
                GeneralPath pathx = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                pathx.moveTo(x - halfSizedraw / factWidth, y - halfSizedraw);//1
                pathx.lineTo(x + halfSizedraw / factWidth, y - halfSizedraw);//2
                pathx.lineTo(x + halfSizedraw / factWidth, y - halfSizedraw / factWidth);//3
                pathx.lineTo(x + halfSizedraw, y - halfSizedraw / factWidth);//4
                pathx.lineTo(x + halfSizedraw, y + halfSizedraw / factWidth);//5
                pathx.lineTo(x + halfSizedraw / factWidth, y + halfSizedraw / factWidth);//6
                pathx.lineTo(x + halfSizedraw / factWidth, y + halfSizedraw);//7
                pathx.lineTo(x - halfSizedraw / factWidth, y + halfSizedraw);//8
                pathx.lineTo(x - halfSizedraw / factWidth, y + halfSizedraw / factWidth);//9
                pathx.lineTo(x - halfSizedraw, y + halfSizedraw / factWidth);//10
                pathx.lineTo(x - halfSizedraw, y - halfSizedraw / factWidth);//11
                pathx.lineTo(x - halfSizedraw / factWidth, y - halfSizedraw / factWidth);//12
                pathx.closePath();
                /*Area ar=new Area(new Line2D.Double(x - halfSizedraw, y, x+halfSizedraw, y));
                 ar.add(new Area(new Line2D.Double(x,y - halfSizedraw,  x,y+halfSizedraw)));
                 
                 return ar;*/

                //pathx.append(new Line2D.Double(x - halfSizedraw, y, x + halfSizedraw, y), false);
                //Rectangle2D r=new Rectangle2D.Double(x - halfSizedraw/2.0, y - halfSizedraw, sizeDraw, halfSizedraw);
                //r.add(new Rectangle2D.Double(x - halfSizedraw, y - halfSizedraw/2.0, halfSizedraw, sizeDraw));
                return pathx;
            case Losange:
                AffineTransform af = new AffineTransform();
                af.setToRotation(Math.PI / 4.0, x, y);
                return af.createTransformedShape(new Rectangle2D.Double(x - halfSizedraw, y - halfSizedraw, sizeDraw, sizeDraw));
            case Null:
                return null;
            default:
                return new Rectangle2D.Double(x - halfSizedraw, y - halfSizedraw, sizeDraw, sizeDraw);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        Color bak = g.getColor();
        // g.setColor(Color.black);
        g.setColor(color);

        g.setXORMode(XorColor);
        pointSetterGetter.draw(g);
        Shape shape = mkShape();
        
        if (shape != null) {
            g.setStroke(new BasicStroke(0));
            g.fill(shape);
        }
        
        // g.draw(shape);
        //       g.setXORMode(bakXor);
        g.setColor(bak);
        //g.setTransform(a);
        g.setPaintMode();
    }

    public GHandler getHandlerByPoint(Point2D p, double tolerance) {
        return (p.distance(this.pointSetterGetter.getPoint()) <= tolerance + sizeDraw) ? this : null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        pointSetterGetter.setPoint(origin, wa.getDrawingPoint(e.getPoint()));
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pointSetterGetter.pressed(e, origin);
        origin = wa.getDrawingPoint(e.getPoint());
        //throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pointSetterGetter.released(e);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        pointSetterGetter.clicked(e, origin);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pointSetterGetter.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pointSetterGetter.keyReleased(e);
    }
    

    public static abstract class PointSetterGetter {
        GMovableAnchor gMovableAnchor;

        public GMovableAnchor getgMovableAnchor() {
            return gMovableAnchor;
        }

        public void setgMovableAnchor(GMovableAnchor gMovableAnchor) {
            this.gMovableAnchor = gMovableAnchor;
        }
        
        
        
        public abstract PointDouble getPoint();

        public abstract void setPoint(PointDouble Origine, PointDouble p);

        public void released(MouseEvent e) {
        }

        public void draw(Graphics2D g) {
        }
        
        /**
         *
         * @param e the value of e
         * @param origin the value of origin
         */
        public void pressed(MouseEvent e, PointDouble origin) {
        }

        public void clicked(MouseEvent e, PointDouble origin) {
        }
        public void keyPressed(KeyEvent e){
            
        }
        public void keyReleased(KeyEvent e){
            
        }
    }
}

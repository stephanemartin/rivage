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
package fr.inria.rivage.engine.concurrency.tools;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.engine.manager.FileController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class Parameters extends Observable implements Serializable {
    public static enum ParameterType {

        FgColor,
        BgColor,
        Text,
        Font,
        TopLeft,
        Dimension,
        Scale,
        Shear,
        Thickness,
        Angular,
        Zpos,
        Visible,
        Stroke,
        Curve1,
        Curve2,
        Center,
        Translate,};
    public static String Names[] = {
        "Foreground Color",
        "Background Color",
        "Text",
        "Font",
        "TopLeft",
        "Dimension",
        "Scale",
        "Shear",
        "Thickness",
        "Angular",
        "Zposition",
        "Visible",
        "Stroke",
        "Curve Left",
        "Curve Right",
        "Center",
        "Translate",};
    protected EnumMap<ParameterType, Parameter> parametersMap = new EnumMap<ParameterType, Parameter>(ParameterType.class);
    //transient protected GDocument doc;
    private transient FileController fc;
    protected ID fCId; /** hack : search the document after serialisation*/
    protected ID target;

    public EnumMap<ParameterType, Parameter> getParametersMap() {
        return parametersMap;
    }
    public FileController getFileController(){
        if(fc==null){
            fc=Application.getApplication().getFileManagerController().getFileControlerById(fCId);
        }
            return fc;
    }
    public Parameters(GDocument doc) {
       // this.doc = doc;
        fCId = doc.getFileController().getId();
    }

    public Parameters(ID fCId) {
        this.fCId=fCId;
    }

     public Parameters(ID fCId,ID target) {
        this.fCId=fCId;
        this.target=target;
    }

    public ID getfCId() {
        return fCId;
    }
     
    public Parameters() {
    }
   

    /*public GDocument getGDocument(){
        if (doc==null){
            doc=Application.getApplication().getFileManagerController().getFileControlerById(fCId).getDocument();
        }
        return doc;
    }*/
    public ID getTarget() {
        return target;
    }

    public void setTarget(ID target) {
        this.target = target;
        for (Parameter p : parametersMap.values()) {
            p.setTarget(target);
        }
    }

    Parameter newParameter(ParameterType type) {

        Parameter ret =getFileController().getConcurrencyController().getFactoryParameter().create(type,target,fCId);
        return ret;
    }

    public Parameters(GDocument doc, ID target, ParameterType... types) {
        this.target = target;
        //this.doc = doc;
        fCId=doc.getFileController().getId();
        addNullType(types);


    }

    final public void addNullType(ParameterType... types) {
        addType(null, types);
    }

    final public void addZeroType(ParameterType... types) {
        addType(new Double(0), types);
    }

    final public void addType(Object element, ParameterType... types) {
        for (ParameterType t : types) {
            Parameter np = newParameter(t);
            parametersMap.put(t, np);

            if (element != null) {
                np.localUpdate(element, 0);
            }
        }

    }

    public void setBoolean(Parameters.ParameterType t, boolean b) {
        setObject(t, b);
    }

    public Set<Map.Entry<ParameterType, Parameter>> getKey() {
        return parametersMap.entrySet();
    }

    public String[] getNames() {
        LinkedList<String> out = new LinkedList();
        for (ParameterType p : parametersMap.keySet()) {
            out.add(Names[p.ordinal()]);
        }
        return (String[]) out.toArray();
    }

    public void setParameter(ParameterType type, Parameter p) {
        parametersMap.put(type, p);
    }

    public void setObject(ParameterType type, Object o, int nice) {
        setObject(type, o, true, nice);
    }

    public void setObject(ParameterType type, Object o) {
        setObject(type, o, true);
    }

    public void setObject(ParameterType type, Object o, boolean updateIfNull, int nice) {
        Parameter p = parametersMap.get(type);
        if (p == null) {
            if (!updateIfNull) {
                return;
            }
            p = newParameter(type);
            parametersMap.put(type, p);
        }
        p.localUpdate(o, nice);
        //System.out.println("obs"+countObservers());

        setChanged();
        notifyObservers(type);
    }

    public void setObject(ParameterType type, Object o, boolean updateIfNull) {
        setObject(type, o, updateIfNull, 0);
    }

    public void remoteUpdateParameter(Parameter p) {
        ParameterType type = p.getType();
        Parameter pa = parametersMap.get(type);
        if (pa == null) {
            parametersMap.put(type, p);
           // p.setDoc(doc);
        } else {
            pa.remoteUpdate(p);
        }
        setChanged();
        notifyObservers(type);
    }

    public Parameter getParameter(ParameterType type) {
        return parametersMap.get(type);
    }

    public Object getObject(ParameterType type) {
        Parameter p = parametersMap.get(type);
        if (p != null) {
            return p.getElement();
        } else {
            return null;
        }
    }

    public double getDouble(ParameterType type) {
        Parameter p = parametersMap.get(type);
        if (p != null && p.getElement() != null) {
            return (Double) p.getElement();
        } else {
            return 0;
        }
    }

    public String getString(ParameterType type) {
        Object o = getObject(type);
        return o == null ? null : o.toString();
    }

    public String getText() {
        return getString(ParameterType.Text);
    }

    public PointDouble getPoint(ParameterType type) {
        return (PointDouble) getObject(type);
    }

    public void setDouble(ParameterType type, double d) {
        setObject(type, new Double(d));

    }

    public void setDouble(ParameterType type, double d, int nice) {
        setObject(type, new Double(d), nice);
    }

    public void addPoint(ParameterType type, Point2D p) {
        PointDouble old = getPoint(type);
        setObject(type, old.plus(p));
    }

    public void multPoint(ParameterType type, PointDouble mult) {
        PointDouble old = getPoint(type);
        setObject(type, old.mult(mult));
    }

    public void setPoint(ParameterType type, double x, double y, int nice) {
        setObject(type, new PointDouble(x, y), nice);
    }

    public void setPoint(ParameterType type, double x, double y) {
        setObject(type, new PointDouble(x, y));
    }

    public void setDimension(int width, int height) {
        setObject(ParameterType.Dimension, new Dimension(width, height));
    }

    public Color getColor(ParameterType type) {

        return (Color) getObject(type);

    }

    public Position getPosition(ParameterType type) {
        return (Position) getObject(type);
    }

    public boolean getBoolean(ParameterType type) {
        Boolean b = (Boolean) getObject(type);
        return b == null ? false : b.booleanValue();
    }

    public boolean getBooleanD(ParameterType type) {
        Boolean b = (Boolean) getObject(type);
        return b == null ? true : b.booleanValue();
    }

    public boolean isParameter(ParameterType type) {
        return parametersMap.containsKey(type);
    }

    public void sendMod() {
        for (Parameter p : parametersMap.values()) {
            p.sendMod();
        }
    }

    public void acceptMod() {
        for (Parameter p : parametersMap.values()) {
            p.acceptMod();
        }
    }

    public Collection<Parameter> getValues() {
        return parametersMap.values();
    }

   

  /* public void setDocument(GDocument doc) {
        this.doc = doc;
        /*for (Parameter p : parametersMap.values()) {
            p.setDoc(doc);
        }*
    }*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Parameter p : parametersMap.values()) {
            sb.append(p.toString());
        }
        return sb.toString();
    }

    public ParameterBounds getBounds() {
        return new ParameterBounds();
    }

    public ParameterBounds getBounds(int nice) {
        return new ParameterBounds(nice);
    }

    public void setNextZPosition(ColObject obj, ID id) {
        Position p = obj.getParameters().getPosition(ParameterType.Zpos);
        this.setObject(ParameterType.Zpos, p.genNext(id));
    }
    
    public class ParameterBounds extends Rectangle2D implements Serializable {

        public ParameterBounds(int nice) {
            this.nice = nice;
        }

        public ParameterBounds() {
        }
        int nice;

        /**
         * Returns the X coordinate of this
         * <code>GBounds2D</code> in double precision.
         *
         * @return the X coordinate of this <code>GBounds2D</code>.
         * @since 1.2
         */
        @Override
        public double getX() {
            double x = getPoint(ParameterType.TopLeft).getX();
            PointDouble p = getPoint(ParameterType.Dimension);
            return p == null ? x : Math.min(x, x + p.getX());

        }

        /**
         * Returns the Y coordinate of this
         * <code>GBounds2D</code> in double precision.
         *
         * @return the Y coordinate of this <code>GBounds2D</code>.
         * @since 1.2
         */
        @Override
        public double getY() {
            double y = getPoint(ParameterType.TopLeft).getY();
            PointDouble p = getPoint(ParameterType.Dimension);
            return p == null ? y : Math.min(y, y + p.getY());

        }

        /**
         * Returns the width of this
         * <code>GBounds2D</code> in double precision.
         *
         * @return the width of this <code>GBounds2D</code>.
         * @since 1.2
         */
        @Override
        public double getWidth() {
            PointDouble p = getPoint(ParameterType.Dimension);
            return p == null ? 0 : Math.abs(p.getX());
        }

        /**
         * Returns the height of this
         * <code>GBounds2D</code> in double precision.
         *
         * @return the height of this <code>GBounds2D</code>.
         * @since 1.2
         */
        @Override
        public double getHeight() {
            PointDouble p = getPoint(ParameterType.Dimension);
            return p == null ? 0 : Math.abs(p.getY());
        }

        /**
         * Determines whether or not this
         * <code>GBounds2D</code> is empty.
         *
         * @return <code>true</code> if this <code>GBounds2D</code> is empty;
         * <code>false</code> otherwise.
         * @since 1.2
         */
        public PointDouble getP1() {
            return getPoint(ParameterType.TopLeft);
        }

        public PointDouble getP2() {
            return getPoint(ParameterType.TopLeft).plus(getPoint(ParameterType.Dimension));
        }

        @Override
        public boolean isEmpty() {
            return (getWidth() == 0.0) || (getHeight() == 0.0);
        }

        public PointDouble getTopLeft() {
            return this.getP1().min(this.getP2());
        }
        /*   public void setP1(Point2D p1) {
         setObject(P1coord, new PointDouble(p1),nice);
         }

         public void setP2(Point2D b) {
         setObject(P2coord, new PointDouble(b),nice);
         }*/

        /**
         * Sets the location and size of this
         * <code>GBounds2D</code> to the specified double values.
         *
         * @param x the coordinates to which to set the upper left corner of
         * this <code>GBounds2D</code>
         * @param y the coordinates of the upper left corner of the newly
         * constructed <code>GBounds2D</code>
         * @param w the value to use to set the width of *          * this <code>GBounds2D</code>
         * @param h the value to use to set the height of this
         * <code>GBounds2D</code>
         * @since 1.2
         */
        @Override
        public void setRect(double x, double y, double w, double h) {
            setPoint(ParameterType.TopLeft, x, y, nice);
            setPoint(ParameterType.Dimension, w, h, nice);
        }

        public PointDouble getDimension() {
            return getPoint(ParameterType.Dimension);
        }

        /**
         * Sets this
         * <code>GBounds2D</code> to be the same as the specified
         * <code>Rectangle2D</code>.
         *
         * @param r the specified <code>Rectangle2D</code>
         * @since 1.2
         */
        @Override
        public void setRect(Rectangle2D r) {
            setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        }

        /**
         * Determines where the specified double coordinates lie with respect to
         * this
         * <code>GBounds2D</code>. This method computes a binary OR of the
         * appropriate mask values indicating, for each side of this
         * <code>GBounds2D</code>, whether or not the specified coordinates are
         * on the same side of the edge as the rest of this
         * <code>GBounds2D</code>.
         *
         * @param x the specified coordinates
         * @param y the specified coordinates
         * @return the logical OR of all appropriate out codes.
         * @see Rectangle2D#OUT_LEFT
         * @see Rectangle2D#OUT_TOP
         * @see Rectangle2D#OUT_RIGHT
         * @see Rectangle2D#OUT_BOTTOM
         * @since 1.2
         */
        @Override
        public int outcode(double x, double y) {
            int out = 0;
            if (getWidth() <= 0) {
                out |= OUT_LEFT | OUT_RIGHT;
            } else if (x < getX()) {
                out |= OUT_LEFT;
            } else if (x > getX() + getWidth()) {
                out |= OUT_RIGHT;
            }
            if (getHeight() <= 0) {
                out |= OUT_TOP | OUT_BOTTOM;
            } else if (y < getY()) {
                out |= OUT_TOP;
            } else if (y > getY() + getHeight()) {
                out |= OUT_BOTTOM;
            }
            return out;
        }

        /**
         * Returns the high precision bounding box of this
         * <code>Rectangle2D</code>.
         *
         * @return the bounding box of this <code>Rectangle2D</code>.
         * @since 1.2
         */
        @Override
        public Rectangle2D getBounds2D() {
            return this;
        }

        /**
         * Returns a new
         * <code>Rectangle2D</code> object representing the intersection of this
         * <code>Rectangle2D</code> with the specified
         * <code>Rectangle2D</code>.
         *
         * @param r the <code>Rectangle2D</code> to be intersected with this
         * <code>Rectangle2D</code>
         * @return the largest <code>Rectangle2D</code> contained in both the
         * specified <code>Rectangle2D</code> and in *          * this <code>Rectangle2D</code>.
         * @since 1.2
         */
        @Override
        public Rectangle2D createIntersection(Rectangle2D r) {
            Rectangle2D dest = new Rectangle2D.Double();
            Rectangle2D.intersect(this, r, dest);
            return dest;
        }

        /**
         * Returns a new
         * <code>Rectangle2D</code> object representing the union of this
         * <code>Rectangle2D</code> with the specified
         * <code>Rectangle2D</code>.
         *
         * @param r the <code>Rectangle2D</code> to be combined with this
         * <code>Rectangle2D</code>
         * @return the smallest <code>Rectangle2D</code> containing both the
         * specified <code>Rectangle2D</code> and this <code>Rectangle2D</code>.
         * @since 1.2
         */
        @Override
        public Rectangle2D createUnion(Rectangle2D r) {
            Rectangle2D dest = new Rectangle2D.Double();
            Rectangle2D.union(this, r, dest);
            return dest;
        }

        /**
         * Returns the
         * <code>String</code> representation of this
         * <code>Rectangle2D</code>.
         *
         * @return a <code>String</code> representing *          * this <code>Rectangle2D</code>.
         * @since 1.2
         */
        @Override
        public String toString() {
            return getClass().getName() + "[x=" + getX() + ",y=" + getY() + ",w=" + getWidth() + ",h=" + getHeight() + "]";
        }

        /**
         * Moves the bounds in direction
         * <code>xdirection</code> and
         * <code>ydirection</code>.
         *
         * @param xdirection the distance in x direction.
         * @param ydirection the distance in y direction.
         */
        /*public void translate(double xdirection, double ydirection) {
         x += xdirection;
         y += ydirection;
         }*/
        /**
         * Scales the bounds around the top-left corner, meaning the x,y of this
         * bound.
         *
         * @param xscale the factor in x direction.
         * @param yscale the factor in y direction.
         */
        /*public void scale(double xscale, double yscale) {
         width *= xscale;
         height *= yscale;
         }

         public void scale(Point2D center, double xscale, double yscale) {
         // 1) translate the rectangle to the coordinates of you point
         x = x - center.getX();
         y = y - center.getY();
         // 2) do the scaling
         x = x * xscale;
         y = y * yscale;
         width = xscale * width;
         height = yscale * height;
         // 3) translate back the rectangle
         x = x + center.getX();
         y = y + center.getY();
         }
         */
        /**
         * @param height The height to set.
         */
        public void setHeight(double height) {
            setPoint(ParameterType.Dimension, getWidth(), height, nice);
        }

        /**
         * @param width The width to set.
         */
        public void setWidth(double width) {
            setPoint(ParameterType.Dimension, width, getHeight(), nice);
        }

        /**
         * @param x The x to set.
         */
        /*public void setX(double x) {
         setPoint(P1coord, x, getY(),nice);
         }

         /**
         * @param y The y to set.
         */
        /*public void setY(double y) {
         setPoint(P1coord, getX(), y,nice);
         }

         /**
         * Conserves width and height and sets the rectangle to a new center set by
         * the x and y coordinates
         *
         * @param x the horizontal coordinate
         * @param y the vertical coordinate
         */
        public void setCenter(Point2D newCenter) {
            setObject(ParameterType.TopLeft, new PointDouble(newCenter).minus(getDimension().div(2.0, 2.0)), nice);
        }

        /*@Deprecated
         public void setToContain(Point2D p1, Point2D p2) {
         setP1(p1);
         setBound(new PointDouble(0,0));
         plus(p2);
         }*/
        public PointDouble getCenter() {
            return getP1().center(getP2());
        }

        public void rotate(double delta, Point2D center) {
            setDouble(ParameterType.Angular, (getDouble(ParameterType.Angular) + delta) % (2.0 * Math.PI), nice);
            AffineTransform af = new AffineTransform();
            af.rotate(delta, center.getX(), center.getY());
            setCenter(af.transform(getCenter(), null));
        }

        public int getNice() {
            return nice;
        }

        public void setNice(int nice) {
            this.nice = nice;
        }

        public boolean isInside(Point2D p, double tolerance) {
            return isInside(new Rectangle2D.Double(p.getX() - tolerance / 2.0, p.getY() - tolerance / 2.0, tolerance, tolerance));
        }

        public boolean isInside(Rectangle2D rec) {
            return this.intersects(rec);
        }
    }
}

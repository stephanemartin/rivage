/*
 * Created on Oct 24, 2004
 */
package fr.inria.rivage.elements.serializable;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yves
 */
public class SerBasicStroke implements Serializable, Stroke {

    private static final Logger LOG = Logger.getLogger(SerBasicStroke.class.getName());
    transient BasicStroke basicstroke;
    
    public SerBasicStroke() {
        basicstroke = new BasicStroke();
    }
    
    public SerBasicStroke(float width) {
        basicstroke = new BasicStroke(width);
    }
    
    public SerBasicStroke(float width, int cap, int join) {
        basicstroke = new BasicStroke(width, cap, join);
    }
    
    public SerBasicStroke(float width, int cap, int join, float miterlimit) {
        basicstroke = new BasicStroke(width, cap, join, miterlimit);
    }
    
    public SerBasicStroke(float width, int cap, int join, float miterlimit, float[] dash,
            float dash_phase) {
        basicstroke = new BasicStroke(width, cap, join, miterlimit, dash, dash_phase);
    }
    
    private void readObject(java.io.ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        float width = stream.readFloat();
        int cap = stream.readInt();
        int join = stream.readInt();
        float miterlimit = stream.readFloat();
        float[] dash = (float[]) stream.readObject();
        float dash_phase = stream.readFloat();
        basicstroke = new BasicStroke(width, cap, join, miterlimit, dash, dash_phase);
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeFloat(basicstroke.getLineWidth());
        stream.writeInt(basicstroke.getEndCap());
        stream.writeInt(basicstroke.getLineJoin());
        stream.writeFloat(basicstroke.getMiterLimit());
        stream.writeObject(basicstroke.getDashArray());
        stream.writeFloat(basicstroke.getDashPhase());
    }
    
    @Override
    public Shape createStrokedShape(Shape p) {
        try {
            return basicstroke.createStrokedShape(p);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Rendereing error{0}", ex);
            return p;
        }
    }
    
    public float[] getDashArray() {
        return basicstroke.getDashArray();
    }
    
    public float getDashPhase() {
        return basicstroke.getDashPhase();
    }
    
    public int getEndCap() {
        return basicstroke.getEndCap();
    }
    
    public int getLineJoin() {
        return basicstroke.getLineJoin();
    }
    
    public float getLineWidth() {
        return basicstroke.getLineWidth();
    }
    
    public float getMiterLimit() {
        return basicstroke.getMiterLimit();
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.basicstroke != null ? this.basicstroke.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SerBasicStroke other = (SerBasicStroke) obj;
        if (this.basicstroke != other.basicstroke && (this.basicstroke == null || !this.basicstroke.equals(other.basicstroke))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return basicstroke.toString();
    }
}

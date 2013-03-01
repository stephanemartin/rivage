package fr.inria.rivage.elements;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class PointDouble extends Point2D.Double implements Serializable, Cloneable {

    public PointDouble() {
        this(0, 0);
    }

    public PointDouble(double x, double y) {
        super(x, y);
    }

    public PointDouble(Point2D p) {
        this(p.getX(), p.getY());
    }

    public void translate(double x, double y) {
        this.setLocation(this.getX() + x, this.getY() + y);
    }

    public void translate(Point2D p) {
        this.translate(p.getX(), p.getY());
    }

    public PointDouble plus(double x, double y) {
        return new PointDouble(this.getX() + x, this.getY() + y);
    }

    public PointDouble plus(Point2D p) {
        if(p==null){
            return new PointDouble(this);
        }
        return plus(p.getX(), p.getY());
    }

    public PointDouble center(double x, double y) {
        return new PointDouble((this.getX() + x) / 2.0, (this.getY() + y) / 2.0);
    }

    public PointDouble center(Point2D p) {
        return center(p.getX(), p.getY());
    }

    public PointDouble minus(double x, double y) {
        return new PointDouble(this.getX() - x, this.getY() - y);
    }

    public PointDouble minus(Point2D p) {
        return minus(p.getX(), p.getY());
    }

    public PointDouble mult(double x, double y) {
        return new PointDouble(this.getX() * x, this.getY() * y);
    }

    public PointDouble mult(Point2D p) {
        if(p==null){
            return new PointDouble(this);
        }
        return mult(p.getX(), p.getY());
    }

    public PointDouble div(double x, double y) {
        return new PointDouble(this.getX() / x, this.getY() / y);
    }

    public PointDouble div(Point2D p) {
        return div(p.getX(), p.getY());
    }
    public PointDouble abs(){
        return new PointDouble(Math.abs(getX()),Math.abs(getY()));
    }
    public PointDouble rotate(Point2D center,double alpha){
        if (alpha==0){
            return new PointDouble(this);
        }
        AffineTransform af=new AffineTransform();
        af.rotate(alpha, center.getX(), center.getY());
        return (PointDouble)af.transform(this, new PointDouble());
        /*PointDouble changed = this.minus(center);
        return new PointDouble(changed.getX()*Math.cos(alpha)-changed.getY()*Math.sin(alpha),
                changed.getX()*Math.sin(alpha)+changed.getY()*Math.cos(alpha)).plus(center);*/
         
    }
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
    public void setX(double x){
        this.x=x;
    }
    public void setY(double y){
        this.y=y;
    }
    public PointDouble min(Point2D p2){
        return new PointDouble(Math.min(this.x, p2.getX()),Math.min(y, p2.getY()));
    }
    public PointDouble intervert(){
        return new PointDouble(this.getY(),this.getX());
    }
    public Point getPoint(){
        return new Point((int)x,(int)y);
    }
    public Dimension getDimension(){
        return new Dimension ((int)x,(int)y);
    }
}

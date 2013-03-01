package fr.inria.rivage.tools;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.util.Vector;

public class debugging {

  final static private PrintStream stream = System.out;

  public static void print(String s) {
    stream.println(s);
  }

  public static String printAffineTransform(AffineTransform af) {
    return "Translations : (" + af.getTranslateX() + "," + af.getTranslateY() + ") -- Scaling : (" + af.getScaleX() +
                   "," + af.getScaleY() + ")";
  }

  public static String printAffineTransform(String s, AffineTransform af) {
    return s + " ---- Translations : (" + af.getTranslateX() + "," + af.getTranslateY() + ") -- Scaling : (" +
                   af.getScaleX() + "," + af.getScaleY() + ")";
  }

  public static String printShapeInfo(String s, Shape shape) {
    return s + " ---- Position : (" + shape.getBounds2D().getX() + "," + shape.getBounds2D().getY() + ") -- Size : (" +
                   shape.getBounds2D().getWidth() + "," + shape.getBounds2D().getHeight() + ")";
  }

  public static String printMouseEvent(String s, MouseEvent e) {
  	return s + " ---- Position : (" + e.getX() + "," + e.getY() + ") -- Button :" +
                   e.getButton();
  }

  public static void printTextLayout(String s, TextLayout t) {

  }

  public static String printPointVector(String s, Vector<Point2D> v) {
    String r = s + " Vector contains " + v.size() + " points";
    int count = 0;
    for(Point2D p : v) {
      r += "\n[" + count + "]   ( " + p.getX() + " , " + p.getY() + " )";
      count++;
    }
    return r;
  }

  public static String printPointArray(String s, Point2D[] v) {
    String r = s + " Vector contains " + v.length + " points";
    int count;
    for (count = 0; count < v.length; count++) {
      r += "\n[" + count + "]   ( " + v[count].getX() + " , " + v[count].getY() + " )";
    }
    return r;
  }

  public static String printPoint(String s, Point2D p) {
    return s + " Position of point : " + p.getX() + " , " + p.getY() + " )";
  }

}

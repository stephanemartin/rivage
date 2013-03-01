package fr.inria.rivage.elements;

import fr.inria.rivage.elements.interfaces.IStrokable;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * This class provides some static methods for applying often used
 * transformation on a graphical object like rotation, scale and
 * translate.
 * @author Tobias Kuhn
 */
public class Transformer {
	
	private Transformer() {} // no instances allowed
	
	/**
	 * Rotates the graphical object.
	 * @param obj the graphical object to rotate
	 * @param angle the angle
	 * @param center the center point of the rotation.
	 */
	public static void rotate(GObject obj, double angle, Point2D center) {
            throw new UnsupportedOperationException("Not implemented yet");
            /*change parameter */
		//obj.transform(AffineTransform.getRotateInstance(angle, center.getX(), center.getY()));
	}

	/**
	 * Scales the graphical object.
	 * @param obj the graphical object to scale
	 * @param center the center point of the scale transformation
	 * @param xfactor the factor of scaling in the x direction
	 * @param yfactor the factor of scaling in the y direction
	 */
	public static void scale(GObject obj, Point2D center, double xfactor, double yfactor) {
                        throw new UnsupportedOperationException("Not implemented yet");

		/*if (xfactor <= 0 || yfactor <= 0) return;
		AffineTransform a = new AffineTransform();
		a.translate(center.getX(), center.getY());
		a.scale(xfactor, yfactor);
		a.translate(- center.getX(), - center.getY());
		//obj.transform(a);*/
	}
	
	/**
	 * Translates the graphical object.
	 * @param obj the graphical object to translate
	 * @param dx the x part of the translation
	 * @param dy the y part of the translation
	 */
	public static void translate(GObject obj, double dx, double dy) {
                        throw new UnsupportedOperationException("Not implemented yet");

		//obj.transform(AffineTransform.getTranslateInstance(dx, dy));
	}
	
	/**
	 * Scales the stroke of the graphical object in respect to an affine
	 * transformation.
	 * @param strokable the strokable object
	 * @param trans the affine transformation
	 */
	public static void scaleStroke(IStrokable strokable, AffineTransform trans) {
                     
		double d = trans.getDeterminant();
		strokable.setStroke(new SerBasicStroke(
			(float) (strokable.getStroke().getLineWidth() * Math.sqrt(d))
		));
	}

}

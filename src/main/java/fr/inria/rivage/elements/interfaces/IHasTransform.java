package fr.inria.rivage.elements.interfaces;

import java.awt.geom.AffineTransform;

/**
 * This interface describes a graphical object that has an
 * affine transformation. The methods of this interface are mainly
 * used by GSnapPoint and SnapManager in order to calculate the
 * correct position of the snappoints.
 * @author Tobias Kuhn
 */
public interface IHasTransform {
	
	/**
	 * Returns the current affine transformation of this object.
	 * @return the current affine transformation
	 */
	public AffineTransform getTransform();
	
	/**
	 * Sets the affine transformation of this object.
	 * @param af the new affine transformation
	 */
	public void setTransform(AffineTransform af);

}

/*
 * Created on Aug 16, 2004
 */
package fr.inria.rivage.elements.interfaces;

import fr.inria.rivage.elements.serializable.SerBasicStroke;


/**
 * @author Yves
 */
public interface IStrokable  {

	/**
	 * This method is used to set the stroke that should be used to draw the
	 * shape.
	 * 
	 * @param stroke
	 *            the stroke that should be used.
	 */
	public void setStroke(SerBasicStroke stroke);

	/**
	 * Returns the stroke currently used to draw the shape's border.
	 * 
	 * @return a stroke used to draw the shape's border.
	 */
	public SerBasicStroke getStroke();

}
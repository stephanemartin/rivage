package fr.inria.rivage.elements.interfaces;

import fr.inria.rivage.elements.GSnapPoint;
import java.util.ArrayList;


/**
 * This interface describes a graphical object that has points which can snap a
 * point of another object. As soon as a point has snapped another point, the
 * object of the snapped point takes control over the position of the snapping
 * point.
 * @author Tobias Kuhn
 * @see geditor.elements.interfaces.ISnappable
 * @see geditor.elements.GSnapPoint
 */
public interface ISnapper  {
	
	/**
	 * Returns the snap point at the given index or null if there is no
	 * one at this index.
	 * @param pointIndex
	 * @return the snap point
	 */
	public GSnapPoint getSnapPoint(int pointIndex);
	
	/**
	 * Returns a list of all snap points.
	 * @return list of all snap points
	 */
	public ArrayList<GSnapPoint> getSnapPoints();
		
	/**
	 * Makes all points of this object unsnapping.
	 */
	public void unsnapAll();
		
}

package fr.inria.rivage.elements.interfaces;

import fr.inria.rivage.elements.SnapManager;


/**
 * This interface describes a graphical object, that can get snapped
 * by snap points of other objects.
 * @author Tobias Kuhn
 * @see geditor.elements.interfaces.ISnapper
 * @see geditor.elements.GSnapPoint
 */
public interface ISnappable extends   IHasTransform {
	
	/**
	 * Returns the SnapManager for this object.
	 * @return the SnapManager
	 */
	public  SnapManager getSnapManager();
	
}

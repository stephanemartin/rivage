package fr.inria.rivage.elements.interfaces;

import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Position;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public interface IGroup extends ITreeElement {

	/**
	 * Returns the object at the given position. The tolerance is used for thin
	 * objects like lines and indicates the allowed distance between the given
	 * position and the true position of the object (in terms of drawing
	 * coordinates).
	 * 
	 * @param p the position of the supposed point
	 * @param tolerance the tolerance as the distance to the object
	 * @return the object at the given point or null if there is no one
	 */
	public GObjectShape getObjectByPoint(final Point2D p, double tolerance);

	/**
	 * The same as <code>getObjectByPoint</code>, but it never returns a
	 * group. It returns the according element of the group instead.
	 * 
	 * @param p the position of the supposed point
	 * @param tolerance the tolerance as the distance to the object
	 * @return the object at the given point
	 */
	public GObjectShape getAtomicObjectByPoint(final Point2D p, double tolerance);

	/**
	 * Checks all the object in the tree beneath this group and returns the
	 * object with the given ID or <code>null</code> if none was found.
	 * 
	 * @param ID the ID to search for
	 * @return the object with the given ID or <code>null</code>
	 */
	public GObjectShape getObjectByID(ID ID);

	public void addChild(GObjectShape obj);

	public ArrayList<GObjectShape> getChildren();

	public GObjectShape removeChild(ID id);

	public int getChildrenCount();
	public Position getMinZ ();
	public Position getMaxZ ();
	
	
	/*
	 * @return the objects contained in the group (the call is recursive)
	 */
	public ArrayList<GObjectShape> getObjects ();

	public Rectangle2D getBounds2D();

}

package fr.inria.rivage.elements.interfaces;

/**
 * This interfaces describes a graphical object that performs a
 * reaction when the cursor is moving on.
 * @author Tobias Kuhn
 */
public interface IMouseSensitive  {
	
	/**
	 * This method gets called with parameter true if the mouse enters
	 * the object. And it gets called again with parameter false when the
	 * cursor is exiting.
	 * @param in true if the mouse enters and false if the mouse exits.
	 */
	public void mouseIn(boolean in);

}

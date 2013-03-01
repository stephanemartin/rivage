/*
 * Created on Aug 26, 2004
 */
package fr.inria.rivage.elements.propertypanel;

/**
 * This is the interface panels that should be displayed in the PropertyPanel
 * should implement.
 * 
 * @author Yves
 */
public interface IPropPanel {
	
	/**
	 * Should reread the current properties of the object, for example after it
	 * was moved by hand or changed by another site.
	 */
	public void refreshObject();
	
}
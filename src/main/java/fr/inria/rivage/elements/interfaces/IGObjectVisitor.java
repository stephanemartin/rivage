package fr.inria.rivage.elements.interfaces;

import fr.inria.rivage.elements.GFile;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.shapes.GAnnotation;
import fr.inria.rivage.elements.shapes.GBitmap;
import fr.inria.rivage.elements.shapes.GEllipse;
import fr.inria.rivage.elements.shapes.GLine;
import fr.inria.rivage.elements.shapes.GRectangle;
import fr.inria.rivage.elements.shapes.GText;

public interface IGObjectVisitor {

	/**
	 * Except if it has a reason, if this accept is called, it means that
	 * something went wrong.
	 * 
	 * @param o an GObjectShape
	 */
	public void accept(GObjectShape o);
	public void accept(GGroup g);
	public void accept(GAnnotation g);
	public void accept(GBitmap g);
	public void accept(GEllipse g);
	public void accept(GFile g);
	public void accept(GLine g);
	public void accept(GRectangle g);
	public void accept(GText g);
	
}

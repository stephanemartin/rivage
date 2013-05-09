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

public class AbstractVisitor implements IGObjectVisitor {

    @Override
	public void accept(GObjectShape o) {

	}

    @Override
	public void accept(GGroup g) {

	}

    @Override
	public void accept(GAnnotation g) {

	}

    @Override
	public void accept(GBitmap g) {

	}

    @Override
	public void accept(GEllipse g) {

	}

    @Override
	public void accept(GFile g) {

	}

    @Override
	public void accept(GLine g) {

	}

    @Override
	public void accept(GRectangle g) {

	}

    @Override
	public void accept(GText g) {

	}

}

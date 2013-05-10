package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.interfaces.ISnappable;
import fr.inria.rivage.elements.shapes.GAnnotation;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.operations.SnapOperation;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.MouseEvent;

/**
 * This class handles the construction of an annotation. First the user has to
 * click where he wants to place the annotation mark. Then a new annotation gets
 * created and the user can enter the text for the annotation.
 * 
 * @author Tobias Kuhn
 * @see geditor.elements.GAnnotation
 */
public class GAnnotationConstructor extends GHandler {

	private WorkArea wa;

	GAnnotationConstructor() {
	}

	@Override
	public void init(WorkArea wa) {
		this.wa = wa;
		wa.getSelectionManager().clearSelection();
		wa.setCursor(Cursors.annotation);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		PointDouble point = wa.getDrawingPoint(e.getPoint());

		// creating an new annotation object (no annotation text yet)
		GAnnotation a = null;// = new GAnnotation(point, "");

		IConcurrencyController cc = wa.getFileController()
				.getConcurrencyController();
//		cc.startLocalOp();

		// showing the dialog for editing the annotation text
		boolean ok = a.showDialog();

		// If the dialog hasn't been canceled: adding the new annotation into
		// the
		// graphic tree. (Otherwise the created annotation waits for the garbage
		// collection.)
		if (ok) {

			GObject o = wa.getPage().getObjectByPoint(
					point, wa.getPointTolerance());
                            
			cc.doAndSendOperation(wa.getCreateOperation(a));

			if (o instanceof ISnappable) {
			//	cc.startLocalOp();
				ISnappable s = (ISnappable) o;
				cc.doAndSendOperation(new SnapOperation(o
						.getId(), a.getId(), GAnnotation.CENTER_INDEX, point,
						wa.getPointTolerance(), wa.getObjectTolerance()));
			}

		}

		wa.repaint();
		wa.setMode(Handlers.SELECTION);

	}

}

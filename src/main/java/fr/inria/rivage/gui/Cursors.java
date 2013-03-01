package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 * This class contains static references to a set of cursors.
 * @author Tobias Kuhn
 */
public class Cursors {
	
	public static Cursor normal = new Cursor(Cursor.DEFAULT_CURSOR);
	public static Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
	public static Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	public static Cursor wait = new Cursor(Cursor.WAIT_CURSOR);
	public static Cursor move = new Cursor(Cursor.MOVE_CURSOR);
	public static Cursor resizeNW = new Cursor(Cursor.NW_RESIZE_CURSOR);
	public static Cursor resizeN = new Cursor(Cursor.N_RESIZE_CURSOR);
	public static Cursor resizeNE = new Cursor(Cursor.NE_RESIZE_CURSOR);
	public static Cursor resizeW = new Cursor(Cursor.W_RESIZE_CURSOR);
	public static Cursor resizeE = new Cursor(Cursor.E_RESIZE_CURSOR);
	public static Cursor resizeSW = new Cursor(Cursor.SW_RESIZE_CURSOR);
	public static Cursor resizeS = new Cursor(Cursor.S_RESIZE_CURSOR);
	public static Cursor resizeSE = new Cursor(Cursor.SE_RESIZE_CURSOR);
	public static Cursor annotation = Toolkit.getDefaultToolkit().createCustomCursor(
			(new ImageIcon(Application.class.getResource("resources/images/annotationcursor.gif"))).getImage(),
			new Point(0, 0),
			"Annotation");
	public static Cursor importing = Toolkit.getDefaultToolkit().createCustomCursor(
			(new ImageIcon(Application.class.getResource("resources/images/importcursor.gif"))).getImage(),
			new Point(0, 0),
			"Import");
	public static Cursor freehand = Toolkit.getDefaultToolkit().createCustomCursor(
			(new ImageIcon(Application.class.getResource("resources/images/freehandcursor.gif"))).getImage(),
			new Point(0, 0),
			"Freehand");
	public static Cursor zoom = Toolkit.getDefaultToolkit().createCustomCursor(
			(new ImageIcon(Application.class.getResource("resources/images/zoomcursor.gif"))).getImage(),
			new Point(5, 5),
			"Zoom");

}

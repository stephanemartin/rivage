package fr.inria.rivage.elements.handlers;

public class Handlers {
	
	public static final GAnnotationConstructor CREATE_ANNOTATION = new GAnnotationConstructor();
	public static final GBitmapImporter INSERT_BITMAP = new GBitmapImporter();
	public static final GEllipseConstructorHandler CREATE_ELLIPSE = new GEllipseConstructorHandler();
	public static final GFileImporter IMPORT_FILE = new GFileImporter();
	public static final GFreehandConstructorHandler CREATE_FREEHAND = new GFreehandConstructorHandler();
	//public static final GLineConstructorHandler CREATE_LINE = new GLineConstructorHandler();
	public static final GPolyConstructor CREATE_LINE = new GPolyConstructor(false, 2);
	//public static final GMovePointHandler MOVE_POINT = new GMovePointHandler();
	public static final GPolyConstructor CREATE_POLYGON = new GPolyConstructor(true);
	public static final GPolyConstructor CREATE_POLYLINE = new GPolyConstructor(false);
	public static final GRectangleConstructorHandler CREATE_RECTANGLE = new GRectangleConstructorHandler();
	//public static final GRotateHandler ROTATE = new GRotateHandler();
	public static final GSelectionHandler SELECTION = new GSelectionHandler();
	//public static final GSnapHandler SNAP = new GSnapHandler();
	public static final GTemplateConstructor USE_TEMPLATE = new GTemplateConstructor();
	public static final GTextConstructor CREATE_TEXT = new GTextConstructor();
	public static final GZoomer ZOOM = new GZoomer();

}

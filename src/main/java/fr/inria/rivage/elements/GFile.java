package fr.inria.rivage.elements;

import java.awt.Shape;


public class GFile extends GObjectShape  {

    @Override
    public Shape makeShape() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	
/*
	private transient IGroup parent;

	private String fileName;
	
	private int pageIndex;

	private GGroup group;

	private AffineTransform af;

	public GFile() {
		this.af = new AffineTransform();
	}

	public GFile(String fileName, int pageIndex) throws Exception {
		this();
		this.fileName = fileName;
		this.pageIndex = pageIndex;
		loadFile();
	}

	@Override
	public Object clone() 
	{
		GFile dolly;
		try {
			dolly  = (GFile) super.clone();
			dolly.af = (AffineTransform) af.clone();
			dolly.group = (GGroup) group.clone();
			return dolly;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
	}

    @Override
	public void draw(Graphics2D g2) {
		AffineTransform oldaf = g2.getTransform();
		g2.transform(af);

		group.draw(g2);

		g2.setTransform(oldaf);
	}

    @Override
	public void drawSelectionMark(Graphics2D g2, GHandler mode) {
		g2.setStroke(new BasicStroke(0));
		g2.draw(getScreenBounds2D());
	}

    @Override
	public void drawTopLayer(Graphics2D g2) {
	}

    /*@Override
	public IGroup getParent() {
		return parent;
	}*

    @Override
	public int getPointIndex(Point2D p, double tolerance) {
		return -1;
	}

    @Override
	public JPopupMenu getPopup(WorkArea wa) {
		return null;
	}

    @Override
	public Rectangle2D getBounds2D() {
		return group.getScreenBounds2D();
	}

    @Override
	public GBounds2D getScreenBounds2D() {
		return new GBounds2D(af.createTransformedShape(group
				.getScreenBounds2D()));
	}

    @Override
	public String[] getToSaveFieldList() {
		return new String[] { "Transform", "PageIndex", "FileName" };
	}

    @Override
	public boolean insideObject(Point2D p, double tolerance) {
		Point2D po = new PointDouble();
		try {
			af.inverseTransform(p, po);
		} catch (NoninvertibleTransformException ex) {
		}
		return group.insideObject(po, 0);
	}

   /* @Override
	public void transform(AffineTransform trans) {
		af.preConcatenate(trans);
	}

	

    @Override
	public void setParent(IGroup parent) {
		this.parent = parent;
	}*

    @Override
	public void setPointAtIndex(int index, double x, double y) {
	}

    @Override
	public Point2D getPointAtIndex(int index) {
		return null;
	}

	

  /* @Override
	public boolean hasAngle() {
		return true;
	}

    @Override
	public double getAngle() {
		return GraphicUtils.getAngleR(af);
	}*

    @Override
	public boolean isMovable() {
		return true;
	}

    @Override
	public boolean isResizable() {
		return true;
	}

    @Override
	public boolean isRotatable() {
		return true;
	}

    @Override
	public AffineTransform getTransform() {
		return (AffineTransform) af.clone();
	}

    @Override
	public void setTransform(AffineTransform af) {
		this.af = (AffineTransform) af.clone();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		try {
			loadFile();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public GGroup getContent() {
		return group;
	}

	private void loadFile() throws Exception {
		String xmlString = Application.getApplication().getServerNetwork()
				.loadFileAsString(ServerNetwork.DOCUMENT_FILES, fileName);
		
		GDocument doc = XMLDecoder.getDocument(
				XMLDecoder.getXMLElement(xmlString));
		
		Page page = doc.getPages().get(pageIndex);
		
		this.group = new GGroup();
		/*for (GObjectShape obj : page.getAllObjects()) {
			group.addChild(obj);
		}*
	}

	

    @Override
	public void accept(IGObjectVisitor visitor) {
		visitor.accept(this);
	}

*/
}

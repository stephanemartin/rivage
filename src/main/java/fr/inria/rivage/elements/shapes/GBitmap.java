package fr.inria.rivage.elements.shapes;

import fr.inria.rivage.elements.GObjectShape;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.ImageIcon;

public class GBitmap extends GObjectShape  {

	
	//private transient IGroup parent;

	private String imageName;

	private transient ImageIcon image;

	//private GPoint pos;

	//private AffineTransform af;

	/*private SnapManager snapManager = new SnapManager(this, true,
			SnapManager.RECTANGLE_POINTS);*/

	public GBitmap(Point2D pos, String imageName) {
		//this.pos = new GPoint(pos);
		this.imageName = imageName;
		getImage();
		this.af = new AffineTransform();
	}

	public GBitmap() {
	//	this.pos = new GPoint();
	}

	

    @Override
	public void draw(Graphics2D g2) {
		AffineTransform oldaf = g2.getTransform();
		g2.transform(af);

		getImage().paintIcon(null, g2, (int) this.parameters.getBounds().getX(), (int) this.parameters.getBounds().getY());

		g2.setTransform(oldaf);
	}

    /*@Override
	public void drawSelectionMark(Graphics2D g2, GHandler mode) {
		g2.setStroke(new BasicStroke(0));
		if (mode == Handlers.SNAP) {
			snapManager.drawPoints(g2);
			return;
		}
		g2.draw(getScreenBounds2D());
	}

    /*@Override
	public void drawTopLayer(Graphics2D g2) {
	}

    /*@Override
	public IGroup getParent() {
		return parent;
	}*/

   /* @Override
	public int getPointIndex(Point2D p, double tolerance) {
		return -1;
	}

    @Override
	public JPopupMenu getPopup(WorkArea wa) {
		return null;
	}

    @Override
	public GBounds2D getBounds2D() {
		return new GBounds2D(pos, getImage().getIconWidth(), getImage()
				.getIconHeight());
	}

    @Override
	public GBounds2D getScreenBounds2D() {
		return new GBounds2D(af.createTransformedShape(getBounds2D()));
	}

    @Override
	public String[] getToSaveFieldList() {
		return new String[] { "Transform", "Pos", "ImageName", "SnapManager" };
	}

    @Override
	public AffineTransform getTransform() {
		return (AffineTransform) af.clone();
	}

    @Override
	public void setTransform(AffineTransform trans) {
		this.af = (AffineTransform) trans.clone();
	}

	public Point2D getP1() {
		return new GPoint(pos);
	}

	public void setPos(Point2D pos) {
		this.pos.setLocation(pos);
	}

	public String getImageName() {
		return imageName;
	}
*/
	private ImageIcon getImage() {
		if (image == null) {
			try {
				/*image = new ImageIcon(Application.getApplication()
						.getServerNetwork().loadFileAsByteArray(
								GroupController.DATA_FILES, imageName));*/
			} catch (Exception ex) {
			}
		}
		return image;
	}
/*
	public void setImageName(String imageName) {
		this.imageName = imageName;
		image = null;
		getImage();
	}

/*    @Override
    public GObject getObjectByPoint(Point2D p, double tolerance) {
        return (new GBounds2D(pos, getImage().getIconWidth(), getImage()
				.getIconWidth())).contains(GraphicUtils.reverseTransformPoint(
				af, p))?this:null;
	}*/

   /* @Override
	public void transform(AffineTransform trans) {
		af.preConcatenate(trans);
	}*/

	

    /*@Override
	public void setParent(IGroup parent) {
		this.parent = parent;
	}*/

    /*@Override
	public void setPointAtIndex(int index, double x, double y) {
	}

   /* @Override
	public Point2D getPointAtIndex(int index) {
		return null;
	}*/

    /*@Override
	public double getAngle() {
		return GraphicUtils.getAngleR(af);
	}*/

    /*@Override
	public boolean hasAngle() {
		return true;
	}*/
	
    /*@Override
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
	}*/

	

 /*   @Override
	public SnapManager getSnapManager() {
		return snapManager;
	}

    @Override
	public void accept(IGObjectVisitor visitor) {
		visitor.accept(this);
	}

*/	

    @Override
    public Shape makeShape() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

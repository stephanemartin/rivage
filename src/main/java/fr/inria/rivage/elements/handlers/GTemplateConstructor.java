package fr.inria.rivage.elements.handlers;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.Cloner;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.elements.GTemplate;
import fr.inria.rivage.elements.PointDouble;
import fr.inria.rivage.elements.Transformer;
import fr.inria.rivage.gui.Cursors;
import fr.inria.rivage.gui.WorkArea;
import fr.inria.rivage.tools.Configuration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This class handles the creation of a shape by using a template. It first
 * shows a dialog where the user can choose a template.
 * 
 * @author Tobias Kuhn
 * @see geditor.elements.GTemplate
 */
public class GTemplateConstructor extends GHandler {

	private WorkArea wa;

	private GTemplate template;

	private Point2D p1, p2;

	private Dialog dialog;

	// The dialog where the user can choose a template. This dialog gets shown
	// every
	// time when a new <code>GTemplateConstructor</code> gets created.
	private class Dialog extends JDialog implements ActionListener {

		private TemplatePanel selected;

		private class TemplatePanel extends JPanel {

			private static final int PWIDTH = 80;

			private static final int PHEIGHT = 100;

			private PreviewPanel previewPanel;

			private class PreviewPanel extends JPanel implements MouseListener {

				public GTemplate temp;

				public PreviewPanel(GTemplate template) {
					this.temp = template;
					addMouseListener(this);
				}

				@Override
				public void paint(Graphics g) {
					Color oldColor = g.getColor();
					if (selected == TemplatePanel.this) {
						g.setColor(Color.BLACK);
					} else {
						g.setColor(Color.LIGHT_GRAY);
					}
					g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
					g.setColor(oldColor);

					if (temp.getShape() == null)
						return;

					GObjectShape s;
					try {
						s = (GObjectShape) ((GObjectShape) temp.getShape()).clone();
					} catch (Exception ex) {
						ex.printStackTrace();
						return;
					}

					Graphics2D g2 = (Graphics2D) g;
					int w = getWidth();
					int h = getHeight();
					int b = 10; // minimal gap at each side
					double xs = s.getScreenBounds2D().getX();
					double ys = s.getScreenBounds2D().getY();
					double ws = s.getScreenBounds2D().getWidth();
					double hs = s.getScreenBounds2D().getHeight();
					double f = Math.min((w - 2 * b) / ws, (h - 2 * b) / hs); // scale
																				// factor
					f = Math.min(f, 1); // only down scaling
					Transformer.translate(s, -xs, -ys);
					Transformer.scale(s, new PointDouble(0, 0), f, f);
					Transformer
							.translate(s, (w - ws * f) / 2, (h - hs * f) / 2);
					s.draw(g2);
				}

                @Override
				public void mousePressed(MouseEvent e) {
					selected = TemplatePanel.this;
					Dialog.this.repaint();
				}

                @Override
				public void mouseReleased(MouseEvent e) {
				}

                @Override
				public void mouseClicked(MouseEvent e) {
				}

                @Override
				public void mouseExited(MouseEvent e) {
				}

                @Override
				public void mouseEntered(MouseEvent e) {
				}

			}

			public TemplatePanel(GTemplate template) {
				setLayout(new BorderLayout());
				setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
				this.add(new JLabel(template.getName()), BorderLayout.SOUTH);
				this.add(previewPanel = new PreviewPanel(template),
						BorderLayout.CENTER);
			}

			public GTemplate getTemplate() {
				return previewPanel.temp;
			}

		}

		public Dialog(Point location) {

			super(Application.getApplication().getMainFrame(), "Templates",
					true);

			if (dialog != null)
				dialog.setVisible(false);
			dialog = this;

			addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
					wa.setMode(Handlers.SELECTION);
				}

			});

			setSize(450, 300);
			setResizable(false);
			setLocation(location);

			// create button panel
			JPanel buttonPanel = new JPanel(new FlowLayout());
			JButton buttonOk = new JButton("OK");
			buttonOk.addActionListener(this);
			buttonPanel.add(buttonOk);
			JButton buttonCancel = new JButton("Cancel");
			buttonCancel.addActionListener(this);
			buttonPanel.add(buttonCancel);
			JButton buttonRemove = new JButton("Remove");
			buttonRemove.addActionListener(this);
			buttonPanel.add(buttonRemove);
			JButton buttonSave = new JButton("Save");
			buttonSave.addActionListener(this);
			buttonPanel.add(buttonSave);
			JButton buttonLoad = new JButton("Load");
			buttonLoad.addActionListener(this);
			buttonPanel.add(buttonLoad);
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);

			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			centerPanel.setPreferredSize(new Dimension(440,
					(GTemplate.templates.size() + 4) / 5 * 105));
			getContentPane().add(
					new JScrollPane(centerPanel,
							JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
					BorderLayout.CENTER);

			for (GTemplate gt : GTemplate.templates) {

				TemplatePanel templatePanel = new TemplatePanel(gt);
				centerPanel.add(templatePanel);
			}

			setVisible(true);

		}

		public Dialog() {
			this(new Point(300, 200));
		}

        @Override
		public void actionPerformed(ActionEvent e) {
			String c = e.getActionCommand();

			if ((c.equals("OK") || c.equals("Remove") || c.equals("Save"))
					&& selected == null) {
				// no item selected, but should be
				JOptionPane.showMessageDialog(this, "No template selected!",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (c.equals("OK")) {
				template = selected.getTemplate();
				setVisible(false);
				wa.setCursor(Cursors.importing);
			} else if (c.equals("Cancel")) {
				setVisible(false);
				wa.setMode(Handlers.SELECTION);
			} else if (c.equals("Remove")) {
				int answer = JOptionPane.showConfirmDialog(this,
						"Removing selected template from list?",
						"Remove template", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (answer != JOptionPane.OK_OPTION)
					return;
				GTemplate.templates.remove(selected.getTemplate());
				selected = null;
				new Dialog(getLocation());
			} else if (c.equals("Save")) {
				JFileChooser chooser = new JFileChooser(Configuration
						.getConfiguration().TEMPLATE_FOLDER);
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				File file = chooser.getSelectedFile();
				try {
					selected.getTemplate().save(file);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(),
							"saving file failed", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (c.equals("Load")) {
				JFileChooser chooser = new JFileChooser(Configuration
						.getConfiguration().TEMPLATE_FOLDER);
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				File file = chooser.getSelectedFile();
				try {
					GTemplate.load(file);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, ex.getMessage(),
							"loading file failed", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new Dialog(getLocation());
			}
		}

	}

	GTemplateConstructor() {
	}

	@Override
	public void init(WorkArea wa) {
		this.wa = wa;
		template = null;
		p1 = null;
		p2 = null;
		wa.getSelectionManager().clearSelection();
		new Dialog();
	}

	@Override
	public void draw(Graphics2D g) {
		if (p1 != null && p2 != null) {
			Color oldColor = g.getColor();
			g.setColor(Color.BLACK);
			g.draw(new Rectangle2D.Double(p1.getX(), p1.getY(), p2.getX()
					- p1.getX(), p2.getY() - p1.getY()));
			g.setColor(oldColor);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		p1 = wa.getDrawingPoint(e.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		p2 = wa.getDrawingPoint(e.getPoint());
		wa.lightRepaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		p2 = wa.getDrawingPoint(e.getPoint());
		if (p2.equals(p1))
			return; // mouseClicked() takes action

		GObjectShape shape;
		try {
			shape = Cloner.clone(template.getShape());
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		Transformer.translate(shape, -shape.getScreenBounds2D().getX(), -shape
				.getScreenBounds2D().getY());
		double w = shape.getScreenBounds2D().getWidth();
		double h = shape.getScreenBounds2D().getHeight();
		Transformer.scale(shape, new PointDouble(0, 0), (p2.getX() - p1
				.getX())
				/ (w == 0 ? 1 : w), (p2.getY() - p1.getY()) / (h == 0 ? 1 : h));
		Transformer.translate(shape, p1.getX(), p1.getY());
		fixTemplate(shape);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D point = wa.getDrawingPoint(e.getPoint());
		GObjectShape shape;
		try {
			shape = Cloner.clone(template.getShape());
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// translate the shape so that the center of the shape is at the clicked
		// point
		Transformer.translate(shape, point.getX() - shape.getScreenBounds2D().getX()
				- shape.getScreenBounds2D().getWidth() / 2, point.getY()
				- shape.getScreenBounds2D().getY()
				- shape.getScreenBounds2D().getHeight() / 2);
		fixTemplate(shape);
	}

	// inserts the template into the graphic tree and switches to selection
	// mode.
	private void fixTemplate(GObjectShape shape) {
		wa.getFileController().getConcurrencyController().doAndSendOperation(
				wa.getCreateOperation(shape));

		wa.setMode(Handlers.SELECTION);
	}

}

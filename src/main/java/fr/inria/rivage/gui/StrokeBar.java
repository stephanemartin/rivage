package fr.inria.rivage.gui;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.serializable.SerBasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class StrokeBar extends JComponent {

    protected MainFrame parent;
    protected StrokePanel strokePanel;
    private StrokeChooserFrame strokeChooserFrame;
    private boolean enabled;
    private float stroke;

    public StrokeBar(MainFrame parent, String toolTipText) {
        this.parent = parent;
        enabled = false;
        setToolTipText(toolTipText);
        setFocusable(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createRaisedBevelBorder(), BorderFactory
                .createLoweredBevelBorder()));
        setBounds(0, 0, 22, 22);
        strokePanel = new StrokePanel();
        strokePanel.addMouseListener(new MouseHandler());
        strokePanel.setToolTipText(toolTipText);
        add(strokePanel);
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!enabled) {
                return;
            }
            strokeChooserFrame = new StrokeChooserFrame();
            strokeChooserFrame.setVisible(true);
        }
    }

    private class StrokeChooserFrame extends JDialog implements ActionListener {

        private JButton ok, cancel;
        private JSlider slider;

        public StrokeChooserFrame() {
            super(parent, "Stroke Chooser", true);
            setResizable(false);

            slider = new JSlider(JSlider.HORIZONTAL, 0, 12, (int) stroke);
            slider.setFocusable(false);
            slider.setLabelTable(slider.createStandardLabels(2));
            slider.setMajorTickSpacing(2);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setSnapToTicks(true);
            slider.setBorder(BorderFactory
                    .createTitledBorder("Choose Line Stroke"));

            ok = new JButton("Ok");
            ok.addActionListener(this);
            cancel = new JButton("Cancel");
            cancel.addActionListener(this);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(ok);
            buttonPanel.add(cancel);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(slider, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width - getWidth()) / 2,
                    (screenSize.height - getHeight()) / 2);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == ok) {
                stroke = slider.getValue();
                strokePanel.repaint();
                Application.getApplication().getCurrentFileController()
                        .getCurrentWorkArea().updateColors(null, null);
                this.dispose();
            } else if (e.getSource() == cancel) {
                this.dispose();
            }
        }
    }

    private class StrokePanel extends JPanel {

        public StrokePanel() {
            setFocusable(false);
        }

        @Override
        public void paint(Graphics g) {
            if (enabled) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            if (enabled) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.GRAY);
            }
            g.fillOval((getWidth() - (int) stroke) / 2,
                    (getHeight() - (int) stroke) / 2, (int) stroke + 1,
                    (int) stroke + 1);
            g.setColor(Color.GRAY);
            g.drawOval((getWidth() - (int) stroke) / 2,
                    (getHeight() - (int) stroke) / 2, (int) stroke,
                    (int) stroke);
        }
    }

    public void setStroke(Stroke stroke) {

        if (stroke instanceof SerBasicStroke) {
            this.stroke = ((SerBasicStroke) stroke).getLineWidth();
        }
        strokePanel.repaint();
    }

    public Stroke getStroke() {
        return new SerBasicStroke(stroke);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        strokePanel.repaint();
    }
}
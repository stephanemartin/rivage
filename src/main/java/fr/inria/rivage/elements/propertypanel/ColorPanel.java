package fr.inria.rivage.elements.propertypanel;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GObjectShape;
import fr.inria.rivage.gui.PropertyPanel;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.operations.ModifyOperation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 * This is a panel for a set of objects that implement the interface IColorable,
 * in order to be able to change their color.
 *
 * @author Yves
 * @author Tobias Kuhn
 */
public class ColorPanel extends JPanel implements MouseListener, ActionListener, IPropPanel {

    private ArrayList<GObjectShape> objs = new ArrayList<GObjectShape>();
    private PropertyPanel pp;
    private JLabel frtLabel;
    private JLabel bckLabel;
    private JButton btnFrtClear;
    private JButton btnBckClear;
    private JButton btnSwap;

    /**
     * Creates a new panel for the set of colorable objects.
     *
     * @param objs the colorable objects
     */
    public ColorPanel(PropertyPanel pp) {
        this.pp = pp;

        setLayout(null);
        //setBorder(BorderFactory.createLoweredBevelBorder());
        setPreferredSize(new Dimension(130, 80));
        setSize(new Dimension(130, 80));

        frtLabel = new JLabel();
        bckLabel = new JLabel();

        btnFrtClear = new JButton();
        btnFrtClear.setFont(new Font("Arial", Font.BOLD, 15));
        btnFrtClear.setMargin(new Insets(0, 0, 0, 0));
        btnFrtClear.setText("/");
        btnFrtClear.addActionListener(this);
        btnFrtClear.setActionCommand("ClearFrtColor");

        btnBckClear = new JButton();
        btnBckClear.setFont(new Font("Arial", Font.BOLD, 15));
        btnBckClear.setMargin(new Insets(0, 0, 0, 0));
        btnBckClear.setText("/");
        btnBckClear.addActionListener(this);
        btnBckClear.setActionCommand("ClearBckColor");

        btnSwap = new JButton();
        btnSwap.setFont(new Font("Arial", Font.BOLD, 15));
        btnSwap.setMargin(new Insets(0, 0, 0, 0));
        btnSwap.setText("S");
        btnSwap.addActionListener(this);
        btnSwap.setActionCommand("SwapColors");

        frtLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bckLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        frtLabel.setOpaque(true);
        bckLabel.setOpaque(true);

        frtLabel.addMouseListener(this);
        bckLabel.addMouseListener(this);

        frtLabel.setFont(new Font("Arial", Font.BOLD, 25));
        frtLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bckLabel.setFont(new Font("Arial", Font.BOLD, 25));
        bckLabel.setHorizontalAlignment(SwingConstants.CENTER);

        frtLabel.setBounds(40, 10, 30, 30);
        bckLabel.setBounds(60, 30, 30, 30);
        btnFrtClear.setBounds(10, 15, 20, 20);
        btnBckClear.setBounds(100, 35, 20, 20);
        btnSwap.setBounds(30, 50, 20, 20);

        add(btnSwap);
        add(btnBckClear);
        add(btnFrtClear);
        add(frtLabel);
        add(bckLabel);
    }

    /**
     * Changes the front color.
     *
     * @param c the new color.
     */
    private void setFrtColor(Color c) {
        IConcurrencyController cc = Application.getApplication().getCurrentFileController().getConcurrencyController();
        for (GObjectShape obj : objs) {
            cc.doAndSendOperation(new ModifyOperation(obj, Parameters.ParameterType.FgColor, c));
        }


    }

    /**
     * Changes the back color.
     *
     * @param c the new color.
     */
    private void setBckColor(Color c) {
        IConcurrencyController cc = Application.getApplication().getCurrentFileController().getConcurrencyController();
        for (GObjectShape obj : objs) {
            cc.doAndSendOperation(new ModifyOperation(obj, Parameters.ParameterType.BgColor, c));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (objs.isEmpty()) {
            return;
        }

        if (e.getSource() instanceof JLabel) {
            JLabel lbl = (JLabel) e.getSource();
            if (!lbl.isEnabled()) {
                return;
            }
            Color newColor = JColorChooser.showDialog(this, "Choose a color", lbl.getBackground());
            if (newColor == null) {
                return;
            }
            if (lbl == frtLabel) {
                setFrtColor(newColor);
            } else {
                setBckColor(newColor);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (objs.isEmpty()) {
            return;
        }

        if (e.getActionCommand().equals("ClearFrtColor")) {
            setFrtColor(new Color(0, 0, 0, 0));
        } else if (e.getActionCommand().equals("ClearBckColor")) {
            setBckColor(new Color(0, 0, 0, 0));
        } else if (e.getActionCommand().equals("SwapColors")) {
            /*Color temp = getFrtColor();
            setFrtColor(getBckColor());*
            setBckColor(temp);*/
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }

    @Override
    public void refreshObject() {
        /*objs.clear();
        int colReg = IColorable.BACK_ONLY | IColorable.FRONT_ONLY;

        objs.addAll(pp.getObjects());

        if (objs.isEmpty()) {
            return;
        }

        pp.addTab("Color", this);

        if ((colReg & IColorable.FRONT_ONLY) > 0) {
            frtLabel.setEnabled(true);
            Color frtColor = getFrtColor();
            frtLabel.setBackground(frtColor);
            frtLabel.setText((frtColor == null ? "/" : ""));
            btnFrtClear.setEnabled(true);
        } else {
            frtLabel.setEnabled(false);
            frtLabel.setBackground(null);
            frtLabel.setText("");
            btnFrtClear.setEnabled(false);
        }


        if ((colReg & IColorable.BACK_ONLY) > 0) {
            bckLabel.setEnabled(true);
            Color bckColor = getBckColor();
            bckLabel.setBackground(bckColor);
            bckLabel.setText((bckColor == null ? "/" : ""));
            btnBckClear.setEnabled(true);
        } else {
            bckLabel.setEnabled(false);
            bckLabel.setBackground(null);
            bckLabel.setText("");
            btnBckClear.setEnabled(false);
        }

        if (colReg == IColorable.FRONT_BACK) {
            btnSwap.setEnabled(true);
        } else {
            btnSwap.setEnabled(false);
        }*/
    }

    /**
     * Returns the cumulated front color. if no object is front colorable then
     * null gets returned. if the front colorable objects do not have the same
     * front color then the color gray gets returned.
     *
     * @return the cumulated front color
     */
    /*private Color getFrtColor() {
        Color c = null;
        for (GObjectShape obj : objs) {
            if (!(obj instanceof IColorable)) {
                return Color.GRAY;
            }
            Color cn = ((IColorable) obj).getFrtColor();
            if (cn == null) {
                continue;
            }
            if (c != null && !c.equals(cn)) {
                return Color.GRAY;
            }
            c = cn;
        }
        return c;
    }*/

    /**
     * Returns the cumulated back color. if no object is back colorable then
     * null gets returned. if the back colorable objects do not have the same
     * back color then the color gray gets returned.
     *
     * @return the cumulated back color
     */
   /* private Color getBckColor() {
        Color c = null;
        for (GObjectShape obj : objs) {
            if (!(obj instanceof IColorable)) {
                return Color.GRAY;
            }
            Color cn = ((IColorable) obj).getBckColor();
            if (cn == null) {
                continue;
            }
            if (c != null && !c.equals(cn)) {
                return Color.GRAY;
            }
            c = cn;
        }
        return c;
    }*/
}
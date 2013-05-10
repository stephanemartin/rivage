/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2013 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.inria.rivage.gui.toolbars;

import fr.inria.rivage.elements.serializable.SerBasicStroke;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class StrokeRenderer extends JComponent implements ListCellRenderer {

    private Stroke stroke;

    public StrokeRenderer() {
        setPreferredSize(new Dimension(150, 18));
        //setMinimumSize(new Dimension(150, 18));
    }

    public StrokeRenderer(Stroke stroke) {
        this();
        this.stroke = stroke;
    }
    

    @Override
    public void paintComponent(final Graphics g) {

        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final double xx = insets.left;
        final double yy = insets.top;
        final double ww = size.getWidth() - insets.left - insets.right;
        final double hh = size.getHeight() - insets.top - insets.bottom;
        double d = 0;
        if (stroke instanceof SerBasicStroke) {
            SerBasicStroke st = (SerBasicStroke) stroke;
            if (st.getEndCap() != BasicStroke.CAP_BUTT) {
                d = (double) st.getLineWidth() / 2.0;
            }
        }
        final Point2D one = new Point2D.Double(xx + 6 + d, yy + hh / 2);
        final Point2D two = new Point2D.Double(xx + ww - 6 - d, yy + hh / 2);
        final Line2D line = new Line2D.Double(one, two);
        if (this.stroke != null) {
            g2.setStroke(this.stroke);
            g2.draw(line);
        }

    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Stroke) {
            stroke = (Stroke) value;
        } else {
            stroke = null;
        }
        return this;
    }
}

/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
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
package fr.inria.rivage.tools;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class JColorShow extends JPanel {

    private Color color;
    final static Color part1 = Color.BLACK;
    final static Color part2 = Color.WHITE;
    private String label;
    
    public JColorShow(Color color) {
        this.color = color;
        this.setBackground(part1);
    }

    public void setLabel(String label) {
        this.label = label;
        this.updateUI();
    }

    public Color getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    public void setColor(Color color) {
        this.color = color;
        this.updateUI();
    }
    
 

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(part2);
        g.fillRect(0, 0, this.getWidth() / 2, this.getHeight() / 2);
        g.fillRect(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2, this.getHeight() / 2);
        g.setColor(color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (label != null) {
            g.setColor(new Color((int)(Math.pow(2,24)-this.getColor().getRGB()-1)));
            FontMetrics fm=g.getFontMetrics();
            g.drawChars(label.toCharArray(), 0,
                    label.length(),
                    this.getWidth() / 2 - fm.stringWidth(label)/2,
                    this.getHeight() / 2 + fm.getAscent()/2);
            
        }
    }
}

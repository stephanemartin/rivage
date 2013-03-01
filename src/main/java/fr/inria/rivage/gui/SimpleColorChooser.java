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
package fr.inria.rivage.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class SimpleColorChooser extends JFrame {

    private JButton changeColor;
    private Color color = Color.lightGray;
    private Container c;

    public SimpleColorChooser() {
        super("Using JColorChooser");

        c = getContentPane();
        c.setLayout(new FlowLayout());

        changeColor = new JButton("Change Color");
        changeColor.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        color =
                                JColorChooser.showDialog(SimpleColorChooser.this,
                                "Choose a colour", color);

                        if (color == null) {
                            color = Color.blue;
                        }

                        c.setBackground(color);
                        c.repaint();
                    }
                });
        c.add(changeColor);

        setSize(400, 130);
        show();
    }

    public static void main(String args[]) {
        SimpleColorChooser app = new SimpleColorChooser();

        app.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
    }
}

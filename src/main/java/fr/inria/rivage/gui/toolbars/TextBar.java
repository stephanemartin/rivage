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

import fr.inria.rivage.gui.listener.CurrentWorkAreaListener;
import fr.inria.rivage.gui.listener.SelectionChangeListener;
import fr.inria.rivage.gui.listener.TreeChangeListener;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public class TextBar extends JToolBar implements
        TreeChangeListener, SelectionChangeListener, CurrentWorkAreaListener {

    private static final String[] DEFAULT_FONT_SIZE_STRINGS =
	{
		"8", "9", "10", "11", "12", "14", "16", "18", "20",
		"22", "24", "26", "28", "36", "48", "72",
	};

    JComboBox size;
    JComboBox police;
    
    public TextBar() {
        super("TextBar", JToolBar.HORIZONTAL);
        
    }
    
    public void treeChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void selectionChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void currentWorkAreaChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

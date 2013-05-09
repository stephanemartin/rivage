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
package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CutCopyObj extends AbstractAction {

    boolean cut = false;

    public CutCopyObj(boolean cut) {
        this.cut = cut;
        String action;
        if (cut) {
            action = "Cut";
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_X);
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        } else {
            action = "Copy";
            this.putValue(MNEMONIC_KEY, KeyEvent.VK_C);
            this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        }
        this.putValue(NAME, action);
        this.putValue(SHORT_DESCRIPTION, action + " selected Objects");

    }

    @Override
    public boolean isEnabled() {
        try {
            /*return Application.getApplication().getCurrentFileController()
                    .getCurrentWorkArea().getSelectionManager().getSelObjects().size() > 0;*/
        } catch (Exception ex) {
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        //FileController fc = Application.getApplication().getCurrentFileController();
        Application.getApplication().getClipboard().copy();

        if (cut){
            Actions.DELETE_OBJS.doAction();
        }
        
    }
}

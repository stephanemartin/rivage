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
import fr.inria.rivage.elements.GLayer;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.listener.LayerChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
class MoveLayer extends AbstractAction {

    public static enum Action {

        Up, Down
    };
    Action action;

    public MoveLayer(Action action) {
        this.action = action;
        String upsidedown;
        if (action == Action.Up) {
            upsidedown = "up";
        } else {
            upsidedown = "down";
        }
        this.putValue(AbstractAction.NAME, upsidedown + " layer");
        this.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Push the selected layer " + upsidedown);
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
				Application.class
						.getResource("resources/images/"+upsidedown+".png")));

    }

    @Override
    public boolean isEnabled() {
        try {
            return Application.getApplication().getCurrentFileController().getInnerWindow().getSelectedPage().size() > 1;
        } catch (Exception ex) {
            return false;
        }
    }

    public void actionPerformed(ActionEvent e) {
        FileController fc=Application.getApplication().getCurrentFileController();
        Page p=fc.getInnerWindow().getSelectedPage();
        GLayer l=fc.getCurrentWorkArea().getActiveLayer();
        
       if(action == Action.Up){
           fc.getDocument().zPosDown(p, l);
       }else{
           fc.getDocument().zPosUp(p, l);
       }
       Application.getApplication().getMainFrame().getLayersToolBar().layerChanged(LayerChangeListener.Type.LAYER_MOVED);
       Application.getApplication().getCurrentFileController().getCurrentWorkArea().treeChanged();
    }
}

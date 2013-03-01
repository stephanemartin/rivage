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
package fr.inria.rivage.engine.operations;

import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CreateGroup extends Operation {

    public CreateGroup(GGroup group) {
        this.group=group;
        this.id = group.getId();
        ///this.group.setId(id);
        group.clearObj();


    }
    GGroup group;

    @Override
    public List<ID> dependOf() {
        return Arrays.asList(group.getParentId());
    }

    @Override
    protected void doApply(FileController fileController) throws UnableToApplyException {
        group.restaureFromId(fileController.getDocument());
        fileController.getDocument().add(group);
    }

    @Override
    protected void doUnapply(FileController fileController) throws UnableToApplyException {

        fileController.getDocument().remove(group);
    }
}

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
package fr.inria.rivage.engine.manager;

import fr.inria.rivage.users.User;
import java.util.HashMap;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public interface GroupController {

    /**
     * Returns the hash conaining the current siteIDs in this group and the User
     * class for each site.
     *
     * @return a hash with siteIDs as key and User class as data.
     */
    @SuppressWarnings(value = "unchecked")
    HashMap<Long, User> getMembers();

    /**
     * This method makes the calling thread block until the group has been
     * joined.
     *
     * @throws InterruptedException if another thread interrupted the current
     * thread.
     */
    void groupJoined() throws InterruptedException;

    void run();
    
}

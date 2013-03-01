/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.rivage.engine.concurrency;

import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.elements.GDocument;
import java.io.Serializable;

/**
 *
 * @author Stephane Martin <stephane@stephanemartin.fr>
 */
public interface DocumentSync extends Serializable {

    public ID getID();

    public GDocument getGDocument();
}

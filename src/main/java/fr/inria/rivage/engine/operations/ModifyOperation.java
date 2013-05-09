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

import fr.inria.rivage.elements.ColObject;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.renderer.AffineTransformRenderer;
import fr.inria.rivage.engine.concurrency.crdt.CRDTParameter;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameter;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.manager.FileController;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class ModifyOperation extends Operation implements Serializable {
    ID target;
    Parameter param;
    private static final Logger LOG = Logger.getLogger(ModifyOperation.class.getName());
   
    
    /*final void saveParam(){
        test1=param.getElement();
       test2=((CRDTParameter)param).getVersion();
        test3=((CRDTParameter)param).getId();
        
    }*/
   // Parameters.ParameterType type;

    public ModifyOperation(ID target, Parameter param) {
        this.target = target;
        this.param =param;
       // saveParam();
    }

    public ModifyOperation(GObject obj,Parameters.ParameterType type) {
        target=obj.getId();
        param=(CRDTParameter)obj.getParameters().getParameter(type);
        //saveParam();
    }
    
    
    public ModifyOperation(GObject obj,Parameters.ParameterType type,Object newP) {
        target=obj.getId();
        param=(CRDTParameter)obj.getParameters().getParameter(type);
        param.localUpdate(newP,0);
        param.acceptMod();
        //saveParam();
        
    }

    public ModifyOperation() {
    }

    public Parameter getParam() {
        return param;
    }
    
    
    @Override
    protected void doApply(FileController fileController) throws UnableToApplyException {
       ColObject go=fileController.getDocument().getObjectById(target);
       if(go==null){
           LOG.log(Level.WARNING, "Object id {0} not found this object could be deleted in concurency", target);
           return;
       }
       //TODO: find a more beautifull think
       if (param.getType()==Parameters.ParameterType.Zpos && !(go instanceof AffineTransformRenderer)){
           //for
           go.deleteMeFromParent();
       }
       
       go.getParameters().remoteUpdateParameter(param);
       if (param.getType()==Parameters.ParameterType.Zpos && !(go instanceof AffineTransformRenderer)){
           //for
           go.addMeFromParent();
       }
       fileController.getCurrentWorkArea().treeChanged();
    }

    @Override
    protected void doUnapply(FileController fileController) throws UnableToApplyException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ID> dependOf() {
       return Arrays.asList(target);
    }

    /*@Override
    public String toString() {
        return "ModifyOperation{" + "target=" + target + ", param=" + param + '}';
    }*/

    @Override
    public String toString() {
        return "ModifyOperation{id =" +this.getId()+ "target=" + target + ", param=" + param +'}';
    }

    public ID getTarget() {
        return target;
    }

    public void setTarget(ID target) {
        this.target = target;
    }

   /* public CRDTParameter getParam() {
        return param;
    }*/

    
    
}

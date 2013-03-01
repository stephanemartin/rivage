/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inria.rivage.engine.concurrency.crdt;

import fr.inria.rivage.engine.concurrency.crdt.CRDTParameter;
import fr.inria.rivage.engine.operations.ModifyOperation;
import fr.inria.rivage.net.group.Message;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;
/*
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class CRDTParameterTest  {
    
    
  /*
   * Test if serializabme
   * 
   */
    @Test 
    public void testSerilizable () throws IOException, ClassNotFoundException{
          ByteArrayOutputStream out =new ByteArrayOutputStream();
          
        ObjectOutputStream outO = new ObjectOutputStream(out);
        CRDTParameter f =new CRDTParameter("zfz");
        f.id=UUID.randomUUID();
        f.version=3;
        Message param=new Message(null,new ModifyOperation(null,f));
        outO.writeObject(param);
        
        
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        Message param2= (Message) in.readObject();
        CRDTParameter f2=(CRDTParameter)((ModifyOperation)param2.getOp()).getParam();
        assertEquals(f.getId(),f2.getId());
        assertEquals(f.getVersion(),f2.getVersion());
        assertEquals(f.getElement(),f2.getElement());
        System.out.println("F&"+f);
        System.out.println("f2"+f2);
        
    }
}

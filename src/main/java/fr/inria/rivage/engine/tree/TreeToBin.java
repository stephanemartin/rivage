/*
 * Created on Jun 22, 2004
 */
package fr.inria.rivage.engine.tree;

import fr.inria.rivage.elements.interfaces.IGroup;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

/**
 * @author Yves
 */
public class TreeToBin {
		
		ObjectOutputStream oos;
		
		public TreeToBin(String filename) throws IOException{
			RandomAccessFile raf = new RandomAccessFile(filename,"rw");
			raf.setLength(0); // Deletes the files content in case it exists
			oos = new ObjectOutputStream(new FileOutputStream(raf.getFD()));
		}
		
		public void saveToFile(IGroup root) throws IOException{
			oos.writeObject(root);
		}
}

package fr.inria.rivage.elements;

import fr.inria.rivage.elements.interfaces.ITreeElement;
import fr.inria.rivage.engine.tree.DecodeException;
import fr.inria.rivage.engine.tree.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A template consists of a shape and a name. A template itself is not an
 * <code>GObjectShape</code>, so it cannot appear in the graphic tree.
 * 
 * @author Tobias Kuhn
 * @see geditor.elements.handlers.GTemplateConstructor
 */
public class GTemplate {

	/**
	 * A list of all existing templates. It's public for easy
	 * access, but you should not directly insert elements. (The constructor
	 * inserts every new template automatically.)
	 */
	public static ArrayList<GTemplate> templates = new ArrayList<GTemplate>();

	private GObject shape;

	private String name;
	

	/**
	 * Creates a new template with given shape and name. The new template
	 * becomes a member of <code>templates</code>. The shape gets cloned
	 * first, so later changes of the original shape will not affect the
	 * template.
	 * 
	 * @param shape the shape of the template (a simple shape or a group)
	 * @param name the template's name
	 */
	public GTemplate(GObject shape, String name) throws CloneNotSupportedException {
		this.shape = Cloner.clone(shape);

		if (name == null || name.equals(""))
			name = "(unnamed)";
		this.name = name;
		templates.add(this);
	}
	
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns the name of the template.
	 * 
	 * @return the template's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the template.
	 * 
	 * @param name the template's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the shape of the template. Before using this (like inserting in a
	 * graphic tree) you should get a clone of it for preventing the template
	 * get changed.
	 * 
	 * @return the shape of the template
	 */
	public GObject getShape() {
		return shape;
	}

	/**
	 * Saves the template in the file.
	 * 
	 * @param file the file to save in
	 * @throws IOException if saving does not succeed
	 */
	public void save(File file) throws IOException {
	/*	String xmlString = XMLEncoder.getAsString(
				XMLEncoder.getXMLElement(shape));
		FileOutputStream out = new FileOutputStream(file);
		out.write(xmlString.getBytes());
		out.close();*/
	}

	/**
	 * Loads a template from the file.
	 * 
	 * @param file the file that contains the template to open
	 * @return the loaded template
	 * @throws IOException if opening the template does not succeed
	 */
	public static GTemplate load(File file) throws IOException, DecodeException, CloneNotSupportedException {
		FileInputStream in = new FileInputStream(file);
		byte[] input = new byte[in.available()];
		in.read(input);
		in.close();

		ITreeElement shape = XMLDecoder.getTreeElement(
				XMLDecoder.getXMLElement(new String(input)));
		return new GTemplate((GObjectShape) shape, file.getName());
	}

	/**
	 * Loads all template files in the given directory. Exceptions are ignored.
	 * 
	 * @param dir the directory with the files to load
	 * @throws FileNotFoundException in case the directory can't be found.
	 */
	public static void loadDir(File dir) throws FileNotFoundException {
		File[] files = dir.listFiles();
		if (files == null)
			throw new FileNotFoundException(
					"The template directory could not be found.");
		for (int i = 0; i < Array.getLength(files); i++) {
			try {
				load(files[i]);
			} catch (Exception ex) {}
		}
	}

}

package fr.inria.rivage.elements.interfaces;

/**
 * This interfaces describes a graphical object that has a text.
 * @author Tobias Kuhn
 */
public interface ITextable {
	
	/** 
	 * Sets the current text of the object.
	 * @param text the text
	 */
	public void setText(String text);
	
	/**
	 * Returns the current text of the object.
	 * @return the text
	 */
	public String getText();
	
	/**
	 * Defines whether the text has a resizable font or not.
	 * @return true if the font is resizable.
	 */
	public boolean isFontResizable();
		
	/**
	 * Sets the font size. This should have no effect if isFontResizable()
	 * returns false.
	 * @param fontSize the new font size
	 */
	public void setFontSize(int fontSize);
	
	/**
	 * Returns the current font size. If isFontResizable() returns false
	 * this method should return 0.
	 * @return
	 */
	public int getFontSize();
	
}

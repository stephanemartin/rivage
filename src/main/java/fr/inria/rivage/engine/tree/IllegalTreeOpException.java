package fr.inria.rivage.engine.tree;


/**
 * This exception gets thrown if you try to do an illegal operation on
 * a GraphicTree object.
 * @author Tobias Kuhn
 */
public class IllegalTreeOpException extends Exception {
	
	private String text;
	
	public IllegalTreeOpException(String text) {
        this.text = text;
    }	
	
	@Override
    public String toString() {
        return "Illegal tree operation: " + text;
    }

}

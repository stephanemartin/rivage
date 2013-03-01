package fr.inria.rivage.engine.algorithms;

public interface LinkFunction<T> {

	/**
	 * This function tell if the objects o1 and o2 are linked.
	 * 
	 * @param o1 the source object.
	 * @param o2 the target object.
	 * @return true if there is an edge between them.
	 */
	public boolean isLinked(T o1, T o2);

}

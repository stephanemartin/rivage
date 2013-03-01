package fr.inria.rivage.engine.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * This class creates a graph with the links being given by the linkFunction,
 * then it orders all the elements according to the comparator. The order the
 * objects are given back if from high priority down to low priotity, if they
 * are not dependent on some other operation.
 * 
 * @author yves
 * 
 * @param <T>
 */
public class PriorityChooser<T> {

	private ArrayList<T> elements;

	private LinkFunction<T> linkFunction;

	private Comparator<T> comparator;

	private ArrayList<Node> nodeList;

	private ArrayList<Node> cancelled;
	private ArrayList<Node> taken;

	public PriorityChooser(ArrayList<T> elements, LinkFunction<T> linkFunction,
			Comparator<T> comparator) {
		this.elements = elements;
		this.linkFunction = linkFunction;
		this.comparator = comparator;
		this.nodeList = new ArrayList<Node>();
		this.cancelled = new ArrayList<Node>();
		this.taken = new ArrayList<Node>();
	}

	@SuppressWarnings("unchecked")
	public void computeOrdering() {
		ArrayList<T> elCopy = (ArrayList<T>) elements.clone();
		Collections.sort(elCopy, comparator);
		for (T t : elCopy) {
			nodeList.add(new Node(t));
		}

		for (int i = 0; i < nodeList.size(); i++)
			for (int j = i+1; j < nodeList.size(); j++) {
				Node n1 = nodeList.get(i);
				Node n2 = nodeList.get(j);

				if (linkFunction.isLinked(n1.value, n2.value)) {
					n1.addLink(n1);
					n2.addLink(n1);
				}
			}
		
		for(int i = nodeList.size()-1;i>=0;i--){
			Node n = nodeList.get(i);
			if(!n.deleted){
				taken.add(n);
				for(Node l : n.getList())
					l.deleted = true;
			} else {
				cancelled.add(n);
			}
		}
		
	}

	public T getNext() {
		if(nodeList.size()<=0) return null;
		return nodeList.remove(0).value;
	}

	public ArrayList<T> getCanceled() {
		ArrayList<T> nodes = new ArrayList<T>();
		for(Node n : cancelled){
			nodes.add(n.value);
		}
		return nodes;
	}

	private class Node {

		T value;

		private HashSet<Node> link;
		
		boolean deleted;

		public Node(T value) {
			this.value = value;
			this.link = new HashSet<Node>();
			this.deleted = false;
		}

		public HashSet<Node> getList() {
			return link;
		}

		public void addLink(Node node) {
			link.add(node);
		}

		public void removeLink(Node node) {
			link.remove(node);
		}

		public int countLink() {
			return link.size();
		}
	}

}

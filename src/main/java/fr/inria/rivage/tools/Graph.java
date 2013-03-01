package fr.inria.rivage.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * TODO : optimizations
 * TODO : add exceptions
 */

public class Graph <NodeType, LabelType> 
{

	public class EdgeEntry
	{
		private NodeType node;
		private LabelType label;
		
		public EdgeEntry (NodeType node, LabelType label)
		{
			this.node = node; this.label = label;
		}
		
		public NodeType getNode ()
		{
			return node;
		}
		
		public LabelType getLabel ()
		{
			return label;
		}
	}; 

	private Map <NodeType, ArrayList <EdgeEntry> > outEdges;
	private Map <NodeType, ArrayList <EdgeEntry> > inEdges;

	public Graph ()
	{
		inEdges = new HashMap<NodeType, ArrayList <EdgeEntry> > ();
		outEdges = new HashMap<NodeType, ArrayList <EdgeEntry> > ();
		
	}
	
	public void addNode (NodeType node)
	{
		///node exists
		if (outEdges.get(node) != null) return;
		outEdges.put(node, new ArrayList <EdgeEntry> ());
		inEdges.put(node, new ArrayList <EdgeEntry> ());
	}
	
	public void addEdge (NodeType node0, NodeType node1, LabelType label)
	{
		addNode (node0);
		addNode (node1);
		
		outEdges.get(node0).add (new EdgeEntry (node1, label));
		inEdges.get(node1).add (new EdgeEntry (node0, label));
				
	}
	
	public long getInDegree (NodeType node)
	{
		ArrayList <EdgeEntry> l = inEdges.get(node);
		if (l == null) return 0;
		return l.size();
	}
	
	public long getOutDegree (NodeType node)
	{
		ArrayList <EdgeEntry> l = outEdges.get(node);
		if (l == null) return 0;
		return l.size();
	}
	
	public LabelType getEdge (NodeType node0, NodeType node1)
	{
		ArrayList <EdgeEntry> l = outEdges.get(node0);
		if (l == null) return null;
		for (EdgeEntry e : l)
			if (e.node.equals(node1)) 
					return e.label; 
		return null;		
	}

	public ArrayList<EdgeEntry> getOutNeighbors (NodeType node)
	{
		ArrayList <EdgeEntry> l = outEdges.get(node);
		if (l == null) 
				return new ArrayList <EdgeEntry> ();
		else
			return (ArrayList<Graph<NodeType, LabelType>.EdgeEntry>) l.clone();		
	}
	
	public ArrayList<EdgeEntry> getInNeighbors (NodeType node)
	{
		ArrayList <EdgeEntry> l = inEdges.get(node);
		if (l == null) 
				return new ArrayList <EdgeEntry> ();
		else
			return (ArrayList<Graph<NodeType, LabelType>.EdgeEntry>) l.clone();		
	}

	
	public ArrayList<NodeType> getOutNeighbors (NodeType node, LabelType label)
	{
		ArrayList <EdgeEntry> l = outEdges.get(node);
		ArrayList <NodeType> result = new ArrayList <NodeType> ();
		for (EdgeEntry e : l)
			if (e.getLabel().equals(label))
								result.add (e.getNode());
		return result;						
	}
	
	public ArrayList<NodeType> getInNeighbors (NodeType node,  LabelType label)
	{
		ArrayList <EdgeEntry> l = inEdges.get(node);
		ArrayList <NodeType> result = new ArrayList <NodeType> ();
		for (EdgeEntry e : l)
			if (e.getLabel().equals(label))
								result.add (e.getNode());
		return result;						
	}
	
	public void clear ()
	{
		inEdges.clear(); outEdges.clear();
	}
	
	public void removeEdge (NodeType node0, NodeType node1)
	{
		ArrayList <EdgeEntry> l0 = outEdges.get(node0);
		ArrayList <EdgeEntry> l1 = inEdges.get(node1);
		if (l0 == null || l1 == null) return;
		for (int i = 0; i < l0.size(); i++)
			if (l0.get(i).getNode ().equals (node1))
			{
				l0.remove(i); break;
			}
		for (int i = 0; i < l1.size(); i++)
			if (l0.get(i).getNode ().equals (node0))
			{
				l1.remove(i); break;
			}				
	}
	
	public void removeNode (NodeType node)
	{
		ArrayList <EdgeEntry> l;
		
		l = outEdges.get(node);	if (l == null) return;
		for (EdgeEntry e : l)
		{
			ArrayList <EdgeEntry> ll = inEdges.get(e.getNode());
			for (int i = 0; i < ll.size(); i++)
				if (ll.get(i).getNode ().equals (node))
				{
					ll.remove(i); break;
				}			
		}
		outEdges.remove(node);

		l = inEdges.get(node);	if (l == null) return;
		for (EdgeEntry e : l)
		{
			ArrayList <EdgeEntry> ll = outEdges.get(e.getNode());
			for (int i = 0; i < ll.size(); i++)
				if (ll.get(i).getNode ().equals (node))
				{
					ll.remove(i); break;
				}	
		}
		
		inEdges.remove(node);
	}
	
	public ArrayList<NodeType> getNodes ()
	{
		ArrayList<NodeType> result = new ArrayList<NodeType>();
		Object nodes[] = inEdges.keySet ().toArray();
		for (int i = 0; i < nodes.length; i++)
			result.add((NodeType) nodes[i]);
			
		return result;
	}

}

package fr.inria.rivage.engine.concurrency.resconcurrency;

import fr.inria.rivage.engine.concurrency.exceptions.CCException;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.users.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This is the history class for all the users. When this class is created, the
 * same users and the same number of users should be present at each site, so
 * the resulting positionis in the vector are the same at each site.
 * 
 * @author yves
 * 
 */
public class History {

	/*
	 * Implicitely the history has a partial order as operations that are not
	 * causally ready aren't executed.
	 */

	private long[] currentVector;

	private ArrayList<OpWrapper> history;

	/*
	 * The map to find the integers.
	 */
	private HashMap<Long, Integer> id2pos;

	/*
	 * The position of the local site in the array.
	 */
	private int myPos;

	public History(HashMap<Long, User> members, long siteID, FileController fc) {
		id2pos = new HashMap<Long, Integer>();
		history = new ArrayList<OpWrapper>();

		ArrayList<Long> al = new ArrayList<Long>(members.keySet());
		Collections.sort(al);

		currentVector = new long[al.size()];
		for (int i = 0; i < al.size(); i++) {
			id2pos.put(al.get(i), i);
			if (al.get(i) == siteID)
				myPos = i;
			currentVector[i] = 0;
		}
	}

	public long[] currentVector() {
		return currentVector.clone();
	}

	/**
	 * This method has to be used to get the vector for the next local
	 * operation. If it is not, there will be many problems with the numbers in
	 * the vector.
	 * 
	 * @return the vector for the next local operation.
	 */
	public long[] vectorForNextLOP() {
		currentVector[myPos]++;
		return currentVector.clone();
	}

	/**
	 * Checks if an operation is causally ready.
	 * 
	 * @param op
	 *            the operation
	 * @return true if it is ready, false otherwise.
	 */
	public boolean isCausallyReady(OpWrapper op) {
		/*
		 * For a new operation to be causally ready, there has to be only one
		 * difference in the vector according to the latest operations received
		 * from every site. Assume the local vector is '111', then '121' would
		 * be ready, but '122' (because we haven't received the operation of
		 * site 2 yet) or '113' (because we have a missing operation from site
		 * 3) wouldn't be.
		 */
		/*
		 * TODO '122' should probably taken as ready too.
		 */
/*		int poscount = 0;
		for (int i = 0; i < currentVector.length; i++)
			if (i != myPos) {
				if (currentVector[i] > op.getStateVector()[i]) {
					if (currentVector[i] - op.getStateVector()[i] > 1)
						return false;
					else
						poscount++;
				}
			}
		return (poscount <= 1);*/
		long host = op.getFromSite();
		int pos = id2pos.get(host); 		
		for (int i = 0; i < currentVector.length; i++)
			if (i == pos) 
			{
				if (currentVector[i]  + 1 != op.getStateVector()[i])
					return false;
			}
			else
			{
				if (currentVector[i] < op.getStateVector()[i])
					return false;
			}
			return true;			
	}

	/**
	 * Checks if the passed operation is concurrent to some operation already in
	 * the history.
	 * 
	 * @param op
	 *            the operation
	 * @return true is the operation is concurrent to some other operation in
	 *         the history.
	 */
	public boolean isConcurrent(OpWrapper op) {
		/*
		 * TODO Optimize a lot, this is really far from good.
		 */
		
		for(OpWrapper h : history){
			if(testConcurrent(h,op)) return true;
		}

		return false;
	}

	private boolean testConcurrent(OpWrapper o1, OpWrapper o2) {
		int count = 0;
		long[] v1 = o1.getStateVector();
		long[] v2 = o2.getStateVector();
		for(int i = 0;i<v1.length;i++){
			count+=(v1[i]==v2[i]?0:1);
		}
		return count>1;
	}

	public void appendToHistory(OpWrapper op) throws CCException {
		if (!isCausallyReady(op))
			throw new CCException("You tried to put an opration into "
					+ "the history that was not causally ready.");

		history.add(op);
	}
	
	public ArrayList<OpWrapper> getConcurrentOps (OpWrapper op)
	{
		ArrayList<OpWrapper> result = new ArrayList<OpWrapper> ();
		for(OpWrapper h : history)
			if(testConcurrent(h,op)) result.add(h);
		return result;
	}
	
	public ArrayList<OpWrapper> getOps ()
	{
		return (ArrayList<OpWrapper> )history.clone();
	}
	
	public void setOps (ArrayList<OpWrapper> h)
	{
		history = (ArrayList<OpWrapper>) h.clone();
		for (int i = 0; i < currentVector.length; i++)
				currentVector[i] = 0;
		
		for (OpWrapper ow : history)
		{
			long host = ow.getFromSite();
			int pos = id2pos.get(host); 
			if (currentVector[pos]< ow.getStateVector()[pos])
				currentVector[pos] = ow.getStateVector()[pos];					
				ow.getOperation().clean();
		}
			
	}

}
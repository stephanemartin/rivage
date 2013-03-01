package fr.inria.rivage.elements;

import java.util.HashMap;
import org.apache.log4j.Logger;



/**
 * This class allows to copy a graphical object and keeping all
 * snappoint-relationships in it.
 * @author Tobias Kuhn
 */
public class Cloner {
	
	private static Logger log = Logger.getLogger(Cloner.class);
	
	private HashMap<SnapManager, SnapManager> snapManagerMap;
	private HashMap<GSnapPoint, GSnapPoint> snapPointMap;
	
	private Cloner() {
		snapManagerMap = new HashMap<SnapManager, SnapManager>();
		snapPointMap = new HashMap<GSnapPoint, GSnapPoint>();
	}
	
	/**
	 * Makes a copy of the graphical object. All snappoint-
	 * relationships are kept.
	 * @param original the object to copy
	 * @return the copied object
	 */
	public static GObjectShape clone(GObject original) throws CloneNotSupportedException {
		Cloner cloner = new Cloner();
		GObjectShape clone = cloner.cloneObject(original);
		cloner.setAllSnapPoints();
		return clone;
	}
	
	private GObjectShape cloneObject(GObject objOrig) throws CloneNotSupportedException {
		/*if (objOrig instanceof GGroup) {
			GGroup groupOrig = (GGroup) objOrig;			
			GGroup groupCopy = new GGroup();
			groupCopy.setId(groupOrig.getId());
			for (GObjectShape childOrig : groupOrig.getChildren()) {
				GObjectShape childCopy = cloneObject(childOrig);
				groupCopy.addChild(childCopy);
				//childCopy.setParent(groupCopy);
			}
			return groupCopy;
		} else {
			GObjectShape objCopy = (GObjectShape) objOrig.clone();
			if (objCopy instanceof ISnappable) {
				snapManagerMap.put(
						((ISnappable) objOrig).getSnapManager(),
						((ISnappable) objCopy).getSnapManager());
			}
			if (objCopy instanceof ISnapper) {
				ArrayList<GSnapPoint> spO = ((ISnapper) objOrig).getSnapPoints();
				ArrayList<GSnapPoint> spC = ((ISnapper) objCopy).getSnapPoints();
				for (int i = 0; i < spO.size(); i++) {
					snapPointMap.put(spO.get(i), spC.get(i));
				}
			}
			return objCopy;
		}*/
            throw new UnsupportedOperationException("Not yet");
	}
	
	private void setAllSnapPoints() {
		for (SnapManager smOrig : snapManagerMap.keySet()) {
			SnapManager smCopy = snapManagerMap.get(smOrig);
			
			for (GSnapPoint spOrig : smOrig.getSnapPoints()) {
				GSnapPoint spCopy = snapPointMap.get(spOrig);
				
				if (spCopy == null) continue;
				
				PointDouble relPos = spOrig.getRelPos();
				smCopy.snap(spCopy, relPos.x, relPos.y);
			}
		}
	}

}

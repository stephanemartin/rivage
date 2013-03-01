package fr.inria.rivage.gui.listener;


public interface LayerChangeListener {
	
	public static final int COLOR_CHANGED = 0;
	public static final int VISIBILITY_CHANGED = 1;
	public static final int NEW_LAYER = 2;
	public static final int LAYER_REMOVED = 3;
	public static final int LAYER_MOVED = 4;
		
        
        public enum Type {COLOR_CHANGED ,VISIBILITY_CHANGED ,NEW_LAYER ,LAYER_REMOVED,LAYER_MOVED};
	public void layerChanged(Type type);

}

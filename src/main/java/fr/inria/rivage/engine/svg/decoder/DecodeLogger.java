package fr.inria.rivage.engine.svg.decoder;

import java.util.HashMap;

/**
 * Logs the warnings while decoding. Since some warnings may occur very
 * frequently, identical warnings get grouped.
 * @author Tobias Kuhn
 */
public class DecodeLogger {
	
	// stores the warning texts (keys) and the occurence counts (values)
	private HashMap<String, Integer> warnings = new HashMap<String, Integer>();
	
	/**
	 * Creates a new logger.
	 */
	public DecodeLogger() {}
	
	/**
	 * Stores the warning.
	 * @param message the warning text
	 */
	public void putWarning(String message) {
		if (warnings.containsKey(message)) {
			warnings.put(message, warnings.get(message) + 1);
		} else {
			warnings.put(message, 1);
		}
	}
	
	/**
	 * Returns all the warnings. (Identical warnings are grouped.)
	 * @return the warnings
	 */
	public String getWarnings() {
		String ms = "";
		for (String m : warnings.keySet()) {
			ms += (m + " (" + warnings.get(m) + "x)\n");
		}
		return ms;
	}

}

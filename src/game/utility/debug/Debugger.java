package game.utility.debug;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The <code>Debugger</code> class is used to as a control panel
 * where you can check which debug functions are enabled or which not.
 * 
 * @author Daniel Pellanda
 */
public class Debugger {
	
	/**
	 * Defines the default font for debugging features.
	 */
	public static final Font debugFont = new Font("Calibri", Font.PLAIN, 10);
	
	/**
	 * A map of flags that tells whether a certain debug function (specified as a key)
	 * is enabled (<code>true</code> value) or not (<code>false</code> value).
	 */
	private final Map<String,Boolean> options = new HashMap<>();
	/**
	 * A supplier that gets the number of FPS the game is currently running with.
	 */
	private final Supplier<Integer> fpsGetter;
	
	/**
	 * The main flag that decides if activate or deactivate the debug functions.
	 * <p>
	 * If this flag is <code>false</code>, the game won't use any debug function currently enabled.
	 * </p>
	 */
	private boolean debugMode = false;
	
	/**
	 * Constructor that initiate the <code>Debugger</code>.
	 * 
	 * @param mode the starting debug mode
	 * @param fpsGetter a supplier that gets the current FPS
	 */
	public Debugger(final boolean mode, final Supplier<Integer> fpsGetter) {
		this.debugMode = mode;
		this.fpsGetter = fpsGetter;
		
		options.put("log: fps", false);
		options.put("log: entities cleaner check", false);
		options.put("fps meter", true);
		options.put("entity coordinates", true);
	}

	/**
	 * Changes the current debug mode.
	 * 
	 * @param mode <code>true</code> for enable, <code>false</code> for disable
	 */
	public void setDebugMode(final boolean mode) {
		this.debugMode = mode;
	}
	
	/**
	 * @return <code>true</code> if debug mode is enabled, <code>false</code> if not
	 */
	public boolean isDebugModeOn() {
		return this.debugMode;
	}
	
	/**
	 * @return the number of FPS the game is currently running with
	 */
	public int fps() {
		return fpsGetter.get();
	}
	
	/**
	 * @param feature the debug function to check
	 * @return <code>true</code> if specified debug function is currently enabled, 
	 * 		   <code>false</code> if either the singular debug function or the whole debugger is disabled
	 */
	public boolean isFeatureEnabled(final String feature) {
		if(this.debugMode && options.containsKey(feature)) {
			return options.get(feature);
		}
		return false;
	}
	
}

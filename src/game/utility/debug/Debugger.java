package game.utility.debug;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Debugger {
	
	private final Map<String,Boolean> options = new HashMap<>();
	private final Supplier<Integer> fpsGetter;
	
	private boolean debugMode = false;
	
	public Debugger(final boolean mode, final Supplier<Integer> fpsGetter) {
		this.debugMode = mode;
		this.fpsGetter = fpsGetter;
		
		options.put("log: fps", false);
		options.put("log: entities cleaner check", false);
		options.put("log: entities cleaner working", false);
		options.put("fps meter", true);
		options.put("entity coordinates", true);
	}
	
	public int fps() {
		return fpsGetter.get();
	}
	
	public void setDebugMode(final boolean mode) {
		this.debugMode = mode;
	}
	
	public boolean isDebugModeOn() {
		return this.debugMode;
	}
	
	public boolean isFeatureEnabled(final String feature) {
		if(this.debugMode && options.containsKey(feature)) {
			return options.get(feature);
		}
		return false;
	}
	
}

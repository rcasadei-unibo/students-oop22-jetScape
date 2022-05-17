package game.utility.debug;

import java.awt.Font;
import java.awt.Color;

import java.util.HashMap;
import java.util.Map;

import game.frame.GameWindow;

/**
 * The <code>Debugger</code> class is used to as a control panel
 * where you can check which debug functions are enabled or which not.
 * 
 * @author Daniel Pellanda
 */
public class Debugger {
    /**
     * debugger font scale 
     */
    static final double fontScale = 52.3;
    /**
     * Defines the default font for debugging features.
     */
    public static final Font debugFont = GameWindow.fLoader.getDebuggerFont().deriveFont((float)(GameWindow.gameScreen.getHeight()/fontScale));
    /**
     * Defines the default color for debugging features.
     */
    public static final Color debugColor = Color.white;
    
    public enum Option{ FPS_METER, DRAW_TIME, ENTITY_COORDINATES, NEXT_SPAWN_TIMER,
        DIFFICULTY_LEVEL, HITBOX, LOG_FPS, LOG_CLEAN, LOG_SPAWN, LOG_HITCHECK};
    
    /**
     * A map of flags that tells whether a certain debug function (specified as a key)
     * is enabled (<code>true</code> value) or not (<code>false</code> value).
     */
    private final Map<Option,Boolean> optionEnabled = new HashMap<>();
    
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
     */
    public Debugger(final boolean mode) {
        this.debugMode = mode;
        
        optionEnabled.put(Option.FPS_METER, true);
        optionEnabled.put(Option.DRAW_TIME, true);
        optionEnabled.put(Option.ENTITY_COORDINATES, true);
        optionEnabled.put(Option.NEXT_SPAWN_TIMER, true);
        optionEnabled.put(Option.DIFFICULTY_LEVEL, true);
        optionEnabled.put(Option.HITBOX, false);
        optionEnabled.put(Option.LOG_CLEAN, false);
        optionEnabled.put(Option.LOG_SPAWN, false);
        optionEnabled.put(Option.LOG_HITCHECK, false);
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
     * @param feature the debug function to check
     * @return <code>true</code> if specified debug function is currently enabled, 
     *            <code>false</code> if either the singular debug function or the whole debugger is disabled
     */
    public boolean isFeatureEnabled(final Option feature) {
        return optionEnabled.get(feature) && this.debugMode;
    }
    
    public void printLog(final Option feature, final String message) {
        if(this.isFeatureEnabled(feature)) {
            System.out.println(message);
        }
    }
}

package game.logics.handler;

import java.awt.Graphics2D;

import game.utility.debug.Debugger;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Screen;

/**
 * The <code>Logics</code> interface is used for accessing <code>LogicsHandler</code> methods.
 * 
 * <p>
 * The <code>LogicsHandler</code> class helps <class>GameWindow</class> to update
 * and draw logical parts of the game like the Interface, Entities, Collisions, etc....
 * </p>
 * 
 * @author Daniel Pellanda
 */
public interface Logics {
	
	/**
	 * @return the screen information used by the handler
	 */
	Screen getScreenInfo();
	
	/**
	 * @return the keyboard listener used by the handler
	 */
	KeyHandler getKeyHandler();
	
	/**
	 * @return the debugger used by the handler
	 */
	Debugger getDebugger();
	
	/**
	 * Updates all the logical objects handled for a frame.
	 */
	void updateAll();
	
	/**
	 * Draws all visible and drawable object handled for a frame.
	 * @param g the graphics drawer
	 */
	void drawAll(Graphics2D g);
}

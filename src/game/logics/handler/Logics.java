package game.logics.handler;

import java.awt.Graphics2D;

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
	 * Updates all the logical objects handled for a frame.
	 */
	void updateAll();
	
	/**
	 * Draws all visible and drawable object handled for a frame.
	 * @param g the graphics drawer
	 */
	void drawAll(Graphics2D g);
}

package game.logics.handler;

import java.awt.Graphics2D;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import game.logics.entities.generic.Entity;
import game.utility.other.EntityType;

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
	
	static int getDifficultyLevel() {
		return LogicsHandler.difficultyLevel;
	}
	
	static int getFrameTime() {
		return LogicsHandler.frameTime;
	}
	
	BiConsumer<Predicate<EntityType>,Predicate<Entity>> getEntitiesCleaner();
	
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

package game.logics.entities.generic;

import java.awt.Graphics2D;

import game.utility.other.Pair;

/**
 * The <code>Entity</code> interface is used for accessing <code>EntityInstance</code> methods.
 * 
 * The abstract class <code>EntityInstance</code> is used to define all the common parts of each entity
 * like their position, entity relationship, visibility, on screen presence, etc...
 * 
 * @author Daniel Pellanda
 */
public interface Entity {
	
	/**
	 * Defines the top Y position the entity can go by moving.
	 */
	static final double yTopLimit = 0.0;
	/**
	 * Defines the bottom Y position the entity can go by moving.
	 */
	static final double yLowLimit = 64.0;
	
	/**
	 * @return <code>true</code> if the entity is visible, <code>false</code> if the entity is hidden
	 */
	boolean isVisible();
	/**
	 * @return <code>true</code> if the entity's position is between screen bounds, <code>false</code> if not
	 */
	boolean isOnScreenBounds();
	/**
	 * @return the entity's position
	 */
	Pair<Double,Double> getPosition();
	/**
	 * @return the X coordinate of the entity's position
	 */
	double getX();
	/**
	 * @return the Y coordinate of the entity's position
	 */
	double getY();
	/**
	 * @return a string representing the entity's category
	 */
	String entityType();
	/**
	 * Reset the current position of the obstacle, setting it to the starting one.
	 */
	void reset();
	/**
	 * Updates entity parameters (called for each frame).
	 */
	void update();
	/**
	 * Draws the entity if visible (called for each frame).
	 * @param g the graphics drawer
	 */
	void draw(Graphics2D g);
}

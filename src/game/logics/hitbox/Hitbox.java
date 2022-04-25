package game.logics.hitbox;

import java.awt.Rectangle;
import java.util.Set;
import java.awt.Graphics2D;

import game.utility.other.Pair;

public interface Hitbox {
	/**
	 * Translates all this entity's Rectangles the indicated distance,
	 * to the right along the X coordinate axis, and downward along the Y coordinate axis.
	 */
	public void updatePosition(Pair<Double,Double> newPos);
	
	/**
	 * @return true if at least one rectangle is intersecting with the target
	 */
	public boolean collides(Hitbox entity);
	
	/**
	 * @return all this entity's rectangles
	 */
	public Set<Rectangle> getRectangles();
	
	/**
	 * moves all the entity's rectangles to the initial position
	 */
	public void resetPosition();
	
	/**
	 * draws the hitboxes
	 */
	public void draw (Graphics2D g);
}

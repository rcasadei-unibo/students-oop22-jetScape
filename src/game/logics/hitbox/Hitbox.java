package game.logics.hitbox;

import java.awt.Rectangle;
import java.util.Set;

public interface Hitbox {
	/**
	 * Translates all this entity's Rectangles the indicated distance,
	 * to the right along the X coordinate axis, and downward along the Y coordinate axis.
	 */
	public void updatePosition(int xShift, int yShift);
	
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
}

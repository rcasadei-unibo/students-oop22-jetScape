package game.logics.hitbox;

import java.awt.Rectangle;
import java.util.Set;
import java.awt.Graphics2D;

import game.utility.other.Pair;

/**
 * The <code>Hitbox</code> interface is used for accessing <code>HitboxInstace</code> methods.
 * 
 * <p>
 * The <code>HitboxInstance</code> class represents a generic entity's group of hitboxes
 * </p>
 * 
 * @author Giacomo Amadio
 */
public interface Hitbox {
	/**
	 * Translates all this entity's Rectangles the indicated distance,
	 * to the right along the X coordinate axis, and downward along the Y coordinate axis.
	 */
	public void updatePosition(Pair<Double,Double> newPos);
	
	/**
	 * @return all this entity's rectangles
	 */
	public Set<Rectangle> getRectangles();
	
	/**
	 * draws the hitboxes
	 */
	public void draw (Graphics2D g);
}

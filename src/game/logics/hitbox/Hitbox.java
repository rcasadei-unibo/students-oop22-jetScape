package game.logics.hitbox;

import java.awt.Rectangle;
import java.util.Set;
import java.awt.Graphics2D;

import game.utility.other.Pair;

/**
 * The {@link Hitbox} interface is used for accessing {@link HitboxInstace} methods.
 * 
 * <p>
 * The {@link HitboxInstance} class represents a generic entity's group of hitboxes
 * </p>
 */
public interface Hitbox {
    /**
     * Translates all this entity's Rectangles the indicated distance,
     * to the right along the X coordinate axis, and downward along the Y coordinate axis.
     * 
     * @param newPos of the {@link Entity}
     */
    void updatePosition(Pair<Double, Double> newPos);

    /**
     * @return all this entity's rectangles
     */
    Set<Rectangle> getRectangles();

    /**
     * draws the hitbox.
     * 
     * @param g {@link Graphics2D} 
     */
    void draw(Graphics2D g);
}

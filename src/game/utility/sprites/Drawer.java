package game.utility.sprites;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.function.Supplier;

import game.utility.other.Pair;

/**
 * The {@link Drawer} interface can be used for accessing {@link DrawManager} methods.
 * 
 * The {@link DrawManager} class decides what sprites to draw
 * for representing an object.
 * 
 * If there aren't any sprites to draw, a place holder rectangle
 * of a specified color will be drawn.
 */
public interface Drawer {
    /**
     * The current key used to draw a place holder sprite.
     */
    String PLACEHOLDER_KEY = "placeholder";

    /**
     * Sets the color of the rectangle to draw if sprites are missing.
     * 
     * @param placeH the color for the placeholder
     */
    void setPlaceH(Color placeH);

    /**
     * Sets up the <code>animator</code> that decides what sprite to draw
     * at every moment.
     * 
     * @param animator a supplier that return a key of the sprite to draw
     */
    void setAnimator(Supplier<String> animator);

    /**
     * Creates a new {@link Sprite} object with a loaded image, specified 
     * in <code>path</code>, and adds it to the <code>sprites</code> map.
     * 
     * @param name the sprite name and key for finding it in the <code>sprites</code> map
     * @param path the path of the image to load in the {@link Sprite} object
     */
    void addSprite(String name, String path);

    /**
     * Adds an already created {@link Sprite} object to the <code>sprites</code> map.
     * 
     * @param s the {@link Sprite} object to add
     */
    void addLoadedSprite(Sprite s); 

    /**
     * Draws the specified sprite in the game environment.
     * 
     * @param g the graphics drawer
     * @param sprite the name of the sprite of draw
     * @param pos the position to draw the sprite
     * @param size the length of a side of the squared image
     */
    void drawSprite(Graphics2D g, String sprite, Pair<Double, Double> pos, int size);

    /**
     * Draws the currently chosen sprite in the game environment.
     * 
     * @param g the graphics drawer
     * @param pos the position to draw the sprite
     * @param size the length of a side of the squared image
     */
    void drawCurrentSprite(Graphics2D g, Pair<Double, Double> pos, int size);
}

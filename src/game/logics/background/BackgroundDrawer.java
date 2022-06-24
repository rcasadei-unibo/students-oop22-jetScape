package game.logics.background;

import java.awt.Graphics2D;
import java.awt.Color;

import game.utility.other.Pair;
import game.utility.sprites.Sprite;

/**
 * This interface can be used for accessing {@link BackgroundDrawManager}
 * methods.
 * 
 * The {@link BackgroundDrawManager} class is an implementation of
 * {@link Background} and handles the background control.
 * 
 * If there isn't the sprite to draw, a place holder rectangle
 * of a specified color will be drawn.
 */
public interface BackgroundDrawer {
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
     * @param height the height of the rectangle sprite
     * @param width the width of the rectangle sprite
     */
    void drawSprite(Graphics2D g, String sprite, Pair<Double, Double> pos, int height, int width);
}

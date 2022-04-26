package game.utility.sprites;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.function.Supplier;

import game.utility.other.Pair;

/**
 * The <code>Drawer</code> interface can be used for accessing <code>DrawManager</code> methods.
 * 
 * The <code>DrawManager</code> class decides what sprites to draw
 * for representing an object.
 * 
 * If there aren't any sprites to draw, a place holder rectangle
 * of a specified color will be drawn.
 * 
 * @author Daniel Pellanda
 */
public interface Drawer {
	
	static final String placeHKey = "placeholder";
	
	/**
	 * Sets the color of the rectangle to draw if sprites are missing.
	 * 
	 * @param placeH the color for the placeholder
	 */
	void setPlaceH(final Color placeH);
	
	/**
	 * Sets up the <code>animator</code> that decides what sprite to draw
	 * at every moment.
	 * 
	 * @param animator a supplier that return a key of the sprite to draw
	 */
	void setAnimator(Supplier<String> animator);
	
	/**
	 * Creates a new <code>Sprite</code> object with a loaded image, specified 
	 * in <code>path</code>, and adds it to the <code>sprites</code> map.
	 * 
	 * @param name the sprite name and key for finding it in the <code>sprites</code> map
	 * @param path the path of the image to load in the <code>Sprite</code> object
	 */
	void addSprite(String name, String path);
	
	/**
	 * Adds an already created <code>Sprite</code> object to the <code>sprites</code> map
	 * 
	 * @param s the <code>Sprite</code> object to add
	 */
	void addLoadedSprite(Sprite s); 
	
	/**
	 * Draws the specified sprite in the game environment.
	 * 
	 * @param g the graphics drawer
	 * @param pos the position to draw the sprite
	 * @param size the length of a side of the squared image
	 */
	void drawSprite(Graphics2D g, String sprite, Pair<Double,Double> pos, int size);
	
	/**
	 * Draws the currently chosen sprite in the game environment.
	 * 
	 * @param g the graphics drawer
	 * @param pos the position to draw the sprite
	 * @param size the length of a side of the squared image
	 */
	void drawCurrentSprite(Graphics2D g, Pair<Double,Double> pos, int size);
}

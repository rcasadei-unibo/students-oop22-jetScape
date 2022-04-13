package game.utility.textures;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.function.Supplier;

import game.utility.other.Pair;

/**
 * The <code>Drawer</code> interface can be used for accessing <code>DrawManager</code> methods.
 * 
 * The <code>DrawManager</code> class decides what textures to draw
 * for representing an object.
 * 
 * If there aren't any textures to draw, a place holder rectangle
 * of a specified color will be drawn.
 * 
 * @author Daniel Pellanda
 */
public interface Drawer {
	
	static final String placeHKey = "placeholder";
	
	/**
	 * Sets the color of the rectangle to draw if textures are missing.
	 * 
	 * @param placeH the color for the placeholder
	 */
	void setPlaceH(final Color placeH);
	
	/**
	 * Sets up the <code>animator</code> that decides what texture to draw
	 * at every moment.
	 * 
	 * @param animator a supplier that return a key of the texture to draw
	 */
	void setAnimator(Supplier<String> animator);
	
	/**
	 * Creates a new <code>Texture</code> object with a loaded image, specified 
	 * in <code>path</code>, and adds it to the <code>textures</code> map.
	 * 
	 * @param name the texture name and key for finding it in the <code>textures</code> map
	 * @param path the path of the image to load in the <code>Texture</code> object
	 */
	void addTexture(String name, String path);
	
	/**
	 * Adds an already created <code>Texture</code> object to the <code>textures</code> map
	 * 
	 * @param t the <code>Texture</code> object to add
	 */
	void addLoadedTexture(Texture t); 
	
	/**
	 * Draws the currently chosen texture in the game environment.
	 * 
	 * @param g the graphics drawer
	 * @param pos the position to draw the texture
	 * @param size the length of a side of the squared image
	 */
	void drawTexture(Graphics2D g, Pair<Double,Double> pos, int size);
}

package game.utility.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import game.utility.other.Pair;

/**
 * The <code>DrawManager</code> class decides what textures to draw
 * for representing an object.
 * 
 * If there aren't any textures to draw, a place holder rectangle
 * of a specified color will be drawn.
 * 
 * @author Daniel Pellanda
 */
public class DrawManager implements Drawer{
	
	/**
	 * A map that stores all the textures an object can use to be represented in the environment.
	 */
	private final Map<String, Texture> textures = new HashMap<>();
	/**
	 * Sets up the <code>textureToDraw</code> string depending on which texture has to
	 * be drawn at the moment.
	 */
	private Optional<Supplier<String>> animator = Optional.empty();
	/**
	 * The color of the place holder rectangle.
	 */
	private Color defaultColor;
	
	/**
	 * A key for the <code>textures</code> map that decides 
	 * which texture needs to be drawn at the moment.
	 */
	private String textureToDraw = "placeholder";
	
	/**
	 * Constructor that sets up the <code>DrawManager</code>.
	 * 
	 * @param placeHolder the color of the place holder rectangle
	 */
	public DrawManager(final Color placeHolder) {
		this.defaultColor = placeHolder;
		textures.put("placeholder", new Texture("placeholder", defaultColor));
	}
	
	public void setAnimator(final Supplier<String> animator) {
		this.animator = Optional.of(animator);
	}
	
	public void addTexture(final String name, final String path) {
		textures.put(name, new Texture(name, defaultColor, path));
	}
	
	public void addLoadedTexture(final Texture t) {
		textures.put(t.getName(), t);
	}
	
	public void drawTexture(final Graphics2D g, final Pair<Double,Double> pos, final int size) {
		animator.ifPresent(a -> textureToDraw = a.get());
		if(textures.containsKey(textureToDraw)) {
			textures.get(textureToDraw).draw(g, pos, size);
		} else {
			textures.get("placeholder").draw(g, pos, size);
		}
	}
}

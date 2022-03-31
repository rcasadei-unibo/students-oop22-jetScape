package game.utility.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import game.utility.other.Pair;

public class DrawManager implements Drawer{
	
	private final Map<String, Texture> textures = new HashMap<>();
	private Optional<Supplier<String>> animator = Optional.empty();
	private Color defaultColor;
	
	private String textureToDraw = "placeholder";
	
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
	
	public void addLoadedTexture(final String name, final Texture t) {
		textures.put(name, t);
	}
	
	public void drawTexture(final Graphics2D g, final Pair<Double,Double> pos, final int size) {
		animator.ifPresent(a -> textureToDraw = a.get());
		if(textures.containsKey(textureToDraw)) {
			textures.get(textureToDraw).draw(g, pos, size);
		}
	}
}

package game.utility.textures;

import java.awt.Graphics2D;
import java.util.function.Supplier;

import game.utility.other.Pair;

public interface Drawer {
	
	void setAnimator(Supplier<String> animator);
	
	void addTexture(String name, String path);
	
	void addLoadedTexture(String name, Texture t); 
	
	void drawTexture(Graphics2D g, Pair<Double,Double> pos, int size);
}

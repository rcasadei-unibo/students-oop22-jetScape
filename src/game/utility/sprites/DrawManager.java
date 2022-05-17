package game.utility.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import game.utility.other.Pair;

/**
 * The <code>DrawManager</code> class decides what sprites to draw
 * for representing an object.
 * 
 * If there aren't any textures to draw, a place holder rectangle
 * of a specified color will be drawn.
 * 
 * @author Daniel Pellanda
 */
public class DrawManager implements Drawer{
    
    /**
     * A map that stores all the sprites an object can use to be represented in the environment.
     */
    private final Map<String, Sprite> sprites = new HashMap<>();
    /**
     * Sets up the <code>spriteToDraw</code> string depending on which sprite has to
     * be drawn at the moment.
     */
    private Optional<Supplier<String>> animator = Optional.empty();
    /**
     * The color of the place holder rectangle.
     */
    private Color defaultColor;
    
    /**
     * A key for the <code>sprites</code> map that decides 
     * which texture needs to be drawn at the moment.
     */
    private String spriteToDraw = placeHKey;
    
    /**
     * Constructor that sets up the <code>DrawManager</code>.
     * 
     * @param placeHolder the color of the place holder rectangle
     */
    public DrawManager() {}
    
    public void setPlaceH(final Color placeHolder) {
        this.defaultColor = placeHolder;
        sprites.put(placeHKey, new Sprite(placeHKey, defaultColor));
    }
    
    public void setAnimator(final Supplier<String> animator) {
        this.animator = Optional.of(animator);
    }
    
    public void addSprite(final String name, final String path) {
        sprites.put(name, new Sprite(name, defaultColor, path));
    }
    
    public void addLoadedSprite(final Sprite t) {
        sprites.put(t.getName(), t);
    }
    
    private void draw(final Graphics2D g, final String sprite, final Pair<Double,Double> pos, final int size) {
        if(sprites.containsKey(sprite)) {
            sprites.get(sprite).draw(g, pos, size);
        } else if(sprites.containsKey(placeHKey)){
            sprites.get(placeHKey).draw(g, pos, size);
        }
    }
    
    public void drawSprite(final Graphics2D g, final String sprite, final Pair<Double,Double> pos, final int size) {
        this.draw(g, sprite, pos, size);
    }
    
    public void drawCurrentSprite(final Graphics2D g, final Pair<Double,Double> pos, final int size) {
        animator.ifPresent(a -> spriteToDraw = a.get());
        this.draw(g, spriteToDraw, pos, size);
    }
}

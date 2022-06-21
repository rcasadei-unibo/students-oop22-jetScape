package game.utility.sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import game.utility.other.Pair;

/**
 * The {@link DrawManager} class decides what sprites to draw
 * for representing an object.
 * 
 * If there aren't any textures to draw, a place holder rectangle
 * of a specified color will be drawn.
 */
public class DrawManager implements Drawer {

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
    private String spriteToDraw = PLACEHOLDER_KEY;

    /**
     * {@inheritDoc}
     */
    public void setPlaceH(final Color placeHolder) {
        this.defaultColor = placeHolder;
        this.sprites.put(PLACEHOLDER_KEY, new EntitySprite(PLACEHOLDER_KEY, defaultColor));
    }
    /**
     * {@inheritDoc}
     */
    public void setAnimator(final Supplier<String> animator) {
        this.animator = Optional.of(animator);
    }
    /**
     * {@inheritDoc}
     */
    public void addSprite(final String name, final String path) {
        this.sprites.put(name, new EntitySprite(name, defaultColor, path));
    }
    /**
     * {@inheritDoc}
     */
    public void addLoadedSprite(final Sprite t) {
        this.sprites.put(t.getName(), t);
    }

    private void draw(final Graphics2D g, final String sprite, final Pair<Double, Double> pos, final int size) {
        if (this.sprites.containsKey(sprite)) {
            this.sprites.get(sprite).draw(g, pos, size);
        } else if (this.sprites.containsKey(PLACEHOLDER_KEY)) {
            this.sprites.get(PLACEHOLDER_KEY).draw(g, pos, size);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void drawSprite(final Graphics2D g, final String sprite, final Pair<Double, Double> pos, final int size) {
        this.draw(g, sprite, pos, size);
    }

    /**
     * {@inheritDoc}
     */
    public void drawCurrentSprite(final Graphics2D g, final Pair<Double, Double> pos, final int size) {
        this.animator.ifPresent(a -> this.spriteToDraw = a.get());
        this.draw(g, this.spriteToDraw, pos, size);
    }
}

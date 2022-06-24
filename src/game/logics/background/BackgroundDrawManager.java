package game.logics.background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import game.utility.other.Pair;
import game.utility.sprites.Sprite;

/**
 * This class choose what sprites to draw for representing the
 * background and handles the store task.
 * 
 * If there aren't any textures to draw, a place holder rectangle
 * of a specified color will be drawn.
 */
public class BackgroundDrawManager implements BackgroundDrawer {

    /**
     * A map that stores all the background sprites that could be represented in
     * the environment.
     */
    private final Map<String, Sprite> sprites = new HashMap<>();

    /**
     * The color of the place holder rectangle.
     */
    private Color defaultColor;

    /**
     * {@inheritDoc}
     */
    public void setPlaceH(final Color placeHolder) {
        this.defaultColor = placeHolder;
        this.sprites.put(PLACEHOLDER_KEY, new BackgroundSprite(PLACEHOLDER_KEY, defaultColor));
    }

    /**
     * {@inheritDoc}
     */
    public void addSprite(final String name, final String path) {
        this.sprites.put(name, new BackgroundSprite(name, defaultColor, path));
    }

    /**
     * {@inheritDoc}
     */
    public void addLoadedSprite(final Sprite t) {
        this.sprites.put(t.getName(), t);
    }

    private void draw(final Graphics2D g, final String sprite, final Pair<Double, Double> pos, final int height, final int width) {
        if (this.sprites.containsKey(sprite)) {
            this.sprites.get(sprite).draw(g, pos, height, width);
        } else if (this.sprites.containsKey(PLACEHOLDER_KEY)) {
            this.sprites.get(PLACEHOLDER_KEY).draw(g, pos, height, width);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void drawSprite(final Graphics2D g, final String sprite, final Pair<Double, Double> pos, final int height, final int width) {
        this.draw(g, sprite, pos, height, width);
    }
}

package game.utility.sprites;

import java.awt.Graphics2D;

import game.utility.other.Pair;

/**
 * This interface defines the behaviour that a Sprite handler has to maintain.
 */
public interface Sprite {

    /**
     * @return the sprite name identifier
     */
    String getName();

    /**
     * Draws the loaded image or a square place holder if the
     * image hasn't been loaded.
     * 
     * @param g the graphics drawer
     * @param pos the position to draw the image
     * @param sizes the length of a side of the squared image or length & width
     *   of the rectangle image
     */
    void draw(Graphics2D g, Pair<Double, Double> pos, int... sizes);
}

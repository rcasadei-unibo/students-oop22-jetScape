package game.logics.background;

import java.awt.Graphics2D;

/**
 * This interface models a Background controller.
 *
 */
public interface Background {

    /**
     * Reset the background parameters.
     */
    void reset();

    /**
     * Updates background parameters (called for each frame).
     */
    void update();

    /**
     * Draws the background images or the rectangle place holder if the
     * images haven't been loaded.
     * 
     * @param g the graphics drawer
     */
    void draw(Graphics2D g);

    /**
     * Draws the coordinates of the background if visible.
     * 
     * @param g the graphics drawer
     */
    void drawCoordinates(Graphics2D g);

}

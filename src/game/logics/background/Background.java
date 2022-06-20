package game.logics.background;

import java.awt.Graphics2D;

/**
 * This interface models a Background controller.
 *
 */
public interface Background {

    /**
     * Updates background parameters (called for each frame).
     * 
     * @param g the graphics drawer
     */
    void update(Graphics2D g);

    /**
     * Draws the coordinates of the background if visible.
     * 
     * @param g the graphics drawer
     */
    void drawCoordinates(Graphics2D g);
}

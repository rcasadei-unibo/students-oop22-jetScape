package game.logics.background;

import java.awt.Graphics2D;

/**
 * This interface models a Background controller.
 *
 */
public interface Background {

    /**
     * Allows to set the background visibility.
     *
     * @param v <code>true</code> if the background has to be shown,
     * <code>false</code> if it has to be hidden
     */
    void setVisibility(boolean v);

    /**
     * @return <code>true</code> if the background is visible,
     * <code>false</code> if the background is hidden
     */
    boolean isVisible();

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
     * images haven't been loaded, but only if they are visible.
     * 
     * @param g the graphics drawer
     */
    void draw(Graphics2D g);

    /**
     * Draws the coordinates of the backgrounds if they are visible.
     * 
     * @param g the graphics drawer
     */
    void drawCoordinates(Graphics2D g);

}

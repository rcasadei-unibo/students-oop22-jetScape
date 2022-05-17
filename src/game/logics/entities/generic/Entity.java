package game.logics.entities.generic;

import java.awt.Graphics2D;
import java.util.Set;

import game.frame.GameWindow;
import game.logics.hitbox.Hitbox;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The <code>Entity</code> interface is used for accessing <code>EntityInstance</code> methods.
 * 
 * The abstract class <code>EntityInstance</code> is used to define all the common parts of each entity
 * like their position, entity relationship, visibility, on screen presence, etc...
 */
public interface Entity {
    /**
     * An utility variable used to correct the higher draw bound of the entity.
     */
    double Y_TOP_LIMIT = 0;
    /**
     * An utility variable used to correct the lower draw bound of the entity.
     */
    double Y_LOW_LIMIT = GameWindow.GAME_SCREEN.getHeight() - (GameWindow.GAME_SCREEN.getTileSize() * 2);


    /**
     * @return <code>true</code> if the entity is visible, <code>false</code> if the entity is hidden
     */
    boolean isVisible();
    /**
     * @return <code>true</code> if the entity's position is between screen bounds, <code>false</code> if not
     */
    boolean isOnScreenBounds();
    /**
     * @return <code>true</code> if the obstacle's position is on the "clear area", <code>false</code> if not
     */
    boolean isOnClearArea();
    /**
     * @return <code>true</code> if the obstacle's position is on the "spawn area", <code>false</code> if not
     */
    boolean isOnSpawnArea();
    /**
     * @return the entity's position
     */
    Pair<Double, Double> getPosition();
    /**
     * @return A <code>{@link Hitbox}</code> set representing all the <code>{@link Hitbox}</code> used by the entity
     */
    Set<Hitbox> getHitboxSet();
    /**
     * @return a string representing the entity's category
     */
    EntityType entityType();

    /**
     * Reset the parameters of the entity.
     */
    void reset();
    /**
     * Reset the parameters and removes from the entities map.
     */
    void clean();
    /**
     * Updates entity parameters (called for each frame).
     */
    void update();
    /**
     * Draws the entity if visible (called for each frame).
     * @param g the graphics drawer
     */
    void draw(Graphics2D g);
    /**
     * Draws the coordinates of the entity if visible.
     * 
     * @param g the graphics drawer
     */
    void drawCoordinates(Graphics2D g);
}

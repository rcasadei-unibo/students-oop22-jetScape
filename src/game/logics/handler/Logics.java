package game.logics.handler;

import game.logics.entities.generic.Entity;
import game.utility.other.EntityType;
import java.awt.Graphics2D;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * The {@link Logics} interface is used for accessing
 * {@link LogicsHandler} methods.
 *
 * <p>
 * The {@link LogicsHandler} class helps {@link GameWindow} to update and draw
 * logical parts of the game like the Interface, Entities, Collisions, etc....
 * </p>
 *
 */
public interface Logics {

    /**
     * @return a {@link BiConsumer} that can be used to clean up current active entities in the environment
     */
    BiConsumer<Predicate<EntityType>, Predicate<Entity>> getEntitiesCleaner();

    /**
     * Updates all the logical objects handled for a frame.
     */
    void updateAll();

    /**
     * Draws all visible and drawable object handled for a frame.
     *
     * @param g the graphics drawer
     */
    void drawAll(Graphics2D g);
}

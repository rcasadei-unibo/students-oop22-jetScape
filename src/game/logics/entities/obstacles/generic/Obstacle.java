package game.logics.entities.obstacles.generic;

import game.logics.entities.generic.Entity;
import game.logics.interactions.SpeedHandler;

/**
 * An Interface for accessing {@link ObstacleInstance} methods.
 * 
 * <p>
 * The abstract class {@link ObstacleInstance} is used to define the common parts of each obstacle.
 * </p>
 */
public interface Obstacle extends Entity {

    /**
     * @return the movement information of the obstacle
     */
    SpeedHandler getSpeedHandler();

}

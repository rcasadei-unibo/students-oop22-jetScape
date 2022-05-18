package game.logics.entities.obstacles.missile;

import game.logics.entities.obstacles.generic.Obstacle;

/**
 * An Interface for accessing {@link MissileInstance} methods.
 * 
 * <p>
 * The class {@link MissileInstance} is used for defining a missile obstacle in the environment.
 * 
 * A missile is a fast projective that can damage the player if hit, when a missile is about to appear
 * a warning icon will alert you from which direction the missile is coming from.
 * </p>
 */
public interface Missile extends Obstacle {

}

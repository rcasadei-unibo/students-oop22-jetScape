package game.logics.entities.pickups.teleport;

import game.logics.entities.pickups.generic.Pickup;

/**
 * An Interface for accessing {@link TeleportInstance} methods.
 * 
 * <p>
 * The class {@link TeleportInstance} is used for defining a teleport pickup in the environment.
 * 
 * A teleport is a bonus item that can be picked up by the player granting a slight score
 * increase + the removal of all the current entities on the screen.
 * </p>
 */
public interface Teleport extends Pickup {

}

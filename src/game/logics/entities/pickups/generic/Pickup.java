package game.logics.entities.pickups.generic;

import game.logics.entities.generic.Entity;
import game.logics.entities.player.Player;
import game.logics.interactions.SpeedHandler;

/**
 * An Interface for accessing {@link PickupInstance} methods.
 * 
 * <p>
 * The abstract class {@link PickupInstance} is used to define the common parts of each pickup bonus.
 * </p>
 */
public interface Pickup extends Entity {
    /**
     * @return the movement information of the pickup
     */
    SpeedHandler getSpeedHandler();

    /**
     * @return a reference to the current {@link Player} entity used
     */
    Player getPlayerPointer();
}

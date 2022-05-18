package game.logics.entities.player;

import game.logics.entities.generic.Entity;

/**
 * The {@link Player} interface can be used for accessing {@link PlayerInstance} methods.
 * 
 * <p>
 * The {@link PlayerInstance} class represents the player's entity in
 * the game environment.
 * </p>
 */
public interface Player extends Entity {

    /**
     * @return <code>true</code> if the player has considered dead (after the death animation has finished), <code>false</code> if not
     */
    boolean hasDied();
    /**
     * @return the current player's score
     */
    int getCurrentScore();
}

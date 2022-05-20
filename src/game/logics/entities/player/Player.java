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
     * This method is used to know if the player has considered dead (after the death animation has finished), <code>false</code> if not.
     * @return <code>true</code> if the player has died, <code>false</code> otherwise.
     */
    boolean hasDied();

    /**
     * Return the actual value of the score, it doesn't matter if Barry is alive or not.
     *
     * @return the current player's score
     */
    int getCurrentScore();

    /**
     * This method is used to get the current cause of death, if any.
     *   If Barry is still alive, returns <code>Player.PlayerDeath.NONE</code>
     * @return a PlayerDeath instance with the cause of death
     */
    PlayerDeath getCauseOfDeath();

    /**
     * A enumerable describing the current status of the player.
     */
    enum PlayerStatus {
        WALK, LAND, FALL, JUMP, ZAPPED, BURNED, DEAD;

        public static boolean hasChanged = false;

        public boolean isInDyingAnimation() {
            switch (this) {
               case ZAPPED:
               case BURNED:
               case DEAD:
                   return true;
               default:
                   break;
            }
            return false;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    enum PlayerDeath {
        BURNED, ZAPPED, NONE;
    }
}

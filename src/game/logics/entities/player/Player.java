package game.logics.entities.player;

import game.logics.entities.generic.Entity;

/**
 * The <code>Player</code> interface can be used for accessing <code>PlayerInstance</code> methods.
 * 
 * The <code>PlayerInstance</code> class represents the player's entity in
 * the game environment.
 * 
 * @author Daniel Pellanda
 */
public interface Player extends Entity {

    static final double baseFallSpeed = 50.0;
    static final double baseJumpSpeed = 20.0;
    static final double initialJumpMultiplier = 1.0;
    static final double initialFallMultiplier = 0.3;
    static final double jumpMultiplierIncrease = 0.6;
    static final double fallMultiplierIncrease = 0.15;

    static final double xRelativePosition = 2.11;

    static final int flickeringSpeed = 10;
    static final int invicibilityTime = 2;

    static final double animationSpeed = 7;

    /**
     * Return <code>true</code> if the player has died, <code>false</code> otherwise.
     * 
     * @return if the player has died.
     */
    boolean hasDied();

    /**
     * Return the actual value of the score, it doesn't matter if Barry is alive or not.
     * @return current score
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

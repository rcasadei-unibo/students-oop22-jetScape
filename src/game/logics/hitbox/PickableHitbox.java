package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link PickableHitbox} class represents a 
 * {@link game.logics.entities.pickups.generic.Pickup Pickup} object's
 * {@link Hitbox}
 * the game environment.
 */
public class PickableHitbox extends HitboxInstance {

    /**
     * initializes a {@link game.logics.entities.pickups.generic.Pickup Pickup}
     * {@link Hitbox} in the given starting position.
     * @param startingPos the starting position
     */
    public PickableHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        this.addRectangle(0, 0, SPRITE_DIMENSIONS, SPRITE_DIMENSIONS);
    }
}


package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link PickableHitbox} class represents a pickable object's hitbox in
 * the game environment.
 */
public class PickableHitbox extends HitboxInstance {

    // TODO: Add javadoc
    /**
     * .
     * @param startingPos
     */
    public PickableHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        this.addRectangle(0, 0, SPRITE_DIMENSIONS, SPRITE_DIMENSIONS);
    }
}


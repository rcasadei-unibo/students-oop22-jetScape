package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The <code>PickableHitbox</code> class represents a pickable object's hitbox in
 * the game environment.
 * 
 * @author Giacomo Amadio
 */
public class PickableHitbox extends HitboxInstance {

    public PickableHitbox(Pair<Double, Double> startingPos) {
        super(startingPos);
        this.addRectangle(0, 0, spriteDimensions, spriteDimensions);
    }
}


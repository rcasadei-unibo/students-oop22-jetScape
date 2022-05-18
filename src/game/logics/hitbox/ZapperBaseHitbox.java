package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link ZapperBaseHitbox} class represents a ZapperBase's  hitbox in
 * the game environment.
 */
public class ZapperBaseHitbox extends HitboxInstance {
    static final double RECTANGLE_X = 5;
    static final double RECTANGLE_Y = 5;
    static final double RECTANGLE_W = 22;
    static final double RECTANGLE_H = 22;

    // TODO: add javadoc
    /**
     * .
     * @param startingPos
     */
    public ZapperBaseHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        addRectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_W, RECTANGLE_H);
    }
}


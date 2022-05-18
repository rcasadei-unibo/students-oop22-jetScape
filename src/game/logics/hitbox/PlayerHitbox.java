package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link PlayerHitbox} class represents a player's hitbox in
 * the game environment.
 */
public class PlayerHitbox extends HitboxInstance {
    static final double RECTANGLE_1X = 16;
    static final double RECTANGLE_1Y = 24;
    static final double RECTANGLE_1W = 8;
    static final double RECTANGLE_1H = 8;
    static final double RECTANGLE_2X = 16;
    static final double RECTANGLE_2Y = 3;
    static final double RECTANGLE_2W = 13;
    static final double RECTANGLE_2H = 21;

    // TODO: add javadoc
    /**
     * .
     * @param startingPos
     */
    public PlayerHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(RECTANGLE_1X, RECTANGLE_1Y, RECTANGLE_1W, RECTANGLE_1H);
        super.addRectangle(RECTANGLE_2X, RECTANGLE_2Y, RECTANGLE_2W, RECTANGLE_2H);
    }
}

package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@code CoinHitbox} class represents a single coin's hitbox in
 * the game environment.
 */
public class CoinHitbox extends HitboxInstance {
    static final double RECTANGLE_X = 8;
    static final double RECTANGLE_Y = 8;
    static final double RECTANGLE_W = 16;
    static final double RECTANGLE_H = 16;

    /**
     * initializes {@link game.logics.entities.pickups.coin.Coin Coin}
     * {@link Hitbox} in the starting position.
     *
     * @param startingPos the starting position
     */
    public CoinHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_W, RECTANGLE_H);
    }

}

package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link ZapperRayVerticalHitbox} class represents a missile 
 * {@link game.logics.entities.obstacles.zapper.ZapperRay ZapperRay}'s vertical hitbox in
 * the game environment.
 */
public class ZapperRayVerticalHitbox extends HitboxInstance {
    static final double RECTANGLE_X = 6;
    static final double RECTANGLE_Y = 0;
    static final double RECTANGLE_W = 20;
    static final double RECTANGLE_H = 32;

    /**
     * initializes {@link game.logics.entities.obstacles.zapper.ZapperRay ZapperRay} vertical
     * {@link Hitbox} in the starting position.
     * @param startingPos the starting position
     */
    public ZapperRayVerticalHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_W, RECTANGLE_H);
    }
}

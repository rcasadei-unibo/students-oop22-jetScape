package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link ZapperRayHorizontalHitbox} class represents a 
 * {@link game.logics.entities.obstacles.zapper.ZapperRay ZapperRay}'s horizontal hitbox in
 * the game environment.
 */
public class ZapperRayHorizontalHitbox extends HitboxInstance {
    static final double RECTANGLE_X = 0;
    static final double RECTANGLE_Y = 6;
    static final double RECTANGLE_W = 32;
    static final double RECTANGLE_H = 20;

    /**
     * initializes {@link game.logics.entities.obstacles.zapper.ZapperRay ZapperRay} horizontal
     * {@link Hitbox} in the starting position.
     * @param startingPos
     */
    public ZapperRayHorizontalHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_W, RECTANGLE_H);
    }
}

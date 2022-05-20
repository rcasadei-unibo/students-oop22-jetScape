package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The {@link MissileHitbox} class represents a missile's hitbox in
 * the game environment.
 */
public class MissileHitbox extends HitboxInstance {
    static final double RECTANGLE_X = 0;
    static final double RECTANGLE_Y = 13;
    static final double RECTANGLE_W = 30;
    static final double RECTANGLE_H = 5;

    /**
     * initializes {@link game.logics.entities.obstacles.missile.Missile Missile}
     * {@link Hitbox} in the starting position.
     * 
     * @param startingPos
     */
    public MissileHitbox(final Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_W, RECTANGLE_H);
    }
}

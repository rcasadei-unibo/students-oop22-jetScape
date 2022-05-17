package game.logics.hitbox;

import game.utility.other.Pair;

/**
 * The <code>ZapperRayVerticalHitbox</code> class represents a missile ZapperRayVertical's hitbox in
 * the game environment.
 * 
 * @author Giacomo Amadio
 */
public class ZapperRayVerticalHitbox extends HitboxInstance {
    static final double rectangleX = 6;
    static final double rectangleY = 0;
    static final double rectangleW = 20;
    static final double rectangleH = 32;

    public ZapperRayVerticalHitbox(Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(rectangleX, rectangleY, rectangleW, rectangleH);
    }
}

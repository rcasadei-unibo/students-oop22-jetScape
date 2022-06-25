package game.logics.hitbox;

import game.utility.other.Pair;

public class CoinHitbox extends HitboxInstance {
    static final double RECTANGLE_X = 8;
    static final double RECTANGLE_Y = 8;
    static final double RECTANGLE_W = 16;
    static final double RECTANGLE_H = 16;

    public CoinHitbox(Pair<Double, Double> startingPos) {
        super(startingPos);
        super.addRectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_W, RECTANGLE_H);
    }

}

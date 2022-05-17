package game.logics.hitbox;

import game.utility.other.Pair;

public class ZapperBaseHitbox extends HitboxInstance {
	static final double rectangleX = 5;
	static final double rectangleY = 5;
	static final double rectangleW = 22;
	static final double rectangleH = 22;

	public ZapperBaseHitbox(Pair<Double, Double> startingPos) {
		super(startingPos);
		addRectangle(rectangleX,rectangleY,rectangleW,rectangleH);
	}

}

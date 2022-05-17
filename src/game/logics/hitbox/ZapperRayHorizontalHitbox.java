package game.logics.hitbox;

import game.utility.other.Pair;
public class ZapperRayHorizontalHitbox extends HitboxInstance {
	static final double rectangleX = 0;
	static final double rectangleY = 6;
	static final double rectangleW = 32;
	static final double rectangleH = 20;

	public ZapperRayHorizontalHitbox(Pair<Double, Double> startingPos) {
		super(startingPos);
		super.addRectangle(rectangleX, rectangleY, rectangleW, rectangleH);
	}
}

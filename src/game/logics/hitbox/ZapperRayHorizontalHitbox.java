package game.logics.hitbox;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class ZapperRayHorizontalHitbox extends HitboxInstance {
	static final double rectangleX = 0;
	static final double rectangleY = 6/spriteDimensions;
	static final double rectangleW = 1;
	static final double rectangleH = 20/spriteDimensions;

	public ZapperRayHorizontalHitbox(Pair<Double, Double> startingPos, Screen gScreen) {
		super(startingPos, gScreen);
		super.addRectangle(rectangleX, rectangleY, rectangleW, rectangleH);
	}
}

package game.logics.hitbox;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class ZapperRayVerticalHitbox extends HitboxInstance {
	static final double rectangleX = 6/spriteDimensions;
	static final double rectangleY = 0;
	static final double rectangleW = 20/spriteDimensions;
	static final double rectangleH = 0;

	public ZapperRayVerticalHitbox(Pair<Double, Double> startingPos, Screen gScreen) {
		super(startingPos, gScreen);
		super.addRectangle(rectangleX, rectangleY, rectangleW, rectangleH);
	}

}

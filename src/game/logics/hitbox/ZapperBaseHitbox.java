package game.logics.hitbox;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class ZapperBaseHitbox extends HitboxInstance {
	static final double rectangleX = 5/spriteDimensions;
	static final double rectangleY = 5/spriteDimensions;
	static final double rectangleW = 22/spriteDimensions;
	static final double rectangleH = 22/spriteDimensions;

	public ZapperBaseHitbox(Pair<Double, Double> startingPos, Screen gScreen) {
		super(startingPos, gScreen);
		addRectangle(rectangleX,rectangleY,rectangleW,rectangleH);
	}

}

package game.logics.hitbox;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class MissileHitbox extends HitboxInstance{
	static final double rectangleX = 0;
	static final double rectangleY = 13;
	static final double rectangleW = 30;
	static final double rectangleH = 5;

	public MissileHitbox(Pair<Double, Double> startingPos, Screen gScreen) {
		super(startingPos, gScreen);
		super.addRectangle(rectangleX, rectangleY, rectangleW, rectangleH);
	}

}

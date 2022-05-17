package game.logics.hitbox;

import game.utility.other.Pair;

public class PlayerHitbox extends HitboxInstance {
	static final double rectangle1X = 16;
	static final double rectangle1Y = 24;
	static final double rectangle1W = 8;
	static final double rectangle1H = 8;
	static final double rectangle2X = 16;
	static final double rectangle2Y = 3;
	static final double rectangle2W = 13;
	static final double rectangle2H = 21;


	public PlayerHitbox(Pair<Double,Double> startingPos) {
		super(startingPos);
		super.addRectangle(rectangle1X, rectangle1Y, rectangle1W, rectangle1H);
		super.addRectangle(rectangle2X, rectangle2Y, rectangle2W, rectangle2H);
	}
	
}

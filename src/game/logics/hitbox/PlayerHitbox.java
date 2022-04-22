package game.logics.hitbox;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class PlayerHitbox extends HitboxInstance {
	static final double rectangle1X = 16/spriteDimensions;
	static final double rectangle1Y = 8/spriteDimensions;
	static final double rectangle1W = 8/spriteDimensions;
	static final double rectangle1H = 8/spriteDimensions;
	static final double rectangle2X = 16/spriteDimensions;
	static final double rectangle2Y = 3/spriteDimensions;
	static final double rectangle2W = 13/spriteDimensions;
	static final double rectangle2H = 21/spriteDimensions;


	public PlayerHitbox(Pair<Double,Double> startingPos, Screen gScreen) {
		super(startingPos, gScreen);
		super.addRectangle(rectangle1X, rectangle1Y, rectangle1W, rectangle1H);
		super.addRectangle(rectangle2X, rectangle2Y, rectangle2W, rectangle2H);
	}
	
}

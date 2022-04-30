package game.logics.hitbox;

import game.utility.other.Pair;
import game.utility.screen.Screen;

public class PickableHitbox extends HitboxInstance {

	public PickableHitbox(Pair<Double, Double> startingPos, Screen gScreen) {
		super(startingPos, gScreen);
		this.addRectangle(0, 0, spriteDimensions, spriteDimensions);
	}

}

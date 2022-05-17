package game.logics.hitbox;

import game.utility.other.Pair;

public class PickableHitbox extends HitboxInstance {

	public PickableHitbox(Pair<Double, Double> startingPos) {
		super(startingPos);
		this.addRectangle(0, 0, spriteDimensions, spriteDimensions);
	}

}

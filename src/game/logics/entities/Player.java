package game.logics.entities;

public interface Player extends Entity{

	static final double baseFallSpeed = 50.0;
	static final double baseJumpSpeed = 20.0;
	static final double initialJumpMultiplier = 1.0;
	static final double initialFallMultiplier = 0.3;
	static final double jumpMultiplierIncrease = 0.6;
	static final double fallMultiplierIncrease = 0.15;
	
	static final double xPosition = 135.0;
}

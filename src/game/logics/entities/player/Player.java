package game.logics.entities.player;

import game.logics.entities.generic.Entity;

/**
 * The <code>Player</code> interface can be used for accessing <code>PlayerInstance</code> methods.
 * 
 * The <code>PlayerInstance</code> class represents the player's entity in
 * the game environment.
 * 
 * @author Daniel Pellanda
 */
public interface Player extends Entity{

	enum PlayerAction{ 
		WALK, LAND, FALL, JUMP;
		public static boolean hasChanged = false;
		public static boolean isLanding = false;
		
		public void changeAction(final PlayerAction newAction) {
			if(this != newAction) {
				hasChanged = true;
				isLanding = newAction == PlayerAction.LAND;
			}
		}
		
		public String toString() {
			switch(this) {
				case LAND:
					return "land";
				case FALL:
					return "fall";
				case JUMP:
					return "jump";
				default:
					break;
			}
			return "walk";
		}
	}
	
	static final double baseFallSpeed = 50.0;
	static final double baseJumpSpeed = 20.0;
	static final double initialJumpMultiplier = 1.0;
	static final double initialFallMultiplier = 0.3;
	static final double jumpMultiplierIncrease = 0.6;
	static final double fallMultiplierIncrease = 0.15;
	
	static final double xRelativePosition = 2.11;
	
	int getCurrentScore();
}

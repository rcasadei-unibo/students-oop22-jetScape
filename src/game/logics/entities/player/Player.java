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
		WALK, LAND, FALL, JUMP, ZAPPED, BURNED, DEAD;
		public static boolean hasChanged = false;
		public static boolean landing = false;
		public static boolean dying = false;
		
		public void changeAction(final PlayerAction newAction) {
			if(this != newAction) {
				hasChanged = true;
				landing = newAction == PlayerAction.LAND;
				dying = newAction == PlayerAction.BURNED || newAction == PlayerAction.ZAPPED;
			}
		}
		
		public boolean isInDyingAnimation() {
			return this.ordinal() > 3;
		}
		
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
	static final double baseFallSpeed = 50.0;
	static final double baseJumpSpeed = 20.0;
	static final double initialJumpMultiplier = 1.0;
	static final double initialFallMultiplier = 0.3;
	static final double jumpMultiplierIncrease = 0.6;
	static final double fallMultiplierIncrease = 0.15;
	
	static final double xRelativePosition = 2.11;
	
	boolean hasDied();
	
	int getCurrentScore();
}

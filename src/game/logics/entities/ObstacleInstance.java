package game.logics.entities;

import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.screen.Pair;

public abstract class ObstacleInstance extends EntityInstance implements Obstacle{

	protected final Pair<Double,Double> startPos;
	protected SpeedHandler movement;
	
	ObstacleInstance(final Logics l, final Pair<Double,Double> sPosition){
		super(l, sPosition.clone());
		entityTag = "obstacle";
		startPos = sPosition;
	}
	
	public boolean isOutofScreen() {
		return !this.isOnScreenBounds();
	}
	
	public void resetPosition() {
		this.position.setX(startPos.getX());
		this.position.setY(startPos.getY());
	}
	
	public SpeedHandler getSpeedHandler() {
		return movement;
	}
}

package game.logics.entities.obstacles;

import game.logics.entities.basic.EntityInstance;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.Pair;

public abstract class ObstacleInstance extends EntityInstance implements Obstacle{

	protected final Pair<Double,Double> startPos;
	protected SpeedHandler movement;
	
	private boolean onClearArea = false;
	
	ObstacleInstance(final Logics l, final Pair<Double,Double> sPosition){
		super(l, sPosition.clone());
		entityTag = "obstacle";
		startPos = sPosition;
	}
	
	private void updateFlags() {
		if(position.getX() < -screen.getTileSize()) {
			onClearArea = true;
		} else {
			onClearArea = false;
		}
	}
	
	public boolean isOutofScreen() {
		return !this.isOnScreenBounds();
	}
	
	public boolean isOnClearArea() {
		return this.onClearArea;
	}
	
	public SpeedHandler getSpeedHandler() {
		return movement;
	}
	
	public void resetPosition() {
		this.position.setX(startPos.getX());
		this.position.setY(startPos.getY());
	}
	
	public void update() {
		super.update();
		updateFlags();
	}
}

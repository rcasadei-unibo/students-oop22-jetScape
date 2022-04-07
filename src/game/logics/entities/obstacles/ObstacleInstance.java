package game.logics.entities.obstacles;

import game.logics.entities.basic.EntityInstance;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.Pair;

/**
 * The abstract class <code>ObstacleInstance</code> is used to define the common parts of each obstacle.
 * 
 * @author Daniel Pellanda
 */
public abstract class ObstacleInstance extends EntityInstance implements Obstacle{

	/**
	 * Defines the starting position where the obstacle starts
	 */
	protected final Pair<Double,Double> startPos;
	/**
	 * Defines the movement parameters of the obstacle.
	 */
	protected SpeedHandler movement;
	
	/**
	 * A flag that automatically updates and tells if the entity is on the "clear area".
	 */
	private boolean onClearArea = false;
	
	/**
	 * Constructor that sets up obstacle default values (picked up from 
	 * <code>Logics</code>), defines it's bounds in the environment and allows to set it's
	 * starting position.
	 * 
	 * @param l the logics handler which the entity is linked to
	 * @param sPosition the starting position of the obstacle in the environment
	 */
	ObstacleInstance(final Logics l, final Pair<Double,Double> sPosition){
		super(l, sPosition.clone());
		entityTag = "obstacle";
		startPos = sPosition;
	}
	
	/**
	 * Updates the obstacle's flags.
	 */
	private void updateFlags() {
		if(position.getX() < -screen.getTileSize()) {
			onClearArea = true;
		} else {
			onClearArea = false;
		}
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
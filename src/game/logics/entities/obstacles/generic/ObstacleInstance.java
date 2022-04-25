package game.logics.entities.obstacles.generic;

import game.frame.GameWindow;
import game.logics.entities.generic.EntityInstance;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The abstract class <code>ObstacleInstance</code> is used to define the common parts of each obstacle.
 * 
 * @author Daniel Pellanda
 */
public abstract class ObstacleInstance extends EntityInstance implements Obstacle{

	/**
	 * Defines the movement parameters of the obstacle.
	 */
	protected SpeedHandler movement;
	
	/**
	 * Constructor that sets up obstacle default values (picked up from 
	 * <code>Logics</code>), defines it's bounds in the environment and allows to set it's
	 * starting position.
	 * 
	 * @param l the logics handler which the entity is linked to
	 * @param sPosition the starting position of the obstacle in the environment
	 */
	protected ObstacleInstance(final Logics l, final Pair<Double,Double> p, final SpeedHandler s){
		super(l, p);
		entityTag = EntityType.OBSTACLE;
		movement = s.clone();
	}
	
	public SpeedHandler getSpeedHandler() {
		return movement;
	}
	
	@Override
	public void reset() {
		super.reset();
		movement.resetSpeed();
	}
	
	@Override
	public void update() {
		super.update();
		
		if(position.getX() > -screen.getTileSize() * 2) {
			position.setX(position.getX() - movement.getXSpeed() / GameWindow.fpsLimit);
			if(!this.isOnSpawnArea()) {
				movement.applyAcceleration();
			}
		}
	}
}

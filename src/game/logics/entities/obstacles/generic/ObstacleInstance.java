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
	 * A flag that automatically updates and tells if the entity is on the "clear area".
	 */
	private boolean onClearArea = false;
	
	/**
	 * A flag that automatically updates and tells if the entity is on the "spawn area".
	 */
	private boolean onSpawnArea = true;
	
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
		movement = s;
	}
	
	/**
	 * Updates the obstacle's flags.
	 */
	private void updateFlags() {
		if(position.getX() < -screen.getTileSize()) {
			onClearArea = true;
			onSpawnArea = false;
		} else if(position.getX() >= screen.getWidth()){
			onClearArea = false;
			onSpawnArea = true;
		} else {
			onClearArea = false;
			onSpawnArea = false;
		}
	}
	
	public SpeedHandler getSpeedHandler() {
		return movement;
	}
	
	public boolean isOnClearArea() {
		return this.onClearArea;
	}
	
	public boolean isOnSpawnArea() {
		return this.onSpawnArea;
	}
	
	@Override
	public void reset() {
		super.reset();
		movement.resetSpeed();
	}
	
	@Override
	public void update() {
		super.update();
		updateFlags();
		
		if(position.getX() > -screen.getTileSize() * 2) {
			position.setX(position.getX() - movement.getXSpeed() / GameWindow.fpsLimit);
			if(!this.isOnSpawnArea()) {
				movement.applyAcceleration();
			}
		}
		this.hitbox.updatePosition(this.position);
	}
}

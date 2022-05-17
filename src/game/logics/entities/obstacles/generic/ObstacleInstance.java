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
     * {@link Logics}), defines it's bounds in the environment and allows to set it's
     * starting position.
     * 
     * @param l the logics handler which the entity is linked to
     * @param position the starting position of the obstacle in the environment
     * @param obstacleType the type of obstacle to create
     * @param speed the {@link SpeedHandler} to use for the obstacle
     */
    protected ObstacleInstance(final Logics l, final Pair<Double, Double> position, final EntityType obstacleType, final SpeedHandler speed){
        super(l, position, obstacleType);
        movement = speed.clone();
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
        
        if(this.getPosition().getX() > -GameWindow.GAME_SCREEN.getTileSize() * 2) {
        	this.getPosition().setX(this.getPosition().getX() - movement.getXSpeed() / GameWindow.FPS_LIMIT);
            if(!this.isOnSpawnArea()) {
                movement.applyAcceleration();
            }
        }
        this.getHitbox().updatePosition(this.getPosition());
    }
}

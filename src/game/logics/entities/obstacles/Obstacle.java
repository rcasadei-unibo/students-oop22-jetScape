package game.logics.entities.obstacles;

import game.logics.entities.basic.Entity;
import game.logics.interactions.SpeedHandler;

public abstract interface Obstacle extends Entity{
	
	void resetPosition();
	
	boolean isOutofScreen();
	
	SpeedHandler getSpeedHandler();
}

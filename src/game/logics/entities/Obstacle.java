package game.logics.entities;

import game.logics.interactions.SpeedHandler;

public abstract interface Obstacle extends Entity{
	
	void resetPosition();
	
	boolean isOutofScreen();
	
	SpeedHandler getSpeedHandler();
}

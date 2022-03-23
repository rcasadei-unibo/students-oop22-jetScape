package game.logics.entities;

import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.screen.Pair;

public abstract class ObstacleInstance extends EntityInstance{

	protected SpeedHandler movement = new SpeedHandler();
	
	ObstacleInstance(final Logics l, final Pair<Double,Double> position){
		super(l, position);
		entityTag = "obstacle";
	}
}

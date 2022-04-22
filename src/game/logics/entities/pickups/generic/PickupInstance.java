package game.logics.entities.pickups.generic;

import game.logics.entities.generic.EntityInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.Pair;

public abstract class PickupInstance extends EntityInstance implements Pickup{
	
	protected final SpeedHandler movement;
	protected final Player player;

	protected PickupInstance(final Logics l, final Pair<Double,Double> position, final Player player, final SpeedHandler speed) {
		super(l, position);
		this.player = player;
		this.movement = speed;
		
		entityTag = "pickup";
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
			position.setX(position.getX() - movement.getXSpeed() / maximumFPS);
		}
	}
}

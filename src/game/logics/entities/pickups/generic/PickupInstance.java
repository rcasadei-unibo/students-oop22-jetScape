package game.logics.entities.pickups.generic;

import game.frame.GameWindow;
import game.logics.entities.generic.EntityInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.hitbox.PickableHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

public abstract class PickupInstance extends EntityInstance implements Pickup{
    
    protected final SpeedHandler movement;
    protected final Player player;

    protected PickupInstance(final Logics l, final Pair<Double,Double> position, final EntityType pickupType, final Player player, final SpeedHandler speed) {
        super(l, position, pickupType);
        this.player = player;
        this.movement = speed.clone();
        
        this.setHitbox(new PickableHitbox(position));
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
        }
        this.getHitbox().updatePosition(this.getPosition());
    }
}

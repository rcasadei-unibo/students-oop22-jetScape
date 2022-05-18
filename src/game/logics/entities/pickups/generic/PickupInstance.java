package game.logics.entities.pickups.generic;

import game.frame.GameWindow;
import game.logics.entities.generic.EntityInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.hitbox.PickableHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The abstract class {@link PickupInstance} is used to define the common parts of each pickup bonus.
 */
public abstract class PickupInstance extends EntityInstance implements Pickup {

    private final SpeedHandler movement;
    private final Player player;

    /**
     * @param l the logics handler which the entity is linked to
     * @param position the starting position of the pickup in the environment
     * @param pickupType the type of pickup to create
     * @param player a reference to the {@link Player} entity
     * @param speed the {@link SpeedHandler} to use for the pickup
     */
    protected PickupInstance(final Logics l, final Pair<Double, Double> position, final EntityType pickupType, final Player player, final SpeedHandler speed) {
        super(l, position, pickupType);
        this.player = player;
        this.movement = speed.clone();

        this.setHitbox(new PickableHitbox(position));
    }
    /**
     * {@inheritDoc}
     */
    public SpeedHandler getSpeedHandler() {
        return movement;
    }
    /**
     * {@inheritDoc}
     */
    public Player getPlayerPointer() {
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        movement.resetSpeed();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update();

        if (this.getPosition().getX() > -GameWindow.GAME_SCREEN.getTileSize() * 2) {
            this.getPosition().setX(this.getPosition().getX() - movement.getXSpeed() / GameWindow.FPS_LIMIT);
        }
        this.getHitbox().updatePosition(this.getPosition());
    }
}

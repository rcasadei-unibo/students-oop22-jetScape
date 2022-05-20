package game.logics.entities.pickups.shield;

import java.awt.Color;

import game.logics.entities.pickups.generic.PickupInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class {@link ShieldInstance} is used for defining a shield pickup in the environment.
 * 
 * A shield is a bonus item that can be picked up by the player granting a one-time
 * protection from any hit from any obstacle.
 */
public class ShieldInstance extends PickupInstance implements Shield {

    /**
     * Specifies the path within the sprite folder [specified in {@link game.utility.sprites.Sprite Sprite} class]
     * where {@link ShieldInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "shield" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link ShieldInstance}.PLACE_HOLDER</code>.
     */
    private static final Color PLACE_HOLDER = Color.blue;

    /**
     * @param l the logics handler which the entity is linked to
     * @param position the starting position of the pickup in the environment
     * @param player a reference to the {@link Player} entity
     * @param speed the {@link SpeedHandler} to use for the pickup
     */
    public ShieldInstance(final Logics l, final Pair<Double, Double> position,  final Player player, final SpeedHandler speed) {
        super(l, position, EntityType.SHIELD, player, speed);

        final var spritesMgr = this.getSpriteManager();
        spritesMgr.setPlaceH(PLACE_HOLDER);
        spritesMgr.addSprite("shield", SPRITE_PATH + "shield.png");
        spritesMgr.setAnimator(() -> "shield");
    }
}

package game.logics.entities.pickups.teleport;

import java.awt.Color;

import game.logics.entities.pickups.generic.PickupInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class {@link TeleportInstance} is used for defining a teleport pickup in
 * the environment.
 * 
 * A teleport is a bonus item that can be picked up by the player granting a slight score
 * increase + the removal of all the current entities on the screen.
 */
public class TeleportInstance extends PickupInstance implements Teleport {

    /**
     * Specifies the path within the sprite folder [specified in {@link game.utility.sprites.Sprite Sprite} class]
     * where {@link TeleportInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "teleport" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link TeleportInstance}.PLACE_HOLDER</code>.
     */
    private static final Color PLACE_HOLDER = Color.red;

    private static final int SCORE_INCREASE = 250;

    /**
     * @param l the logics handler which the entity is linked to
     * @param position the starting position of the pickup in the environment
     * @param player a reference to the {@link Player} entity
     * @param speed the {@link SpeedHandler} to use for the pickup
     */
    public TeleportInstance(final Logics l, final Pair<Double, Double> position,
            final Player player, final SpeedHandler speed) {
        super(l, position, EntityType.TELEPORT, player, speed);

        final var spritesMgr = this.getSpriteManager();
        spritesMgr.setPlaceH(PLACE_HOLDER);
        spritesMgr.addSprite("teleport", SPRITE_PATH + "teleport.png");
        spritesMgr.setAnimator(() -> "teleport");
    }

    /**
     * @return the current score increase number for each picked up teleport
     */
    public static int getScoreIncrease() {
        return SCORE_INCREASE;
    }
}

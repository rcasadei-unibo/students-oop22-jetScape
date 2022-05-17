package game.logics.entities.pickups.teleport;

import java.awt.Color;

import game.logics.entities.pickups.generic.PickupInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

public class TeleportInstance extends PickupInstance implements Teleport{

    /**
     * Specifies the path within the sprite folder [specified in <code>Sprite</code> class]
     * where <code>TeleportInstance</code> sprites can be found.
     */
    private static final String texturePath = "teleport" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>TeleportInstance.placeH</code>.
     */
    private static final Color placeH = Color.red;
    
    private final int scoreInc = 250;
    
    public TeleportInstance(final Logics l, final Pair<Double, Double> position,  final Player player, final SpeedHandler speed) {
        super(l, position, EntityType.TELEPORT, player, speed);
        
        final var spritesMgr = this.getSpriteManager();
        spritesMgr.setPlaceH(placeH);
        spritesMgr.addSprite("teleport", texturePath + "teleport.png");
        spritesMgr.setAnimator(() -> "teleport");
    }
    
    public int getScoreIncrease() {
        return scoreInc;
    }
}

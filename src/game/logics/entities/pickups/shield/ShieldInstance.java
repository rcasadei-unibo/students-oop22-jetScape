package game.logics.entities.pickups.shield;

import java.awt.Color;

import game.logics.entities.pickups.generic.PickupInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

public class ShieldInstance extends PickupInstance implements Shield{

    /**
     * Specifies the path within the sprite folder [specified in <code>Sprite</code> class]
     * where <code>ShieldInstance</code> sprites can be found.
     */
    private static final String texturePath = "shield" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>ShieldInstance.placeH</code>.
     */
    private static final Color placeH = Color.blue;
    
    public ShieldInstance(final Logics l, final Pair<Double, Double> position,  final Player player, final SpeedHandler speed) {
        super(l, position, player, speed);
        entityTag = EntityType.SHIELD;
        
        spritesMgr.setPlaceH(placeH);
        spritesMgr.addSprite("shield", texturePath + "shield.png");
        spritesMgr.setAnimator(() -> "shield");
    }

}

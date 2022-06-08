package game.logics.entities.obstacles.zapper;

import java.awt.Color;

import game.logics.entities.obstacles.generic.ObstacleInstance;
import game.logics.handler.Logics;
import game.logics.hitbox.ZapperRayHorizontalHitbox;
import game.logics.hitbox.ZapperRayVerticalHitbox;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class {@link ZapperRayInstance} represents one part of the most common
 * type of obstacle that can be encountered during the game.
 * 
 * {@link ZapperRay} is one of the central parts of a Zapper obstacle, an electric trap
 * that can be get the player killed when he hits it.
 * Each Zapper is composed by 2 {@link ZapperBase} and as many {@link ZapperRay} as
 * the size of the trap.
 * 
 * Each {@link ZapperRayInstance} needs to be paired to the 2 {@link ZapperBaseInstance} that compose
 * the trap.
 */
public class ZapperRayInstance extends ObstacleInstance implements ZapperRay {

    /**
     * Specifies the path within the sprite folder [specified in {@link game.utility.sprites.Sprite Sprite} class]
     * where {@link ZapperRayInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "zapperray" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link ZapperRayInstance}.PLACE_HOLDER</code>.
     */
    private static final Color PLACE_HOLDER = Color.yellow;

    /**
     * The first {@link ZapperBase} paired.
     */
    private final ZapperBase electrode1;
    /**
     * The second {@link ZapperBase} paired.
     */
    private final ZapperBase electrode2;

    /**
     * Specifies the current rotation of the obstacle.
     */
    private String rotation = "diagonal-left";

    /**
     * Constructor used for initializing basic parts of the obstacle.
     * It also pairs the {@link ZapperBase} objects given to the object.
     * 
     * @param l the logics handler which the entity is linked to
     * @param p the starting position of the obstacle in the environment
     * @param e1 the first {@link ZapperBase} to pair
     * @param e2 the second {@link ZapperBase} to pair
     */
     public ZapperRayInstance(final Logics l, final Pair<Double, Double> p, final ZapperBase e1, final ZapperBase e2) {
        super(l, p, EntityType.ZAPPERRAY, e1.getSpeedHandler());

        electrode1 = e1;
        electrode2 = e2;

        updateRotation();

        if ("vertical".equals(this.rotation)) {
            this.setHitbox(new ZapperRayVerticalHitbox(p));
        } else {
            this.setHitbox(new ZapperRayHorizontalHitbox(p));
        }

        final var spritesMgr = this.getSpriteManager();
        spritesMgr.setPlaceH(PLACE_HOLDER);
        spritesMgr.addSprite("vertical", SPRITE_PATH + "zapperray_vert.png");
        spritesMgr.addSprite("horizontal", SPRITE_PATH + "zapperray_horr.png");
        spritesMgr.setAnimator(() -> rotation);
    }

    /**
     * Updates the object rotation, depending of the position of the paired {@link ZapperBase} objects.
     */
    private void updateRotation() {
        if (Math.round(electrode1.getPosition().getX()) == Math.round(electrode2.getPosition().getX())) {
            rotation = "vertical";
        } else if (Math.round(electrode1.getPosition().getY()) == Math.round(electrode2.getPosition().getY())) {
            rotation = "horizontal";
        } else {
            rotation = "undefined";
        }
    }

}


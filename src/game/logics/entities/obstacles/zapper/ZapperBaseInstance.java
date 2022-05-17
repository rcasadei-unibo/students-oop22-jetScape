package game.logics.entities.obstacles.zapper;

import java.awt.Color;

import game.logics.entities.obstacles.generic.ObstacleInstance;
import game.logics.handler.Logics;
import game.logics.hitbox.ZapperBaseHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class {@link ZapperBaseInstance} represents one part of the most common
 * type of obstacle that can be encountered during the game.
 * 
 * {@link ZapperBase} is one of the two farthest point of a Zapper obstacle, an electric trap
 * that can be get the player killed when he hits it.
 * Each Zapper is composed by 2 {@link ZapperBase} and as many {@link ZapperRay} as
 * the size of the trap.
 * 
 * Each {@link ZapperBaseInstance} needs to be paired to another {@link ZapperBaseInstance}.
 */
public class ZapperBaseInstance extends ObstacleInstance implements ZapperBase {

    /**
     * Specifies the path within the sprites folder [specified in {@link Sprite} class]
     * where {@link ZapperBaseInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "zapperbase" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link ZapperBaseInstance}.PLACE_HOLDER</code>.
     */
    private static final Color PLACE_HOLDER = Color.gray;

    /**
     * Specifies the master class where all the zapper entities are managed.
     */
    private Zapper master;

    /**
     * Specifies the current rotation of the obstacle.
     */
    private String rotation = "up";

    private boolean hasMaster = false;

    /**
     * Constructor used for initializing basic parts of the obstacle
     * and for giving its movement behavior in game.
     * 
     * @param l the logics handler which the entity is linked to
     * @param position the starting position of the obstacle in the environment
     * @param s the movement behavior the obstacle has to followed once loaded up
     */
    public ZapperBaseInstance(final Logics l, final Pair<Double, Double> position, final SpeedHandler s) {
        super(l, position, EntityType.ZAPPERBASE, s);
        this.setHitbox(new ZapperBaseHitbox(position));
    }

    /**
     * {@inheritDoc}
     */
    public void setMaster(final Zapper zap) {
        if (!this.hasMaster) {
            this.master = zap;
            this.hasMaster = true;

            updateRotation();

            final var spritesMgr = this.getSpriteManager();
            spritesMgr.setPlaceH(PLACE_HOLDER);
            spritesMgr.addSprite("up", SPRITE_PATH + "zapperbase_up.png");
            spritesMgr.addSprite("down", SPRITE_PATH + "zapperbase_down.png");
            spritesMgr.addSprite("left", SPRITE_PATH + "zapperbase_left.png");
            spritesMgr.addSprite("right", SPRITE_PATH + "zapperbase_right.png");
            spritesMgr.setAnimator(() -> rotation);
        }
    }

    /**
     * Updates the object rotation, depending of the position of the paired {@link ZapperBaseInstance}.
     */
    private void updateRotation() {
        ZapperBase pairedBase = master.getPaired(this);
        if (Math.round(this.getPosition().getX()) == Math.round(pairedBase.getPosition().getX())) {
            if (this.getPosition().getY() < pairedBase.getPosition().getY()) {
                rotation = "down";
            } else {
                rotation = "up";
            }
        } else if (Math.round(this.getPosition().getY()) == Math.round(pairedBase.getPosition().getY())) {
            if (this.getPosition().getX() > pairedBase.getPosition().getX()) {
                rotation = "left";
            } else {
                rotation = "right";
            }
        } else {
            rotation = "undefined";
        }
    }
}

package game.logics.entities.obstacles.zapper;

import java.awt.Color;

import game.logics.entities.obstacles.generic.ObstacleInstance;
import game.logics.handler.Logics;
import game.logics.hitbox.ZapperRayHorizontalHitbox;
import game.logics.hitbox.ZapperRayVerticalHitbox;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class <code>ZapperRayInstance</code> represents one part of the most common
 * type of obstacle that can be encountered during the game.
 * 
 * <code>ZapperRay</code> is one of the central parts of a Zapper obstacle, an electric trap
 * that can be get the player killed when he hits it.
 * Each Zapper is composed by 2 <code>ZapperBase</code> and as many <code>ZapperRay</code> as
 * the size of the trap.
 * 
 * Each <code>ZapperRayInstance</code> needs to be paired to the 2 <code>ZapperBaseInstance</code> that compose
 * the trap.
 * 
 * @author Daniel Pellanda
 */
public class ZapperRayInstance extends ObstacleInstance implements ZapperRay{

	/**
	 * Specifies the path within the sprite folder [specified in <code>Sprite</code> class]
	 * where <code>ZapperRayInstance</code> sprites can be found.
	 */
	private static final String spritePath = "zapperray" + System.getProperty("file.separator");
	/**
	 * If sprites are missing, they will be replace by a rectangle of the color specified in
	 * <code>ZapperRayInstance.placeH</code>.
	 */
	private static final Color placeH = Color.yellow;
	
	/**
	 * The first <code>ZapperBase</code> paired.
	 */
	private final ZapperBase electrode1;
	/**
	 * The second <code>ZapperBase</code> paired.
	 */
	private final ZapperBase electrode2;

	/**
	 * Specifies the current rotation of the obstacle.
	 */
	private String rotation = "diagonal-left";

	
	/**
	 * Constructor used for initializing basic parts of the obstacle.
	 * It also pairs the <code>ZapperBase</code> objects given to the object.
	 * 
	 * @param l the logics handler which the entity is linked to
	 * @param p the starting position of the obstacle in the environment
	 * @param e1 the first <code>ZapperBase</code> to pair
	 * @param e2 the second <code>ZapperBase</code> to pair
	 */
	public ZapperRayInstance(final Logics l, final Pair<Double,Double> p, final ZapperBase e1, final ZapperBase e2) {
		super(l, p, e1.getSpeedHandler());
		entityTag = EntityType.ZAPPERRAY;
		
		electrode1 = e1;
		electrode2 = e2;	
		
		updateRotation();
		spritesMgr.setPlaceH(placeH);
		spritesMgr.addSprite("vertical", spritePath + "zapperray_vert.png");
		spritesMgr.addSprite("horizontal", spritePath + "zapperray_horr.png");
		spritesMgr.setAnimator(() -> rotation);
		if(this.rotation.equals("vertical")) {
			this.hitbox = new ZapperRayVerticalHitbox(p);
		} else {
			this.hitbox = new ZapperRayHorizontalHitbox(p);
		}
	}
	
	/**
	 * Updates the object rotation, depending of the position of the paired <code>ZapperBase</code> objects.
	 */
	private void updateRotation() {
		if(electrode1.getX() == electrode2.getX()) {
			rotation = "vertical";
		} else if(electrode1.getY() == electrode2.getY()) {
			rotation = "horizontal";
		} else if((electrode1.getX() > electrode2.getX() && electrode1.getY() < electrode2.getY()) || (electrode1.getX() < electrode2.getX() && electrode1.getY() > electrode2.getY())) {
			rotation = "diagonal-right";
		} else {
			rotation = "diagonal-left";
		}
	}

}

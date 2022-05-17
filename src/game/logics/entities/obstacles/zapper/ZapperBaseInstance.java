package game.logics.entities.obstacles.zapper;

import java.awt.Color;

import game.logics.entities.obstacles.generic.ObstacleInstance;
import game.logics.handler.Logics;
import game.logics.hitbox.ZapperBaseHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class <code>ZapperBaseInstance</code> represents one part of the most common
 * type of obstacle that can be encountered during the game.
 * 
 * <code>ZapperBase</code> is one of the two farthest point of a Zapper obstacle, an electric trap
 * that can be get the player killed when he hits it.
 * Each Zapper is composed by 2 <code>ZapperBase</code> and as many <code>ZapperRay</code> as
 * the size of the trap.
 * 
 * Each <code>ZapperBaseInstance</code> needs to be paired to another <code>ZapperBaseInstance</code>.
 * 
 * @author Daniel Pellanda
 */
public class ZapperBaseInstance extends ObstacleInstance implements ZapperBase{

	/**
	 * Specifies the path within the sprites folder [specified in <code>Sprite</code> class]
	 * where <code>ZapperBaseInstance</code> sprites can be found.
	 */
	private static final String spritePath = "zapperbase" + System.getProperty("file.separator");
	/**
	 * If sprites are missing, they will be replace by a rectangle of the color specified in
	 * <code>ZapperBaseInstance.placeH</code>.
	 */
	private static final Color placeH = Color.gray;
	
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
	public ZapperBaseInstance(final Logics l, final Pair<Double,Double> position, final SpeedHandler s) {
		super(l, position, s);
		entityTag = EntityType.ZAPPERBASE;
		this.hitbox = new ZapperBaseHitbox(position);
	}
	
	public void setMaster(final Zapper zap) {
		if(!this.hasMaster) {
			this.master = zap;
			this.hasMaster = true;
			
			updateRotation();
			spritesMgr.setPlaceH(placeH);
			spritesMgr.addSprite("up", spritePath + "zapperbase_up.png");
			spritesMgr.addSprite("down", spritePath + "zapperbase_down.png");
			spritesMgr.addSprite("left", spritePath + "zapperbase_left.png");
			spritesMgr.addSprite("right", spritePath + "zapperbase_right.png");
			spritesMgr.setAnimator(() -> rotation);
		}
	}
	
	/**
	 * Updates the object rotation, depending of the position of the paired <code>ZapperBaseInstance</code>.
	 */
	private void updateRotation() {
		ZapperBase pairedBase = master.getPaired(this);
		if(this.getX() == pairedBase.getX()) {
			if(this.getY() < pairedBase.getY()) {
				this.setRotation("down");
			} else {
				this.setRotation("up");
			}
		} else if(this.getY() == pairedBase.getY()) {
			if(this.getX() > pairedBase.getX()) {
				this.setRotation("left");
			} else {
				this.setRotation("right");
			}
		} else if(this.getX() > pairedBase.getX() && this.getY() < pairedBase.getY()) {
			this.setRotation("down-left");
		} else if(this.getX() < pairedBase.getX() && this.getY() > pairedBase.getY()){
			this.setRotation("up-right");
		} else if(this.getX() < pairedBase.getX() && this.getY() < pairedBase.getY()) {
			this.setRotation("down-right");
		} else if(this.getX() > pairedBase.getX() && this.getY() > pairedBase.getY()) {
			this.setRotation("up-left");
		} else {
			this.setRotation("up");
		}
	}
	
	public void setRotation(final String rotation) {
		switch(rotation.toLowerCase()) {
			case "up":
				this.rotation = "up";
				break;
			case "down":
				this.rotation = "down";
				break;
			case "left":
				this.rotation = "left";
				break;
			case "right":
				this.rotation = "right";
				break;
			case "up-left":
			case "upper-left":
			case "upleft":
			case "upperleft":
				this.rotation = "up-left";
				break;
			case "up-right":
			case "upper-right":
			case "upright":
			case "upperright":
				this.rotation = "up-right";
				break;
			case "down-left":
			case "lower-left":
			case "downleft":
			case "lowerleft":
				this.rotation = "down-left";
				break;
			case "down-right":
			case "lower-right":
			case "downright":
			case "lowerright":
				this.rotation = "down-right";
				break;
		}
		
	}
}

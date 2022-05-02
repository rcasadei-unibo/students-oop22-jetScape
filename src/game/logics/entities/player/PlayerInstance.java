package game.logics.entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Set;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.generic.EntityInstance;
import game.logics.handler.Logics;
import game.logics.hitbox.PlayerHitbox;
import game.logics.interactions.CollisionsHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The <code>PlayerInstance</code> class represents the player's entity in
 * the game environment.
 * 
 * @author Daniel Pellanda
 */
public class PlayerInstance extends EntityInstance implements Player{
	
	/**
	 * Specifies the path within the sprite folder [specified in <code>Sprite</code> class]
	 * where <code>PlayerInstance</code> sprites can be found.
	 */
	private static final String texturePath = "player" + System.getProperty("file.separator");
	/**
	 * If sprites are missing, they will be replace by a rectangle of the color specified in
	 * <code>PlayerInstance.placeH</code>.
	 */
	private static final Color placeH = Color.white;
	/**
	 * Determines how fast sprite change.
	 */
	private static final double animationSpeed = 8;
	
	/**
	 * The horizontal position where the player will be.
	 */
	private final double xPosition = screen.getTileSize() * xRelativePosition;
	
	private boolean shieldProtected = false;
	
	private boolean invulnerable = false;
			
	/**
	 * The current player's score.
	 */
	private int score = 0;
	
	/**
	 * The current jump speed of the player.
	 */
	private final double jumpSpeed;
	/**
	 * The current fall speed of the player.
	 */
	private final double fallSpeed;

	/**
	 * The current multiplier applied to the speed jump.
	 */
	private double jumpMultiplier = initialJumpMultiplier;
	/**
	 * The current multiplier applied to the speed fall.
	 */
	private double fallMultiplier = initialFallMultiplier;
	
	/**
	 * A enumerable describing the current action of the player.
	 * It can either be <code>IDLE</code>, <code>LAND</code>(landing), <code>FALL</code>(falling) and <code>JUMP</code>(jumping).
	 */
	private PlayerAction action;
	
	/**
	 * Decides which sprite should be displayed.
	 */
	private int spriteSwitcher = 1;
	/**
	 * How many frames have passed since between a second and another.
	 */
	private int frameTime = 0;
	
	private final KeyHandler keyH;
	
	private final CollisionsHandler hitChecker;
	
	/**
	 * Constructor used for initializing basic parts of the player entity.
	 * 
	 * @param l the logics handler which the entity is linked to
	 */
	public PlayerInstance(final Logics l, final Map<EntityType, Set<Entity>> entities) {
		super();
		this.keyH = GameWindow.keyHandler;
		
		fallSpeed = baseFallSpeed / GameWindow.fpsLimit;
		jumpSpeed = baseJumpSpeed / GameWindow.fpsLimit;
		
		position = new Pair<>(xPosition, yGround);
		
		hitbox = new PlayerHitbox(position, screen);
		hitboxSet.add(this.hitbox);
		hitChecker = new CollisionsHandler(entities, this);
		
		action = PlayerAction.WALK;
		entityTag = EntityType.PLAYER;
		
		spritesMgr.setPlaceH(placeH);
		spritesMgr.addSprite("walk1", texturePath + "barrywalk1.png");
		spritesMgr.addSprite("walk2", texturePath + "barrywalk2.png");
		spritesMgr.addSprite("walk3", texturePath + "barrywalk3.png");
		spritesMgr.addSprite("walk4", texturePath + "barrywalk4.png");
		spritesMgr.addSprite("jump1", texturePath + "barryjump1.png");
		spritesMgr.addSprite("jump2", texturePath + "barryjump2.png");
		spritesMgr.addSprite("fall1", texturePath + "barryfall1.png");
		spritesMgr.addSprite("fall2", texturePath + "barryfall2.png");
		spritesMgr.addSprite("land1", texturePath + "barryland1.png");
		spritesMgr.addSprite("land2", texturePath + "barryland2.png");
		spritesMgr.addSprite("land3", texturePath + "barryland3.png");
		spritesMgr.addSprite("land4", texturePath + "barryland4.png");
		spritesMgr.addSprite("zapped1", texturePath + "barryzapped1.png");
		spritesMgr.addSprite("zapped2", texturePath + "barryzapped2.png");
		spritesMgr.addSprite("zapped3", texturePath + "barryzapped3.png");
		spritesMgr.addSprite("zapped4", texturePath + "barryzapped4.png");
		spritesMgr.addSprite("burned1", texturePath + "barryburned1.png");
		spritesMgr.addSprite("burned2", texturePath + "barryburned2.png");
		spritesMgr.addSprite("burned3", texturePath + "barryburned3.png");
		spritesMgr.addSprite("burned4", texturePath + "barryburned4.png");
		spritesMgr.addSprite("dead1", texturePath + "barrydead1.png");
		spritesMgr.setAnimator(() -> {
			int spriteSwitcher = action == PlayerAction.FALL || action == PlayerAction.JUMP ? (this.spriteSwitcher % 2 + 1) : action == PlayerAction.DEAD ? 1 : this.spriteSwitcher % 4 + 1;
			return action.toString() + spriteSwitcher;
		});
	}
	
	private void checkHit(final Entity entityHit) {
		switch(entityHit.entityType()) {
			case MISSILE: 
				if(!this.invulnerable && !action.isInDyingAnimation()) {
					this.invulnerable = true;
					if(this.shieldProtected) {
						this.shieldProtected = false;
						break;
					}
					setAction(PlayerAction.BURNED);
				}
				entityHit.hide();
				break;
			case ZAPPER:
				if(!this.invulnerable && !action.isInDyingAnimation()) {
					this.invulnerable = true;
					if(this.shieldProtected) {
						this.shieldProtected = false;
						break;
					}
					setAction(PlayerAction.ZAPPED);
				}
				break;
			case SHIELD:
				this.shieldProtected = true;
				break;
			default:
				break;
		}
	}
	
	private void jump() {
		fallMultiplier = initialFallMultiplier;

		
		position.setY(position.getY() - jumpSpeed * jumpMultiplier > yRoof ? position.getY() - jumpSpeed * jumpMultiplier : yRoof);
		setAction(PlayerAction.JUMP);
	}
	
	private boolean fall() {
		jumpMultiplier = initialJumpMultiplier;
		
		if(position.getY() + fallSpeed * fallMultiplier < yGround) {
			position.setY(position.getY() + fallSpeed * fallMultiplier);
			return true;
		}
		position.setY(yGround);
		return false;
	}
	
	private void controlPlayer() {
		if(keyH.input.get(KeyEvent.VK_SPACE)) {
			jump();
			jumpMultiplier += jumpMultiplierIncrease;
		} else if(action != PlayerAction.WALK) {
			setAction(fall() ? PlayerAction.FALL : PlayerAction.LAND);
			fallMultiplier += fallMultiplierIncrease;
		}
	}

	/**
	 * Sets the current player's action.
	 * 
	 * @param newAction the new action
	 */
	private void setAction(final PlayerAction newAction) {
		action.changeAction(newAction);
		action = newAction;
	}
	
	/**
	 * Updates the sprite that should be display during the animation.
	 */
	private void updateSprite() {
		if(PlayerAction.hasChanged) {
			frameTime = 0;
			spriteSwitcher = 0;
			PlayerAction.hasChanged = false;
		}
		else if(frameTime >= GameWindow.fpsLimit / animationSpeed) {
			if(PlayerAction.dying && spriteSwitcher >= 7) {
				setAction(PlayerAction.DEAD);
			}
			if(PlayerAction.landing && spriteSwitcher >= 3) {
				setAction(PlayerAction.WALK);
			}
			frameTime = 0;
			spriteSwitcher++;
		}
		frameTime++;
	}
	
	private void updateScore() {
		if(frameTime % 2 == 0) {
			this.score++;
		}
	}
	
	public int getCurrentScore() {
		return this.score;
	}
	
	public boolean hasDied() {
		return action == PlayerAction.DEAD;
	}
	
	@Override
	public void reset() {
		position.setX(xPosition);
		position.setY(yGround);
		action = PlayerAction.WALK;
		score = 0;
		frameTime = 0;
		
		invulnerable = false;
		shieldProtected = false;
	}
	
	@Override
	public void update() {
		super.update();
		this.updateSprite();
		
		if(!action.isInDyingAnimation()) {
			this.updateScore();
			this.controlPlayer();
		} 
		
		if(this.hasDied()) {
			fall();
			fallMultiplier += fallMultiplierIncrease * 4;
		}
	
		this.hitbox.updatePosition(position);
		this.hitChecker.interact(e -> checkHit(e));
	}

}

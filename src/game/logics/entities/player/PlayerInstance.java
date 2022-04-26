package game.logics.entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;

import game.frame.GameWindow;
import game.logics.entities.generic.EntityInstance;
import game.logics.handler.Logics;
import game.logics.hitbox.PlayerHitbox;
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
	private static final double animationSpeed = 6;
	
	/**
	 * The horizontal position where the player will be.
	 */
	private final double xPosition = screen.getTileSize() * xRelativePosition;
	
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
	
	private final KeyHandler keyH;
	
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
	
	/**
	 * Constructor used for initializing basic parts of the player entity.
	 * 
	 * @param l the logics handler which the entity is linked to
	 */
	public PlayerInstance(final Logics l) {
		super();
		this.keyH = GameWindow.keyHandler;
		
		fallSpeed = baseFallSpeed / GameWindow.fpsLimit;
		jumpSpeed = baseJumpSpeed / GameWindow.fpsLimit;
		
		position = new Pair<>(xPosition, yGround);
		this.hitbox = new PlayerHitbox(position, screen);
		
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
		spritesMgr.setAnimator(() -> {
			int spriteSwitcher = action != PlayerAction.WALK && action != PlayerAction.LAND ? (this.spriteSwitcher % 2 + 1) : this.spriteSwitcher;
			return action.toString() + spriteSwitcher;
		});
	}
	
	private void jump() {
		position.setY(position.getY() - jumpSpeed * jumpMultiplier > yRoof ? position.getY() - jumpSpeed * jumpMultiplier : yRoof);
		setAction(PlayerAction.JUMP);
	}
	
	private void fall() {
		if(position.getY() + fallSpeed * fallMultiplier < yGround) {
			position.setY(position.getY() + fallSpeed * fallMultiplier);
			setAction(PlayerAction.FALL);
		} else {
			position.setY(yGround);
			setAction(PlayerAction.LAND);
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
			spriteSwitcher = 1;
			PlayerAction.hasChanged = false;
		}
		else if(frameTime >= GameWindow.fpsLimit / animationSpeed) {
			if(PlayerAction.isLanding && spriteSwitcher == 4) {
				setAction(PlayerAction.WALK);
			}
			frameTime = 0;
			spriteSwitcher = spriteSwitcher >= 4 ? 1 : spriteSwitcher + 1;
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
	
	@Override
	public void reset() {
		super.reset();
		position.setX(xPosition);
		position.setY(yGround);
		action = PlayerAction.WALK;
		score = 0;
	}
	
	@Override
	public void update() {
		super.update();
		this.updateSprite();
		this.updateScore();
		if(keyH.input.get(KeyEvent.VK_SPACE)) {
			jump();
			jumpMultiplier += jumpMultiplierIncrease;
			fallMultiplier = initialFallMultiplier;
		} else if(action != PlayerAction.WALK) {
			fall();
			fallMultiplier += fallMultiplierIncrease;
			jumpMultiplier = initialJumpMultiplier;
		}
		this.hitbox.updatePosition(position);
	}

}

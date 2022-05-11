package game.logics.entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;

import java.util.Map;
import java.util.Set;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.generic.EntityInstance;
import game.logics.entities.pickups.teleport.Teleport;
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
public class PlayerInstance extends EntityInstance implements Player {
	
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
	 * The horizontal position where the player will be.
	 */
	private final double xPosition = screen.getTileSize() * xRelativePosition;
	private final Pair<Double,Double> shieldPosition = new Pair<>(0.0,0.0);
	
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
	 * A enumerable describing the current status of the player.
	 */
	private PlayerStatus status;
	private PlayerDeath causeOfDeath;

	
	/**
	 * Decides which sprite should be displayed.
	 */
	private int spriteSwitcher = 1;
	
	/**
	 * How many frames have passed since between a second and another.
	 */
	private int frameTime = 0;
	private int invulnerableTimer = -1;
	
	private final KeyHandler keyH;
	private final CollisionsHandler hitChecker;
	
	/**
	 * Constructor used for initializing basic parts of the player entity.
	 * 
	 * @param l the logics handler which the entity is linked to
	 */
	public PlayerInstance(final Logics l, final Map<EntityType, Set<Entity>> entities) {
		super(l);
		this.keyH = GameWindow.keyHandler;
		
		fallSpeed = baseFallSpeed / GameWindow.fpsLimit;
		jumpSpeed = baseJumpSpeed / GameWindow.fpsLimit;
		
		position = new Pair<>(xPosition, yGround);
		
		hitbox = new PlayerHitbox(position, screen);
		hitboxSet.add(this.hitbox);
		hitChecker = new CollisionsHandler(entities, this);
		
		status = PlayerStatus.WALK;
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
		spritesMgr.addSprite("shield", texturePath + "barryshield.png");
		spritesMgr.setAnimator(() -> {
			int spriteSwitcher = status == PlayerStatus.FALL || status == PlayerStatus.JUMP ? (this.spriteSwitcher % 2 + 1) : status == PlayerStatus.DEAD ? 1 : this.spriteSwitcher % 4 + 1;
			return status.toString() + spriteSwitcher;
		});
	}
	
	private void obstacleHit(final PlayerStatus statusAfterHit) {
		if(!this.invulnerable && !status.isInDyingAnimation()) {
			if(this.shieldProtected) {
				this.invulnerable = true;
				this.shieldProtected = false;
				return;
			}
			this.setStatus(statusAfterHit);
			this.setCauseOfDeathIfDead();
		}
	}
	
	private void checkHit(final Entity entityHit) {
		switch(entityHit.entityType()) {
			case MISSILE: 
				this.obstacleHit(PlayerStatus.BURNED);
				entityHit.clean();
				break;
			case ZAPPER:
				this.obstacleHit(PlayerStatus.ZAPPED);
				break;
			case SHIELD:
				this.shieldProtected = true;
				entityHit.clean();
				break;
			case TELEPORT:
				score += Teleport.scoreIncrease;
				cleaner.accept(t -> t.isGenerableEntity(), e -> true);
			default:
				break;
		}
	}
	
	private void jump() {
		fallMultiplier = initialFallMultiplier;

		position.setY(position.getY() - jumpSpeed * jumpMultiplier > yRoof ? position.getY() - jumpSpeed * jumpMultiplier : yRoof);
		setStatus(PlayerStatus.JUMP);
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
		} else if(status != PlayerStatus.WALK) {
			setStatus(fall() ? PlayerStatus.FALL : PlayerStatus.LAND);
			fallMultiplier += fallMultiplierIncrease;
		}
	}

	/**
	 * Sets the current player's status.
	 * 
	 * @param newStatus the new status
	 */
	private void setStatus(final PlayerStatus newStatus) {
		status.changeStatus(newStatus);
		status = newStatus;
	}
	
	/**
	 * Updates the sprite that should be display during the animation.
	 */
	private void updateSprite() {
		if(PlayerStatus.hasChanged) {
			frameTime = 0;
			spriteSwitcher = 0;
			PlayerStatus.hasChanged = false;
		}
		else if(frameTime >= GameWindow.fpsLimit / animationSpeed) {
			if(PlayerStatus.dying && spriteSwitcher >= 7) {
				setStatus(PlayerStatus.DEAD);
			}
			if(PlayerStatus.landing && spriteSwitcher >= 3) {
				setStatus(PlayerStatus.WALK);
			}
			frameTime = 0;
			spriteSwitcher++;
		}
		frameTime++;
	}
	
	private void updateInvulnerableTimer() {
		if(this.invulnerable) {
			if(this.invulnerableTimer == -1) {
				this.invulnerableTimer = Logics.getFrameTime();
			} else if(Logics.getFrameTime() - this.invulnerableTimer >= invicibilityTime * GameWindow.fpsLimit) {
				this.invulnerable = false;
				this.invulnerableTimer = -1;
			}
		}
	}
	
	private void updateScore() {
		if(frameTime % 2 == 0) {
			this.score++;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCurrentScore() {
		return this.score;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasDied() {
		return status == PlayerStatus.DEAD;
	}
	
	private void setCauseOfDeathIfDead() {
		switch(status) {
			case BURNED:
				causeOfDeath = Player.PlayerDeath.BURNED;
				break;
			case ZAPPED:
				causeOfDeath = Player.PlayerDeath.ZAPPED;
				break;
			default:
				causeOfDeath = Player.PlayerDeath.NONE;
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Player.PlayerDeath getCauseOfDeath() {
		return this.causeOfDeath;
	}
	
	@Override
	public void reset() {
		position.setX(xPosition);
		position.setY(yGround);
		setStatus(PlayerStatus.WALK);
		score = 0;
		frameTime = 0;
		
		invulnerable = false;
		shieldProtected = false;
	}
	
	@Override
	public void update() {
		super.update();
		this.updateSprite();
		this.updateInvulnerableTimer();
		
		if(!status.isInDyingAnimation()) {
			this.updateScore();
			this.controlPlayer();
		} 
		
		if(this.hasDied()) {
			fall();
			fallMultiplier += fallMultiplierIncrease * 4;
		}
	
		shieldPosition.setX(position.getX() + screen.getTileSize() / 16);
		shieldPosition.setY(position.getY());
		this.hitbox.updatePosition(position);
		this.hitChecker.interact(e -> checkHit(e));
	}
	
	@Override
	public void draw(final Graphics2D g) {
		if(this.isVisible()) {
			if(!this.invulnerable || frameTime % flickeringSpeed < flickeringSpeed / 2) {
				this.spritesMgr.drawCurrentSprite(g, position, screen.getTileSize());
			}
			
			if(this.shieldProtected) {
				this.spritesMgr.drawSprite(g, "shield", shieldPosition, screen.getTileSize());
			}
		}
	}
}

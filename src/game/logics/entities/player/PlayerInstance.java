package game.logics.entities.player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.Set;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.generic.EntityInstance;
import game.logics.entities.pickups.teleport.TeleportInstance;
import game.logics.handler.AbstractLogics;
import game.logics.handler.Logics;
import game.logics.hitbox.PlayerHitbox;
import game.logics.interactions.CollisionsHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;
import game.utility.other.Sound;

/**
 * The {@link PlayerInstance} class represents the player's entity in
 * the game environment.
 * 
 */
public class PlayerInstance extends EntityInstance implements Player {

    /**
     * Specifies the path within the sprite folder [specified in {@link game.utility.sprites.Sprite Sprite} class]
     * where {@link PlayerInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "player" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link PlayerInstance}.PLACE_HOLDER</code>.
     */
    private static final Color PLACE_HOLDER = Color.white;

    private static final double BASE_FALL_SPEED = 50.0;
    private static final double BASE_JUMP_SPEED = 20.0;
    private static final double INITIAL_JUMP_MULTIPLIER = 1.0;
    private static final double INITIAL_FALL_MULTIPLIER = 0.3;
    private static final double JUMP_MULTIPLIER_INCREASE = 0.6;
    private static final double FALL_MULTIPLIER_INCREASE = 0.15;
    private static final double X_RELATIVE_POSITION = 2.11;

    private static final int FLICKERING_SPEED = 10;
    private static final int INVINCIBILITY_TIMER = 2;

    private static final double ANIMATION_SPEED = 7;

    /**
     * The horizontal position where the player will be.
     */
    private static final double X_POSITION = GameWindow.GAME_SCREEN.getTileSize() * X_RELATIVE_POSITION;
    private final Pair<Double, Double> shieldPosition = new Pair<>(0.0, 0.0);

    private boolean shieldProtected;
    private boolean invulnerable;

    /**
     * The current player's score.
     */
    private int score;
    /**
     * The current player's number of coins collected.
     */
    private int coins;
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
    private double jumpMultiplier = INITIAL_JUMP_MULTIPLIER;
    /**
     * The current multiplier applied to the speed fall.
     */
    private double fallMultiplier = INITIAL_FALL_MULTIPLIER;

    /**
     * Decides which sprite should be displayed.
     */
    private int spriteSwitcher = 1;

    /**
     * How many frames have passed since between a second and another.
     */
    private int frameTime;
    private int invulnerableTimer = -1;

    private final KeyHandler keyH;
    private final CollisionsHandler hitChecker;

    /**
     * A enumerable describing the current status of the player.
     */
    private PlayerStatus status;
    private boolean statusChanged;

    private PlayerDeath causeOfDeath;

    /**
     * Constructor used for initializing basic parts of the player entity.
     * 
     * @param l the logics handler which the entity is linked to
     * @param entities the map where all current active entities are contained
     */
    public PlayerInstance(final Logics l, final Map<EntityType, Set<Entity>> entities) {

        super(l, new Pair<>(X_POSITION, Y_LOW_LIMIT), EntityType.PLAYER);
        this.keyH = GameWindow.GAME_KEYHANDLER;

        this.fallSpeed = BASE_FALL_SPEED / GameWindow.FPS_LIMIT;
        this.jumpSpeed = BASE_JUMP_SPEED / GameWindow.FPS_LIMIT;

        this.setHitbox(new PlayerHitbox(this.getPosition()));
        hitChecker = new CollisionsHandler(entities, this);

        this.status = PlayerStatus.WALK;

        final var spritesMgr = this.getSpriteManager();

        spritesMgr.setPlaceH(PLACE_HOLDER);
        spritesMgr.addSprite("walk1", SPRITE_PATH + "barrywalk1.png");
        spritesMgr.addSprite("walk2", SPRITE_PATH + "barrywalk2.png");
        spritesMgr.addSprite("walk3", SPRITE_PATH + "barrywalk3.png");
        spritesMgr.addSprite("walk4", SPRITE_PATH + "barrywalk4.png");
        spritesMgr.addSprite("jump1", SPRITE_PATH + "barryjump1.png");
        spritesMgr.addSprite("jump2", SPRITE_PATH + "barryjump2.png");
        spritesMgr.addSprite("fall1", SPRITE_PATH + "barryfall1.png");
        spritesMgr.addSprite("fall2", SPRITE_PATH + "barryfall2.png");
        spritesMgr.addSprite("land1", SPRITE_PATH + "barryland1.png");
        spritesMgr.addSprite("land2", SPRITE_PATH + "barryland2.png");
        spritesMgr.addSprite("land3", SPRITE_PATH + "barryland3.png");
        spritesMgr.addSprite("land4", SPRITE_PATH + "barryland4.png");
        spritesMgr.addSprite("zapped1", SPRITE_PATH + "barryzapped1.png");
        spritesMgr.addSprite("zapped2", SPRITE_PATH + "barryzapped2.png");
        spritesMgr.addSprite("zapped3", SPRITE_PATH + "barryzapped3.png");
        spritesMgr.addSprite("zapped4", SPRITE_PATH + "barryzapped4.png");
        spritesMgr.addSprite("burned1", SPRITE_PATH + "barryburned1.png");
        spritesMgr.addSprite("burned2", SPRITE_PATH + "barryburned2.png");
        spritesMgr.addSprite("burned3", SPRITE_PATH + "barryburned3.png");
        spritesMgr.addSprite("burned4", SPRITE_PATH + "barryburned4.png");
        spritesMgr.addSprite("dead1", SPRITE_PATH + "barrydead1.png");
        spritesMgr.addSprite("shield", SPRITE_PATH + "barryshield.png");
        spritesMgr.setAnimator(() -> {
            final int spriteSwitcher = status == PlayerStatus.FALL
                    || status == PlayerStatus.JUMP
                    ? this.spriteSwitcher % 2 + 1
                    : status == PlayerStatus.DEAD
                            ? 1 : this.spriteSwitcher % 4 + 1;
            return status.toString() + spriteSwitcher;
        });
    }

    private void obstacleHit(final PlayerStatus statusAfterHit) {
        if (!this.invulnerable && !status.isInDyingAnimation()) {
            if (this.shieldProtected) {
                this.invulnerable = true;
                this.shieldProtected = false;
                GameWindow.GAME_SOUND.play(Sound.SHIELD_DOWN);
                return;
            }
            this.setStatus(statusAfterHit);
            this.setCauseOfDeath(statusAfterHit);
        }
    }

    private void checkHit(final Entity entityHit) {
        switch (entityHit.entityType()) {
            case MISSILE:
                if (!this.shieldProtected) {
                    GameWindow.GAME_SOUND.stop(Sound.JETPACK);
                    GameWindow.GAME_SOUND.play(Sound.MISSILE);
                }
                this.obstacleHit(PlayerStatus.BURNED);
                entityHit.clean();
                break;
            case ZAPPER:
                if (!this.shieldProtected) {
                    GameWindow.GAME_SOUND.stop(Sound.JETPACK);
                    GameWindow.GAME_SOUND.play(Sound.ZAPPED);
                }
                this.obstacleHit(PlayerStatus.ZAPPED);
                break;
            case SHIELD:
                this.shieldProtected = true;
                entityHit.clean();
                GameWindow.GAME_SOUND.play(Sound.SHIELD_UP);
                break;
            case TELEPORT:
                this.score += TeleportInstance.getScoreIncrease();
                this.getCleaner().accept(t -> t.isGenerableEntity(), e -> true);
                GameWindow.GAME_SOUND.play(Sound.TELEPORT);
                break;
            case COIN:
                this.coins++;
                entityHit.clean();
                GameWindow.GAME_SOUND.play(Sound.COIN);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the current player's status.
     * 
     * @param newStatus the new status
     */
    private void setStatus(final PlayerStatus newStatus) {
        statusChanged = status != newStatus;
        status = newStatus;
    }

    private void jump() {
        fallMultiplier = INITIAL_FALL_MULTIPLIER;

        this.getPosition().setY(this.getPosition().getY() - jumpSpeed * jumpMultiplier > Y_TOP_LIMIT
                ? this.getPosition().getY() - jumpSpeed * jumpMultiplier
                : Y_TOP_LIMIT);
        setStatus(PlayerStatus.JUMP);
        if (statusChanged) {
            GameWindow.GAME_SOUND.playInLoop(Sound.JETPACK);
        }
    }

    private boolean fall() {
        jumpMultiplier = INITIAL_JUMP_MULTIPLIER;

        if (this.getPosition().getY() + fallSpeed * fallMultiplier < Y_LOW_LIMIT) {
            this.getPosition().setY(this.getPosition().getY() + fallSpeed * fallMultiplier);
            return true;
        }
        this.getPosition().setY(Y_LOW_LIMIT);
        return false;
    }

    private void controlPlayer() {
        if (keyH.getCurrentInput(KeyEvent.VK_SPACE)) {
            jump();
            jumpMultiplier += JUMP_MULTIPLIER_INCREASE;
        } else if (status != PlayerStatus.WALK) {
            setStatus(fall() ? PlayerStatus.FALL : PlayerStatus.LAND);
            fallMultiplier += FALL_MULTIPLIER_INCREASE;
            if (statusChanged) {
                GameWindow.GAME_SOUND.stop(Sound.JETPACK);
            }
        }
    }

    /**
     * Updates the sprite that should be display during the animation.
     */
    private void updateSprite() {
        final int lastDeathSprite = 7;
        final int lastLandSprite = 3;

        if (this.statusChanged) {
            frameTime = 0;
            spriteSwitcher = 0;
            this.statusChanged = false;
        } else if (frameTime >= GameWindow.FPS_LIMIT / ANIMATION_SPEED) {
            if (status.isInDyingAnimation() && spriteSwitcher >= lastDeathSprite) {
                setStatus(PlayerStatus.DEAD);
            }
            if (status == PlayerStatus.LAND && spriteSwitcher >= lastLandSprite) {
                setStatus(PlayerStatus.WALK);
            }
            frameTime = 0;
            spriteSwitcher++;
        }
        frameTime++;
    }

    private void updateInvulnerableTimer() {
        if (this.invulnerable) {
            if (this.invulnerableTimer == -1) {
                this.invulnerableTimer = AbstractLogics.getFrameTime();
            } else if (AbstractLogics.getFrameTime() - this.invulnerableTimer >= INVINCIBILITY_TIMER * GameWindow.FPS_LIMIT) {
                this.invulnerable = false;
                this.invulnerableTimer = -1;
            }
        }
    }

    private void updateScore() {
        if (frameTime % 2 == 0) {
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
    public int getCurrentCoinsCollected() {
        return this.coins;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasDied() {
        return status == PlayerStatus.DEAD;
    }

    private void setCauseOfDeath(final PlayerStatus deathCause) {
        switch (deathCause) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        this.setStatus(PlayerStatus.WALK);
        this.score = 0;
        this.coins = 0;

        this.frameTime = 0;

        this.invulnerable = false;
        this.shieldProtected = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update();
        this.updateSprite();
        this.updateInvulnerableTimer();

        if (!status.isInDyingAnimation()) {
            this.updateScore();
            this.controlPlayer();
        } 

        if (this.hasDied()) {
            fall();
            fallMultiplier += FALL_MULTIPLIER_INCREASE * 4;
        }

        shieldPosition.setX(this.getPosition().getX() + GameWindow.GAME_SCREEN.getTileSize() / 16.0);
        shieldPosition.setY(this.getPosition().getY());

        this.getHitbox().updatePosition(this.getPosition());
        this.hitChecker.interact(e -> checkHit(e));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(final Graphics2D g) {
        if (this.isVisible()) {
            if (!this.invulnerable || frameTime % FLICKERING_SPEED < FLICKERING_SPEED / 2) {
                this.getSpriteManager().drawCurrentSprite(g, this.getPosition(), GameWindow.GAME_SCREEN.getTileSize());
            }

            if (this.shieldProtected) {
                this.getSpriteManager().drawSprite(g, "shield", shieldPosition, GameWindow.GAME_SCREEN.getTileSize());
            }
        }
    }
}


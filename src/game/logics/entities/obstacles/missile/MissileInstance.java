package game.logics.entities.obstacles.missile;

import java.awt.Color;
import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.logics.entities.obstacles.generic.ObstacleInstance;
import game.logics.entities.player.Player;
import game.logics.handler.AbstractLogics;
import game.logics.handler.Logics;
import game.logics.hitbox.MissileHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

/**
 * The class {@link MissileInstance} is used for defining a missile obstacle in the environment.
 * 
 * A missile is a fast projective that can damage the player if hit, when a missile is about to appear
 * a warning icon will alert you from which direction the missile is coming from.
 */
public class MissileInstance extends ObstacleInstance implements Missile {

    /**
     * Specifies the path within the sprite folder [specified in {@link Sprite} class]
     * where {@link MissileInstance} sprites can be found.
     */
    private static final String SPRITE_PATH = "missile" + System.getProperty("file.separator");
    /**
     * If sprites are missing, they will be replace by a rectangle of the color specified in
     * <code>{@link MissileInstance}.PLACE_HOLDER</code> .
     */
    private static final Color PLACE_HOLDER = Color.red;

    private enum Direction { UP, DOWN };

    /**
     * The horizontal position of the missile warning.
     */
    private final double warnDefaultX = GameWindow.GAME_SCREEN.getWidth() - GameWindow.GAME_SCREEN.getTileSize() * 1.5;
    /**
     * The position of the missile warning is drawn.
     */
    private final Pair<Double, Double> warnPosition;

    /**
     * The position when the warning should start flickering.
     */
    private final double warnFlickRange = GameWindow.GAME_SCREEN.getWidth() + GameWindow.GAME_SCREEN.getTileSize() * 4;

    /**
     * The flickering speed of the missile warning when a missile is about to appear.
     */
    static final int WARN_FLICK_SPEED = 10;
    /**
     * How many frames have passed since between a second and another.
     */
    private int frameTime;

    /**
     * A reference to the player's position.
     */
    private final Pair<Double, Double> playerPosition;

    private static final double Y_DEFAULT_SPEED = 0;
    private static final double Y_DEFAULT_ACCELERATION = 400;

    private final double yStartSpeed = Y_DEFAULT_SPEED;
    private double ySpeed = yStartSpeed;
    private final double yAcceleration = Y_DEFAULT_ACCELERATION;
    static final double Y_BRAKING_DIVIDER = 3.5;

    static final double Y_BRAKE_DECREASE = 1.0;

    /**
     * The direction the missile was moving.
     */
    private Direction lastDir = Direction.UP;

    /**
     * Creates a {@link MissileInstance} with default values.
     * 
     * @param l the logics handler which the entity is linked to
     * @param pos the starting position of the entity in the environment
     * @param player a reference to player entity
     * @param speed the {@link SpeedHandler} used by the missile
     */
    public MissileInstance(final Logics l, final Pair<Double, Double> pos, final Player player, final SpeedHandler speed) {
        super(l, pos, EntityType.MISSILE, speed);

        warnPosition = new Pair<>(warnDefaultX, this.getPosition().getY());
        playerPosition = player.getPosition();

        this.setHitbox(new MissileHitbox(pos));

        final var spritesMgr = this.getSpriteManager();
        spritesMgr.setPlaceH(PLACE_HOLDER);
        spritesMgr.addSprite("warn", SPRITE_PATH + "warn.png");
        spritesMgr.addSprite("missile", SPRITE_PATH + "missile.png");
        spritesMgr.setAnimator(() -> "missile");
    }

    private void updateFrameTime() {
        frameTime++;
        if (frameTime >= GameWindow.FPS_LIMIT) {
            frameTime = 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        ySpeed = yStartSpeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        super.update();
        updateFrameTime();

        if (this.isOnSpawnArea()) {
            if (this.getPosition().getY() > playerPosition.getY()) {
                if (lastDir != Direction.UP) {
                    ySpeed = -ySpeed / Y_BRAKING_DIVIDER + Y_BRAKE_DECREASE * AbstractLogics.getDifficultyLevel();
                }
                this.getPosition().setY(this.getPosition().getY() - ySpeed / GameWindow.FPS_LIMIT);
                ySpeed += yAcceleration / GameWindow.FPS_LIMIT;
                lastDir = Direction.UP;
            } else if (this.getPosition().getY() < playerPosition.getY()) {
                if (lastDir != Direction.DOWN) {
                    ySpeed = -ySpeed / Y_BRAKING_DIVIDER + Y_BRAKE_DECREASE * AbstractLogics.getDifficultyLevel();
                }
                this.getPosition().setY(this.getPosition().getY() + ySpeed / GameWindow.FPS_LIMIT);
                ySpeed += yAcceleration  / GameWindow.FPS_LIMIT;
                lastDir = Direction.DOWN;
            }
        }
        warnPosition.setY(this.getPosition().getY());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(final Graphics2D g) {
        super.draw(g);
        if (!this.isVisible()) {
            return;
        }

        if (this.getPosition().getX() > GameWindow.GAME_SCREEN.getWidth()
                && (this.getPosition().getX() > warnFlickRange
                || frameTime % WARN_FLICK_SPEED < WARN_FLICK_SPEED / 2)) {
            this.getSpriteManager().drawSprite(g, "warn", warnPosition, GameWindow.GAME_SCREEN.getTileSize());
        }
    }
}

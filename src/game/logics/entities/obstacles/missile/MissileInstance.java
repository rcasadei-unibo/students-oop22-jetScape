package game.logics.entities.obstacles.missile;

import java.awt.Color;
import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.logics.entities.obstacles.generic.ObstacleInstance;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.hitbox.MissileHitbox;
import game.logics.interactions.SpeedHandler;
import game.utility.other.EntityType;
import game.utility.other.Pair;

public class MissileInstance extends ObstacleInstance implements Missile{

	/**
	 * Specifies the path within the sprite folder [specified in <code>Sprite</code> class]
	 * where <code>MissileInstance</code> sprites can be found.
	 */
	private static final String spritePath = "missile" + System.getProperty("file.separator");
	/**
	 * If sprites are missing, they will be replace by a rectangle of the color specified in
	 * <code>MissileInstance.placeH</code>.
	 */
	private static final Color placeH = Color.red;
	
	private enum Direction { UP, DOWN };
	
	/**
	 * The horizontal position of the missile warning.
	 */
	private final double warnDefaultX = screen.getWidth() - screen.getTileSize() * 1.5;
	/**
	 * The position of the missile warning is drawn.
	 */
	private Pair<Double,Double> warnPosition;
	
	/**
	 * The position when the warning should start flickering.
	 */
	private final double warnFlickRange = screen.getWidth() + screen.getTileSize() * 4;
	/**
	 * The flickering speed of the missile warning when a missile is about to appear.
	 */
	private final int warnFlickSpeed = 10;
	/**
	 * How many frames have passed since between a second and another.
	 */
	private int frameTime = 0;
	
	/**
	 * A reference to the player's position.
	 */
	private final Pair<Double,Double> playerPosition;
	
	private final double yStartSpeed = yDefaultSpeed;
	private double ySpeed = yStartSpeed;
	private double yAcceleration = yDefaultAcceleration;
	
	private double yBrakingDivider = 3.5;
	
	/**
	 * The direction the missile was moving.
	 */
	private Direction lastDir = Direction.UP;
	
	public MissileInstance(final Logics l, final Pair<Double,Double> pos, final Player player, final SpeedHandler speed) {
		super(l, pos, new SpeedHandler(speed.getXSpeed(), speed.getXSpeedIncDiff(), speed.getXAcceleration()));
		
		entityTag = EntityType.MISSILE;
		
		warnPosition = new Pair<>(warnDefaultX, position.getY());
		playerPosition = player.getPosition();
		
		spritesMgr.setPlaceH(placeH);
		spritesMgr.addSprite("warn", spritePath + "warn.png");
		spritesMgr.addSprite("missile", spritePath + "missile.png");
		spritesMgr.setAnimator(() -> "missile");
		this.hitbox = new MissileHitbox(pos, screen);
	}
	
	private void updateFrameTime() {
		frameTime++;
		if(frameTime >= GameWindow.fpsLimit) {
			frameTime = 0;
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		ySpeed = yStartSpeed;
	}
	
	@Override
	public void update() {
		super.update();
		updateFrameTime();
		
		if(this.isOnSpawnArea()) {
			if(position.getY() > playerPosition.getY()) {
				if(lastDir != Direction.UP) {
					ySpeed = -ySpeed / yBrakingDivider;
				}
				position.setY(position.getY() - ySpeed / GameWindow.fpsLimit);
				ySpeed += yAcceleration / GameWindow.fpsLimit;
				lastDir = Direction.UP;
			} else if(position.getY() < playerPosition.getY()) {
				if(lastDir != Direction.DOWN) {
					ySpeed = -ySpeed / yBrakingDivider;
				}
				position.setY(position.getY() + ySpeed / GameWindow.fpsLimit);
				ySpeed += yAcceleration / GameWindow.fpsLimit;
				lastDir = Direction.DOWN;
			}
		}
		warnPosition.setY(position.getY());
	}
	
	@Override
	public void draw(final Graphics2D g) {
		super.draw(g);
		if(position.getX() > screen.getWidth()) {
			if(position.getX() > warnFlickRange || frameTime % warnFlickSpeed < warnFlickSpeed / 2) {
				spritesMgr.drawSprite(g, "warn", warnPosition, screen.getTileSize());
			}
		}
	}
}

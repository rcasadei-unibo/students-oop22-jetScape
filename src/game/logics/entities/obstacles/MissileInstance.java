package game.logics.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.logics.entities.player.Player;
import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.other.Pair;

public class MissileInstance extends ObstacleInstance implements Missile{

	/**
	 * Specifies the path within the texture folder [specified in <code>Texture</code> class]
	 * where <code>MissileInstance</code> textures can be found.
	 */
	private static final String texturePath = "missile" + System.getProperty("file.separator");
	/**
	 * If textures are missing, they will be replace by a rectangle of the color specified in
	 * <code>MissileInstance.placeH</code>.
	 */
	private static final Color placeH = Color.red;
	
	private enum Direction { UP, DOWN };
	
	private final double warnDefaultX = screen.getWidth() - screen.getTileSize() * 1.5;
	private final int warnFlickering = 10;
	private int frameTime = 0;
	
	private final Pair<Double,Double> playerPosition;
	private Pair<Double,Double> warnPosition;
	
	private final double yStartSpeed = yDefaultSpeed;
	private double ySpeed = yStartSpeed;
	private double yAcceleration = yDefaultAcceleration;
	
	private Direction lastDir = Direction.UP;
	
	public MissileInstance(final Logics l, final Pair<Double,Double> pos, final Player player, final SpeedHandler speed) {
		super(l, pos, new SpeedHandler(speed.getXSpeed(), speed.getXSpeedIncDiff(), speed.getXAcceleration()));
		
		warnPosition = new Pair<>(warnDefaultX, position.getY());
		playerPosition = player.getPosition();
		
		textureMgr.setPlaceH(placeH);
		textureMgr.addTexture("warn", texturePath + "warn.png");
		textureMgr.addTexture("missile", texturePath + "missile.png");
		textureMgr.setAnimator(() -> {
			if(position.getX() > screen.getWidth()) {
				return "warn";
			}
			return "missile";
		});
	}
	
	private void updateFrameTime() {
		frameTime++;
		if(frameTime >= GameWindow.fpsLimit) {
			frameTime = 0;
		}
	}
	
	public void reset() {
		super.reset();
		ySpeed = yStartSpeed;
	}
	
	public void update() {
		super.update();
		updateFrameTime();
		
		if(!this.isOnScreenBounds()) {
			if(position.getY() > playerPosition.getY()) {
				if(lastDir != Direction.UP) {
					ySpeed = yStartSpeed;
				}
				position.setY(position.getY() - ySpeed / GameWindow.fpsLimit);
				ySpeed += yAcceleration / GameWindow.fpsLimit;
				lastDir = Direction.UP;
			} else if(position.getY() <= playerPosition.getY()) {
				if(lastDir != Direction.DOWN) {
					ySpeed = yStartSpeed;
				}
				position.setY(position.getY() + ySpeed / GameWindow.fpsLimit);
				ySpeed += yAcceleration / GameWindow.fpsLimit;
				lastDir = Direction.DOWN;
			}
		}
		warnPosition.setY(position.getY());
	}
	
	public void draw(final Graphics2D g) {
		super.draw(g);
		if(position.getX() > screen.getWidth()) {
			if(position.getX() > screen.getWidth() + screen.getTileSize() * 4 || frameTime % warnFlickering < warnFlickering / 2) {
				textureMgr.drawTexture(g, warnPosition, screen.getTileSize());
			}
		}
	}
}

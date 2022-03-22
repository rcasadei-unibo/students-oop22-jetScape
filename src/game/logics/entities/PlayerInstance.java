package game.logics.entities;

import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Pair;

public class PlayerInstance extends EntityInstance implements Player{
	
	private final double fallSpeed;
	private final double jumpSpeed;
	
	private final double yGround;
	private final double yRoof;

	private double jumpMultiplier = initialJumpMultiplier;
	private double fallMultiplier = initialFallMultiplier;
	private KeyHandler keyH;
	private String action;
	
	public PlayerInstance(final GameWindow g) {
		super(g);
		this.keyH = g.getKeyHandler();
		this.fps = GameWindow.fpsLimiter;
		
		fallSpeed = baseFallSpeed / fps;
		jumpSpeed = baseJumpSpeed / fps;
		yGround = screen.getHeight() - (yLowLimit + screen.getTileSize());
		yRoof = yTopLimit + screen.getTileSize();

		position = new Pair<>(xPosition, yGround);
		action = "idle";
		entityTag = "player";
	}
	
	private void jump() {
		position.setY(position.getY() - jumpSpeed * jumpMultiplier > yRoof ? position.getY() - jumpSpeed * jumpMultiplier : yRoof);
		action = "jump";
	}
	
	private void fall() {
		if(position.getY() + fallSpeed * fallMultiplier < yGround) {
			position.setY(position.getY() + fallSpeed * fallMultiplier);
			action = "fall";
		} else {
			position.setY(yGround);
			action = "idle";
		}
	}

	@Override
	public void update() {
		if(keyH.spacePressed) {
			jump();
			jumpMultiplier += jumpMultiplierIncrease;
			fallMultiplier = initialFallMultiplier;
		} else if(action != "idle") {
			fall();
			fallMultiplier += fallMultiplierIncrease;
			jumpMultiplier = initialJumpMultiplier;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.fillRect((int)Math.round(position.getX()), (int)Math.round(position.getY()), screen.getTileSize(), screen.getTileSize());
	}
}

package game.logics.entities.player;

import java.awt.Graphics2D;
import java.awt.Color;

import game.logics.entities.basic.EntityInstance;
import game.logics.handler.Logics;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.Pair;
import game.utility.textures.Texture;

public class PlayerInstance extends EntityInstance implements Player{
	
	private final double fallSpeed;
	private final double jumpSpeed;

	private double jumpMultiplier = initialJumpMultiplier;
	private double fallMultiplier = initialFallMultiplier;
	private final Texture texture = new Texture("player", Color.white);
	private final KeyHandler keyH;
	private String action;
	
	
	public PlayerInstance(final Logics l) {
		super(l);
		this.keyH = l.getKeyHandler();
		
		fallSpeed = baseFallSpeed / fps;
		jumpSpeed = baseJumpSpeed / fps;

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
		super.update();
		if(keyH.input.get("spacebar")) {
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
		texture.draw(g, position, screen.getTileSize());
		super.draw(g);
	}
}

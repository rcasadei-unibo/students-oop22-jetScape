package game.logics.entities;

import java.awt.Graphics2D;
import java.awt.Color;

import game.logics.handler.Logics;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Pair;

public class PlayerInstance extends EntityInstance implements Player{
	
	private final double fallSpeed;
	private final double jumpSpeed;

	private double jumpMultiplier = initialJumpMultiplier;
	private double fallMultiplier = initialFallMultiplier;
	private KeyHandler keyH;
	private String action;
	
	private Color texture = Color.white;
	
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
		g.setColor(texture);
		super.draw(g);
	}
}

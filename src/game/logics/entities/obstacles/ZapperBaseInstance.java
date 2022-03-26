package game.logics.entities.obstacles;

import java.awt.Color;
import java.awt.Graphics2D;

import game.logics.handler.Logics;
import game.logics.interactions.SpeedHandler;
import game.utility.screen.Pair;

public class ZapperBaseInstance extends ObstacleInstance implements ZapperBase{

	private ZapperBase pairedBase;
	
	//private String rotation = "up";
	private Color texture = Color.gray;
	private boolean paired = false;
	
	ZapperBaseInstance(final Logics l, final Pair<Double,Double> position){
		super(l, position);
		entityTag = "zapper-base";
	}
	
	public ZapperBaseInstance(final Logics l, final Pair<Double,Double> position, final SpeedHandler s) {
		this(l, position);
		
		this.movement = s;
	}
	
	public ZapperBaseInstance(final Logics l, final Pair<Double,Double> position, final ZapperBase pair) {
		this(l, position);
		
		this.setPaired(pair);
		
		this.movement = pair.getSpeedHandler();
	}
	
//	private void updateRotation() {
//		if(this.getX() == pairedBase.getX()) {
//			if(this.getY() < pairedBase.getY()) {
//				this.setRotation("down");
//			} else {
//				this.setRotation("up");
//			}
//		} else if(this.getY() == pairedBase.getY()) {
//			if(this.getX() > pairedBase.getX()) {
//				this.setRotation("left");
//			} else {
//				this.setRotation("right");
//			}
//		} else if(this.getX() > pairedBase.getX() && this.getY() < pairedBase.getY()) {
//			this.setRotation("down-left");
//		} else if(this.getX() < pairedBase.getX() && this.getY() > pairedBase.getY()){
//			this.setRotation("up-right");
//		} else if(this.getX() < pairedBase.getX() && this.getY() < pairedBase.getY()) {
//			this.setRotation("down-right");
//		} else if(this.getX() > pairedBase.getX() && this.getY() > pairedBase.getY()) {
//			this.setRotation("up-left");
//		} else {
//			this.setRotation("up");
//		}
//	}
	
	public ZapperBase getPaired() {
		return this.pairedBase;
	}
	
	public void setPaired(final ZapperBase zap) {
		if(!this.paired) {
			this.pairedBase = zap;
			this.paired = true;
			zap.setPaired(this);
		}
	}
	
//	public void setRotation(final String rotation) {
//		switch(rotation.toLowerCase()) {
//			case "up":
//				this.rotation = "up";
//				break;
//			case "down":
//				this.rotation = "down";
//				break;
//			case "left":
//				this.rotation = "left";
//				break;
//			case "right":
//				this.rotation = "right";
//				break;
//			case "up-left":
//			case "upper-left":
//			case "upleft":
//			case "upperleft":
//				this.rotation = "up-left";
//				break;
//			case "up-right":
//			case "upper-right":
//			case "upright":
//			case "upperright":
//				this.rotation = "up-right";
//				break;
//			case "down-left":
//			case "lower-left":
//			case "downleft":
//			case "lowerleft":
//				this.rotation = "down-left";
//				break;
//			case "down-right":
//			case "lower-right":
//			case "downright":
//			case "lowerright":
//				this.rotation = "down-right";
//				break;
//		}
//		
//	}
	
	public void update() {
		super.update();
		//updateRotation();
		
		if(position.getX() > -screen.getTileSize()) {
			position.setX(position.getX() - movement.getXSpeed() / fps);
		} 
//		else {
//			position.setX((double)screen.getWidth());
//			position.setY(yRoof + (yGround - yRoof) * r.nextDouble());
//			xspeed += xspeedIncDifficulty;
//			//this.setVisibility(false);
//		}
	}
	
	public void draw(final Graphics2D g) {
		g.setColor(texture);
		super.draw(g);
	}

}

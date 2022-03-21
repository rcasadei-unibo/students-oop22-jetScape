package game.logics.entities;

import game.utility.screen.Pair;
import java.awt.Graphics2D;


public abstract class EntityInstance implements Entity{
	
	protected int screenWidth, screenHeight, tileSize;
	protected Pair<Double, Double> position;
	protected String entityTag;
	
	EntityInstance(final int width, final int height, final int tileSize) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.tileSize = tileSize;
		position = new Pair<>(-1.0,-1.0);
		entityTag = "undefined";
	}
		
	public abstract void update();
	
	public abstract void draw(Graphics2D g);
	
	public double getX(){
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public String entityType() {
		return entityTag;
	}
	
}

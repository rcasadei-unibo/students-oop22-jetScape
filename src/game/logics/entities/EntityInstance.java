package game.logics.entities;

import game.frame.GameWindow;
import game.utility.screen.Pair;
import game.utility.screen.Screen;

import java.awt.Graphics2D;


public abstract class EntityInstance implements Entity{
	
	protected int fps;
	protected Screen screen;
	protected Pair<Double, Double> position;
	protected String entityTag;
	
	EntityInstance(final GameWindow g) {
		this.screen = g.getScreenInfo();
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

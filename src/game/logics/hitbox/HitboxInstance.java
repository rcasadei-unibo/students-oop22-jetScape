package game.logics.hitbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.awt.Graphics2D;
import java.awt.Color;


import game.utility.other.Pair;
import game.utility.screen.Screen;

import java.awt.Rectangle;

public abstract class HitboxInstance implements Hitbox{
	static final int spriteDimensions = 32;
	protected final Map<Rectangle, Pair<Integer,Integer>> hitboxes;
	private final Pair<Double,Double> startingPos;
	private final  Screen gScreen;
	private Pair<Double,Double> lastPos;
	
	
	public HitboxInstance(Pair<Double, Double> startingPos,	Screen gScreen) {
		super();
		this.hitboxes = new HashMap<>();
		this.startingPos = startingPos;
		this.lastPos = startingPos;
		this.gScreen = gScreen;
	}

	public void updatePosition(Pair<Double,Double> newPos) {
		var shift = getShift(newPos);
		hitboxes.keySet().forEach(hitbox -> {
			hitbox.translate(shift.getX(), shift.getY());
		});
		this.lastPos = newPos;
	}
	
	public boolean collides(Hitbox entity) {
		for(Rectangle hitbox : this.hitboxes.keySet()) {
			for(Rectangle hitboxTarget : entity.getRectangles()) {
				if(hitbox.intersects(hitboxTarget)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Set<Rectangle> getRectangles() {
		return this.hitboxes.keySet();
	}
	
	public void resetPosition() {
		this.hitboxes.forEach((hitbox, startPos) -> {
			hitbox.setLocation(startPos.getX(),startPos.getY());
		});
	}
	
	protected void addRectangle(double x, double y, double width, double height) {
		int rectangleX = (int) (startingPos.getX() + (gScreen.getTileSize()* (x/spriteDimensions)));
		int rectangleY = (int) (startingPos.getY() + (gScreen.getTileSize()* (y/spriteDimensions)));
		int rectangleWidth  = (int) (gScreen.getTileSize()* (width/spriteDimensions));
		int rectangleHeight = (int) (gScreen.getTileSize() * (height/spriteDimensions));
		this.hitboxes.put(new Rectangle(rectangleX,rectangleY,rectangleWidth,rectangleHeight),
				new Pair<>(rectangleX, rectangleY));
	}
	
	
	private Pair<Integer,Integer> getShift(Pair<Double,Double> newPos){
		int xShift = (int) (newPos.getX() - this.lastPos.getX());
		int yShift = (int) (newPos.getY() - this.lastPos.getY());
		return new Pair<>(xShift, yShift);		
	}
	
	public void draw(Graphics2D g) {
		this.hitboxes.forEach((hitbox, startPos) -> {
			g.setColor(Color.MAGENTA);
			g.draw(hitbox);
		});
	}
	
}

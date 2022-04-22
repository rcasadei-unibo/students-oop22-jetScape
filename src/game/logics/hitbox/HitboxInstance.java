package game.logics.hitbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import game.utility.other.Pair;
import game.utility.screen.Screen;

import java.awt.Rectangle;

public abstract class HitboxInstance implements Hitbox{
	static final int spriteDimensions = 32;
	protected final Map<Rectangle, Pair<Integer,Integer>> hitboxes;
	private final Pair<Double,Double> startingPos;
	private final  Screen gScreen;
	
	
	public HitboxInstance(Pair<Double, Double> startingPos,	Screen gScreen) {
		super();
		this.hitboxes = new HashMap<>();
		this.startingPos = startingPos;
		this.gScreen = gScreen;
	}

	public void updatePosition(int xShift, int yShift) {
		hitboxes.keySet().forEach(hitbox -> {
			hitbox.translate(xShift, yShift);
		});
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
		this.hitboxes.keySet().forEach(hitbox -> {
			var startingPoint = this.hitboxes.get(hitbox);
			hitbox.setLocation(startingPoint.getX(), startingPoint.getY());
		});
	}
	
	protected void addRectangle(double xProportion, double yProportion, double hProportion, double wProportion) {
		int rectangleX = (int) (startingPos.getX() + gScreen.getTileSize()* xProportion);
		int rectangleY = (int) (startingPos.getY() - gScreen.getTileSize()* yProportion);
		int rectangleWidth  = (int) (gScreen.getTileSize()* wProportion);
		int rectangleHeight = (int) (gScreen.getTileSize() * hProportion);
		this.hitboxes.put(new Rectangle(rectangleX,rectangleY,rectangleWidth,rectangleHeight),
				new Pair<>(rectangleWidth, rectangleHeight));
	}
	
}

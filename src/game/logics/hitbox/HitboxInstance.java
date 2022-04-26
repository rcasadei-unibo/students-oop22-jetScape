package game.logics.hitbox;

import java.util.Collections;
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
	protected final Map<Rectangle, Pair<Double,Double>> hitboxes;
	private final Pair<Double,Double> startingPos;
	private final  Screen gScreen;
	
	
	public HitboxInstance(Pair<Double, Double> startingPos,	Screen gScreen) {
		super();
		this.hitboxes = new HashMap<>();
		this.startingPos = new Pair<>(startingPos.getX(),startingPos.getY());
		this.gScreen = gScreen;
	}

	public void updatePosition(Pair<Double,Double> newPos) {
		this.hitboxes.forEach((hitbox,scale) -> {
			hitbox.setLocation((int) (newPos.getX()+ scale.getX()),
					(int) (newPos.getY()+ scale.getY()));
		});
	}
	
	public Set<Rectangle> getRectangles() {
		return Collections.unmodifiableSet(this.hitboxes.keySet());
	}
	
	public void draw(Graphics2D g) {
		this.hitboxes.forEach((hitbox, startPos) -> {
			g.setColor(Color.MAGENTA);
			g.draw(hitbox);
		});
	}
	
	protected void addRectangle(double x, double y, double width, double height) {
		int startingX = (int) (startingPos.getX() + this.scale(x));
		int startingY = (int) (startingPos.getY() + this.scale(y));
		int scaledWidth  = (int) this.scale(width);
		int scaledHeight = (int) this.scale(height);
		this.hitboxes.put(new Rectangle(startingX,startingY,scaledWidth,scaledHeight),
				new Pair<>(this.scale(x), this.scale(y)));
	}
	
	private double scale (double x) {
		return (gScreen.getTileSize()* (x/spriteDimensions));
	}
}

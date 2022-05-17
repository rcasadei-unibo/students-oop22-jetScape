package game.logics.hitbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

import game.frame.GameWindow;
import game.utility.debug.Debugger;
import game.utility.other.Pair;

public abstract class HitboxInstance implements Hitbox{
	static final int spriteDimensions = 32;
	private final Map<Rectangle, Pair<Double,Double>> hitboxes;
	private final Pair<Double,Double> startingPos;
	protected final Set<Rectangle> rectangles;
	
	public HitboxInstance(Pair<Double, Double> startingPos) {
		super();
		this.hitboxes = new HashMap<>();
		this.startingPos = new Pair<>(startingPos.getX(),startingPos.getY());
		this.rectangles = new HashSet<>();
	}

	public void updatePosition(Pair<Double,Double> newPos) {
		this.hitboxes.forEach((hitbox,scale) -> {
			hitbox.setLocation((int) (newPos.getX()+ scale.getX()),
					(int) (newPos.getY()+ scale.getY()));
		});
	}
	
	public Set<Rectangle> getRectangles() {
		return Collections.unmodifiableSet(this.rectangles);
	}
	
	public void draw(Graphics2D g) {
		if(GameWindow.debugger.isFeatureEnabled(Debugger.Option.HITBOX)) {
			this.rectangles.forEach(hitbox -> {
				g.setColor(Color.MAGENTA);
				g.draw(hitbox);
			});
		}
	}
	
	protected void addRectangle(double x, double y, double width, double height) {
		int startingX = (int) (startingPos.getX() + this.scale(x));
		int startingY = (int) (startingPos.getY() + this.scale(y));
		int scaledWidth  = (int) this.scale(width);
		int scaledHeight = (int) this.scale(height);
		this.hitboxes.put(new Rectangle(startingX,startingY,scaledWidth,scaledHeight),
				new Pair<>(this.scale(x), this.scale(y)));
		this.rectangles.addAll(this.hitboxes.keySet());
	}
	
	private double scale (double x) {
		return (GameWindow.gameScreen.getTileSize()* (x/spriteDimensions));
	}
}

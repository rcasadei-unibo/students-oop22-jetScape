package game.logics.hitbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import game.utility.other.Pair;

import java.awt.Rectangle;

public abstract class HitboxInstance implements Hitbox{
	private final Map<Rectangle, Pair<Integer,Integer>> hitboxes;
	
	public HitboxInstance() {
		super();
		this.hitboxes = new HashMap<>();
	}

	public void updatePosition(int xShift, int yShift) {
		hitboxes.keySet().forEach(hitbox -> {
			hitbox.translate(xShift, yShift);
		});
	}
	
	public boolean collides(HitboxInstance entity) {
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
	
}

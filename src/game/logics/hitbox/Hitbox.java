package game.logics.hitbox;

import java.util.Set;
import java.awt.Rectangle;

public class Hitbox {
	private Set<Rectangle> hitboxes;
	
	public void updatePosition(int xShift, int yShift) {
		hitboxes.forEach(hitbox -> {
			hitbox.translate(xShift, yShift);
		});
	}
	
	public boolean collides(Hitbox entity) {
		for(Rectangle hitbox : this.hitboxes) {
			for(Rectangle hitboxTarget : entity.getRectangles()) {
				if(hitbox.intersects(hitboxTarget)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Set<Rectangle> getRectangles() {
		return this.hitboxes;
	}
	
}

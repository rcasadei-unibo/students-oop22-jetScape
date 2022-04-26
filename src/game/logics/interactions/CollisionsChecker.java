package game.logics.interactions;

import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import game.logics.entities.generic.Entity;
import game.logics.entities.player.PlayerInstance;
import game.logics.hitbox.Hitbox;

public class CollisionsChecker {
	private final Map<String, Set<Entity>> entities;
	private final Queue<Entity> collisions ;
	private final Hitbox player;
	
	public CollisionsChecker(Map<String, Set<Entity>> entities, PlayerInstance p) {
		super();
		this.entities = entities;
		this.collisions = new PriorityQueue<>();
		this.player = p.getHitbox();
	}
	
	public Optional<Entity> getNextToHandle() {
		return Optional.ofNullable(this.collisions.poll());
	}

	public void updateCollisions() {
		this.entities.forEach((type, entities) -> {
			if(!type.equals("player")) {
				entities.forEach(entity -> {
					this.collides(entity.getHitbox());
				});
			}		
		});
	}
	
	private void collides(Hitbox entity) {
		this.player.getRectangles().forEach(hitbox -> {
			entity.getRectangles().forEach(target -> {
				if (hitbox.intersects(target)) {
					this.collisions.add((Entity) entity);
					return;
				}
			});
		});
	}
	
	

}

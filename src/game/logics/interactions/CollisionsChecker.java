package game.logics.interactions;

import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import game.logics.entities.generic.Entity;
import game.logics.entities.player.PlayerInstance;
import game.logics.hitbox.Hitbox;
import game.utility.other.EntityType;

public class CollisionsChecker {
	private final Map<EntityType, Set<Entity>> entities;
	private final Queue<Entity> collisions ;
	private final Hitbox player;
	
	public CollisionsChecker(Map<EntityType, Set<Entity>> entities, PlayerInstance p) {
		super();
		this.entities = entities;
		this.collisions = new PriorityQueue<>();
		this.player = p.getHitbox().stream().findFirst().get();
	}
	
	public Optional<Entity> getNextToHandle() {
		return Optional.ofNullable(this.collisions.poll());
	}

	public void updateCollisions() {
		this.entities.forEach((type, entities) -> {
			if(!type.equals(EntityType.PLAYER)) {
				entities.forEach(entity -> {
					entity.getHitbox().forEach(hitbox -> this.collides(hitbox));
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

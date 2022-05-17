package game.logics.interactions;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.Rectangle;

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
		this.collisions = new  LinkedBlockingQueue<>();
		this.player = p.getHitbox();
	}
	
	public Optional<Entity> getNextToHandle() {
		return Optional.ofNullable(this.collisions.poll());
	}

	public void updateCollisions() {
		this.entities.forEach((type, entities) -> {
			if(!type.equals(EntityType.PLAYER)) {
				entities.forEach(entity -> {
					if(this.collides(entity.getHitbox())) {
						this.collisions.add(entity);
					}
				});
			}		
		});
	}
	
	private boolean collides(Hitbox entity) {
		for(Rectangle hitbox : player.getRectangles()) {
			for(Rectangle target : entity.getRectangles()) {
				if (hitbox.intersects(target)) {
					return true;
				}
			}
		}
		return false;
	}
	
	

}

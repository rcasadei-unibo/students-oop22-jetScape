package game.logics.interactions;

import java.util.Map;
import java.util.Set;

import game.logics.entities.generic.Entity;
import game.logics.entities.player.PlayerInstance;
import game.utility.other.EntityType;

public class CollisionsHandler {
	private final CollisionsChecker cChecker; 
	
	public CollisionsHandler(Map<EntityType, Set<Entity>> entities, PlayerInstance p){
		this.cChecker = new CollisionsChecker(entities, p);
	}
	
	public void interact() {
		this.cChecker.updateCollisions();
		var entity = this.cChecker.getNextToHandle();
		while (entity.isPresent()) {
			//TODO use different entities handling method
			System.out.println("Colpito! "+ entity.get().entityType());
			entity = this.cChecker.getNextToHandle();
		}
	}
	
	
}

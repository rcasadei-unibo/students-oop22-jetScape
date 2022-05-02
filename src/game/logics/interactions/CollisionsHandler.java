package game.logics.interactions;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.player.PlayerInstance;
import game.utility.debug.Debugger;
import game.utility.other.EntityType;

public class CollisionsHandler {
	private final CollisionsChecker cChecker; 
	
	public CollisionsHandler(Map<EntityType, Set<Entity>> entities, PlayerInstance p){
		this.cChecker = new CollisionsChecker(entities, p);
	}
	
	public void interact(final Consumer<Entity> action) {
		this.cChecker.updateCollisions();
		var entity = this.cChecker.getNextToHandle();
		while (entity.isPresent()) {
			action.accept(entity.get());
			GameWindow.debugger.printLog(Debugger.Option.LOG_HITCHECK, "Colpito! "+ entity.get().entityType());
			entity = this.cChecker.getNextToHandle();
		}
	}
	
	
}

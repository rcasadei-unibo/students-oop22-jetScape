package game.logics.interactions;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.player.PlayerInstance;
import game.utility.debug.Debugger;
import game.utility.other.EntityType;

/**
 * The {@link CollisionsHandler} class helps {@link game.logics.handler.LogicsHandler LogicsHandler} to manage
 * collision between player and various entities.
 */ 
public class CollisionsHandler {
    private final CollisionsChecker cChecker; 

    /**
     *  Instantiates {@link CollisionsChecker} between {@link PlayerInstance} p 
     *  and {@link Entity} to keep track of collisions.
     * @param entities
     * @param p
     */
    public CollisionsHandler(final Map<EntityType, Set<Entity>> entities, final PlayerInstance p) {
        this.cChecker = new CollisionsChecker(entities, p);
    }

    /**
     * if some kind of collision happens, performs an action given the entity hit .
     * @param action takes the entity hit and performs the correct action
     */
    public void interact(final Consumer<Entity> action) {
        this.cChecker.updateCollisions();
        var entity = this.cChecker.getNextToHandle();
        while (entity.isPresent()) {
            action.accept(entity.get());
            GameWindow.GAME_DEBUGGER.printLog(Debugger.Option.LOG_HITCHECK, "Colpito! " + entity.get().entityType());
            entity = this.cChecker.getNextToHandle();
        }
    }
}

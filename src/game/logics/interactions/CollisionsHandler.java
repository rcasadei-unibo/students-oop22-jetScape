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
 * The {@link CollisionsHandler} class helps {@link LogicsHandler} to manage
 * collision between player and various entities.
 */ 
public class CollisionsHandler {
    private final CollisionsChecker cChecker; 

    // TODO: Add javadoc
    /**
     * .
     * @param entities
     * @param p
     */
    public CollisionsHandler(final Map<EntityType, Set<Entity>> entities, final PlayerInstance p) {
        this.cChecker = new CollisionsChecker(entities, p);
    }

    // TODO: complete action parameter
    /**
     * if some kind of collision happens, starts the procedure to handle it .
     * @param action
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

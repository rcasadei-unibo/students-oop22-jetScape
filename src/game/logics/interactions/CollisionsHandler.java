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
 * The <code>CollisionsHandler</code> class helps <class>LogicsHandler</class> to manage
 * collision between player and various entities
 *  
 * @author Giacomo Amadio
 */ 
public class CollisionsHandler {
    private final CollisionsChecker cChecker; 
    
    public CollisionsHandler(Map<EntityType, Set<Entity>> entities, PlayerInstance p){
        this.cChecker = new CollisionsChecker(entities, p);
    }
    
    /**
     * if some kind of collision happens, starts the procedure to handle it 
     */
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

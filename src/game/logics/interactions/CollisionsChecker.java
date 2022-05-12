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

/**
 * The <code>CollisionsChecker</code> class helps <class>CollisionsHandler</class> to detect
 * collisions between player and other entities
 *  
 * @author Giacomo Amadio
 */ 
public class CollisionsChecker {
    private final Map<EntityType, Set<Entity>> entities;
    private final Hitbox player;
    /**
     * collisions queue of hits to handle
     */
    private final Queue<Entity> collisions ;
    
    public CollisionsChecker(Map<EntityType, Set<Entity>> entities, PlayerInstance p) {
        super();
        this.entities = entities;
        this.collisions = new  LinkedBlockingQueue<>();
        this.player = p.getHitbox().stream().findFirst().get();
    }
    
    /**
     * @return Optional of the fist entity hit else Optional empty 
     */
    public Optional<Entity> getNextToHandle() {
        return Optional.ofNullable(this.collisions.poll());
    }

    /**
     * if there are some contacts, adds in the collisions queue all 
     * the entities that are touching the player 
     */
    public void updateCollisions() {
        this.entities.forEach((type, entities) -> {
            if(!type.equals(EntityType.PLAYER)) {
                entities.forEach(entity -> {
                    entity.getHitbox().forEach(hitbox -> {
                        if(this.collides(hitbox)) {
                            this.collisions.add(entity);
                        }
                    });
                });
            }        
        });
    }
    
    /**
     * @param Hitbox entity
     * 
     * @return true if the entity hitbox collides with the player ones  
     */
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

package game.logics.interactions;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import game.logics.entities.generic.Entity;
import game.logics.entities.player.PlayerInstance;
import game.logics.hitbox.Hitbox;
import game.utility.other.GameState;

public class CollisionsChecker {
	private final Map<String, Set<Entity>> entities;
	private final Consumer<GameState> setGameState;
	private final Hitbox player;
	
	public CollisionsChecker(Map<String, Set<Entity>> entities, 
			Consumer<GameState> setGameState, PlayerInstance p) {
		super();
		this.entities = entities;
		this.setGameState = setGameState;
		this.player = p.getHitbox();
	}

	public void checkCollision() {
		this.entities.keySet().forEach(entity -> {
			if(!entity.equals("player")) {
				this.entities.get(entity).forEach(obstacle -> {
					if(obstacle.getHitbox().collides(player)) {
						setGameState.accept(GameState.MENU);
						return;
					}
				});
			}	
		});
	}
		
}

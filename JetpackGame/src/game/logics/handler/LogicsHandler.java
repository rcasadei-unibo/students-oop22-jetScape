package game.logics.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.logics.entities.Entity;
import game.logics.entities.PlayerInstance;

public class LogicsHandler implements Logics{
	
	private GameWindow gScreen;
	private Map<String, Set<Entity>> entities = new HashMap<>();
	
	public LogicsHandler(final GameWindow g) {
		this.gScreen = g;
		entities.put("player", new HashSet<>());
		entities.get("player").add(new PlayerInstance(gScreen));
	}
	
	public void updateAll() {
		entities.forEach((s, se) -> se.forEach(e -> e.update()));
	}
	
	public void drawAll(final Graphics2D g) {
		entities.forEach((s, se) -> se.forEach(e -> e.draw(g)));
	}
}

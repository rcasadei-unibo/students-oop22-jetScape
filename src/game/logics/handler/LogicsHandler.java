package game.logics.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.awt.Graphics2D;

import game.logics.entities.*;
import game.logics.interactions.SpeedHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Pair;
import game.utility.screen.Screen;

public class LogicsHandler implements Logics{
	
	private final Map<String, Set<Entity>> entities = new HashMap<>();
	
	private final Screen gScreen;
	private final KeyHandler keyH;
	
	private final Pair<Double,Double> obstaclesPos;
	
	private boolean debug;
	
	public LogicsHandler(final Screen screen, final KeyHandler keyH, final boolean debug) {
		this.gScreen = screen;
		this.keyH = keyH;
		this.debug = debug;
		
		obstaclesPos = new Pair<>(500.0, (double)gScreen.getTileSize());
		
		entities.put("player", new HashSet<>());
//		entities.put("zappers", new HashSet<>());
		entities.get("player").add(new PlayerInstance(this));
		
//		ZapperBase z1 = new ZapperBaseInstance(this, new Pair<>(obstaclesPos.getX(), obstaclesPos.getY()), new SpeedHandler());
//		entities.get("zappers").add(z1);
//		obstaclesPos.setY(obstaclesPos.getY() + gScreen.getTileSize() * 2);
//		ZapperBase z2 = new ZapperBaseInstance(this, new Pair<>(obstaclesPos.getX(), obstaclesPos.getY()), z1);
//		entities.get("zappers").add(z2);
//		obstaclesPos.setY(obstaclesPos.getY() - gScreen.getTileSize());
//		entities.get("zappers").add(new ZapperRayInstance(this, new Pair<>(obstaclesPos.getX(), obstaclesPos.getY()), z1, z2));
	}
	
	public Pair<Double,Double> getObstaclesPos(){
		return obstaclesPos;
	}
	
	public Screen getScreenInfo() {
		return gScreen;
	}
	
	public KeyHandler getKeyHandler() {
		return keyH;
	}
	
	public boolean isDebugModeOn() {
		return debug;
	}
	
	public void updateAll() {
		entities.forEach((s, se) -> se.forEach(e -> e.update()));
	}
	
	public void drawAll(final Graphics2D g) {
		entities.forEach((s, se) -> se.forEach(e -> e.draw(g)));
	}
}

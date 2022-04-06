package game.logics.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.awt.Graphics2D;

import java.lang.Runnable;

import game.logics.entities.basic.*;
import game.logics.entities.obstacles.Obstacle;
import game.logics.entities.obstacles.ZapperBaseInstance;
import game.logics.entities.obstacles.ZapperRayInstance;
import game.logics.entities.player.PlayerInstance;
import game.logics.interactions.Generator;
import game.logics.interactions.SpeedHandler;
import game.logics.interactions.TileGenerator;
import game.utility.debug.Debugger;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Screen;
import game.utility.other.GameState;
import game.display.DisplayController;

public class LogicsHandler implements Logics{
	
	private final Map<String, Set<Entity>> entities = new HashMap<>();
	
	private final Screen gScreen;
	private final KeyHandler keyH;
	private final Generator spawner;
	private final DisplayController displayController = new DisplayController();;
	
	//private final Pair<Double,Double> obstaclesPos;
	
	private long updateTimer = System.nanoTime();
	private int spawnInterval = 3;
	private Debugger debugger;
	private GameState gs = GameState.MENU;
	
	public LogicsHandler(final Screen screen, final KeyHandler keyH, final Debugger debugger) {
		this.gScreen = screen;
		this.keyH = keyH;
		this.debugger = debugger;
		
		//obstaclesPos = new Pair<>(500.0, (double)gScreen.getTileSize());
		
		entities.put("player", new HashSet<>());
		entities.put("zappers", new HashSet<>());
		entities.get("player").add(new PlayerInstance(this));
		
//		spawner = new TileGenerator((type, set) -> entities.get(type).addAll(set), 3);
		spawner = new TileGenerator(entities, spawnInterval);
		spawner.setZapperBaseCreator(p -> new ZapperBaseInstance(this, p, new SpeedHandler()));
		spawner.setZapperRayCreator((b,p) -> new ZapperRayInstance(this, p, b.getX(), b.getY()));
		
		spawner.initialize();
		
//		ZapperBase z1 = new ZapperBaseInstance(this, new Pair<>(obstaclesPos.getX(), obstaclesPos.getY()), new SpeedHandler());
//		entities.get("zappers").add(z1);
//		obstaclesPos.setY(obstaclesPos.getY() + gScreen.getTileSize() * 2);
//		ZapperBase z2 = new ZapperBaseInstance(this, new Pair<>(obstaclesPos.getX(), obstaclesPos.getY()), z1);
//		entities.get("zappers").add(z2);
//		obstaclesPos.setY(obstaclesPos.getY() - gScreen.getTileSize());
//		entities.get("zappers").add(new ZapperRayInstance(this, new Pair<>(obstaclesPos.getX(), obstaclesPos.getY()), z1, z2));
		
	}	
	
	public Screen getScreenInfo() {
		return gScreen;
	}
	
	public KeyHandler getKeyHandler() {
		return keyH;
	}
	
	public Debugger getDebugger() {
		return debugger;
	}
	
	private void cleanEntities() {
		entities.get("zappers").removeIf(e -> {
			Obstacle o = (Obstacle)e;
			if(o.isOutofScreen()) {
				o.resetPosition();
				if(debugger.isFeatureEnabled("log: entities cleaner working")) {
					System.out.println("reset");
				}
			}
			return o.isOutofScreen();
		});
	}
	
	private void checkDebugMode() {
		if(keyH.input.get("z")) {
			debugger.setDebugMode(true);
		} else if(keyH.input.get("x")) {
			debugger.setDebugMode(false);
		}
	}
	
	private boolean updateEachInterval(final long interval, final long timeStart, final Runnable r) {
		long timePassed = System.nanoTime() - timeStart;
		if(timePassed >= interval) {
			r.run();
			return true;
		}
		return false;
	}
	
	public void updateAll() {
		if(isInGame()) {
			if(updateEachInterval(2 * 1000000000, updateTimer, () -> cleanEntities())) {
				updateTimer = System.nanoTime();
				if(debugger.isFeatureEnabled("log: entities cleaner check")) {
					System.out.println("clean");
				}
			}
			checkDebugMode();
			entities.forEach((s, se) -> se.forEach(e -> e.update()));
		}
	}
	
	public void drawAll(final Graphics2D g) {
		if (isInGame()) {
			entities.forEach((s, se) -> se.forEach(e -> e.draw(g)));
		}
		this.displayController.displayScreen(g, gs);
	}

	public void setGameState(GameState gs) {
		this.gs = gs;
	}
	
	private boolean isInGame() {
		return this.gs == GameState.INGAME;
	}
	
}

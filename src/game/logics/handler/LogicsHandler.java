package game.logics.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.json.simple.parser.ParseException;

import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.event.KeyEvent;

import java.io.FileNotFoundException;
import java.io.IOException;

import game.frame.GameHandler;
import game.frame.GameWindow;
import game.logics.entities.generic.Entity;
import game.logics.entities.obstacles.missile.MissileInstance;
import game.logics.entities.obstacles.zapper.ZapperBaseInstance;
import game.logics.entities.obstacles.zapper.ZapperRayInstance;
import game.logics.entities.player.Player;
import game.logics.entities.player.PlayerInstance;
import game.logics.display.controller.DisplayController;
import game.logics.generator.Generator;
import game.logics.generator.TileGenerator;
import game.logics.interactions.SpeedHandler;
import game.utility.debug.Debugger;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Screen;
import game.utility.other.EntityType;
import game.utility.other.GameState;

/**
 * The <code>LogicsHandler</code> class helps <class>GameWindow</class> to update
 * and draw logical parts of the game like the Interface, Entities, Collisions, etc....
 * 
 * @author Daniel Pellanda
 */
public class LogicsHandler implements Logics{
	
	/**
	 * Contains the current active entities on the game environment.
	 */
	private final Map<EntityType, Set<Entity>> entities = new HashMap<>();
	
	/**
	 * Generates sets of obstacles on the environment.
	 */
	private final Screen screen;
	private final KeyHandler keyH;
	
	private final Generator spawner;
	
	private final DisplayController displayController;
	
	private GameState gameState = GameState.MENU;
	
	/**
	 * A reference to the player's entity.
	 */
	private final Player playerEntity;
	
	/**
	 * Defines how many seconds have to pass for the spawner to generate
	 * another set of obstacles.
	 */
	private int spawnInterval = 2;
	/**
	 * Defines the interval of each check for entities to clean.
	 */
	private int cleanInterval = 5;
	
	/**
	 * The frames passed since the last second.
	 */
	private int frameTime = 0;
		
	private final Debugger debugger;
	
	/**
	 * Constructor that gets the screen information, the keyboard listener and the debugger, 
	 * initialize each entity category on the entities map and initialize the obstacle spawner.
	 * 
	 * @param screen the screen information of the game window
	 * @param keyH the keyboard listener linked to the game window
	 * @param debugger the debugger used
	 */
	public LogicsHandler(final Screen screen, final KeyHandler keyH, final Debugger debugger) {		
		this.screen = screen;
		this.keyH = keyH;
		this.debugger = debugger;
		
		EntityType.concreteGenericTypes.stream().sorted((e1, e2) -> -e1.compareTo(e2)).collect(Collectors.toList())
		.forEach(e -> entities.put(e, new HashSet<>()));
		
		playerEntity = new PlayerInstance(this);
		
		displayController = new DisplayController(keyH,screen, g -> setGameState(g),
				() -> gameState, () -> playerEntity.getCurrentScore());
		
		spawner = new TileGenerator(screen.getTileSize(), entities, spawnInterval);
		this.initializeSpawner();
	}
	
	public Screen getScreenInfo() {
		return screen;
	}
	
	public KeyHandler getKeyHandler() {
		return keyH;
	}
	
	public Debugger getDebugger() {
		return debugger;
	}
	
	private void initializeSpawner() {
		spawner.setMissileCreator(p -> new MissileInstance(this, p, playerEntity, new SpeedHandler(500.0, 0, 5000.0)));
		spawner.setZapperBaseCreator(p -> new ZapperBaseInstance(this, p, new SpeedHandler(250.0, 0, 0)));
		spawner.setZapperRayCreator((b,p) -> new ZapperRayInstance(this, p, b.getX(), b.getY()));
		
		try {
			spawner.initialize();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog((Component)GameHandler.gameWindow, "Tiles information file cannot be found.\n\nDetails:\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ParseException | IOException e) {
			JOptionPane.showMessageDialog((Component)GameHandler.gameWindow, "An error occured while trying to load tiles.\n\nDetails:\n"+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void updateTimers() {
		frameTime++;
	}
	
	/**
	 * Removes all entities that are on the "clear area" [x < -tile size].
	 */
	private void updateCleaner() {
		if(frameTime % GameWindow.fpsLimit * cleanInterval == 0) {
			spawner.cleanTiles();
			if(debugger.isFeatureEnabled("log: entities cleaner check")) {
				System.out.println("clean");
			}
		}
	}
	
	/**
	 * Handles the commands executed for each key pressed.
	 */
	private void checkKeyboardInput() {
		switch(keyH.getKeyTyped()) {
			case KeyEvent.VK_Z:
				debugger.setDebugMode(!debugger.isDebugModeOn());
				keyH.resetKeyTyped();
				break;
			case KeyEvent.VK_P:
				this.setGameState(GameState.PAUSED);
				keyH.resetKeyTyped();
				break;
			default:
				break;
		}
	}
	
	private void setGameState(final GameState gs) {
		if(this.gameState != gs) {
			switch (gs) {
				case EXIT:
					final String quitMessage = "Are you sure to quit the game?";
					final String quitTitle = "Quit Game";
					if(JOptionPane.showConfirmDialog((Component)GameHandler.gameWindow, quitMessage, quitTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
						return;
					}
					break;
				case INGAME:
					if (this.gameState == GameState.MENU) {
						entities.get(EntityType.PLAYER).add(playerEntity);
					}
					spawner.resume();
					break;
				case MENU:
					if(this.gameState == GameState.PAUSED) {
						final String message = "Do you want to return to the main menu?\nYou will lose the current progress of this match.";
						final String title = "Return to main menu";
						if(JOptionPane.showConfirmDialog((Component)GameHandler.gameWindow, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
							return;
						}
					}
					synchronized(entities) {
						entities.forEach((s, se) -> {
							se.forEach(e -> e.reset());
							se.clear();
						});
					}
					spawner.pause();
					break;
				case PAUSED:
					if(this.gameState != GameState.INGAME) {
						return;
					}
					spawner.pause();
					break;
				default:
					break;
			}
			this.gameState = gs;
		}
	}
	
	public void updateAll() {
		switch(this.gameState) {
			case EXIT:
				spawner.terminate();
				GameHandler.gameWindow.stopGame();
				System.exit(0);
				break;
			case INGAME:
				this.updateCleaner();
				synchronized(entities) {
					entities.forEach((s, se) -> se.forEach(e -> e.update()));
				}
			default:
				break;
		}
		this.displayController.updateScreen();
		this.checkKeyboardInput();
		this.updateTimers();
	}		
	
	public void drawAll(final Graphics2D g) {
		switch(this.gameState) {
			case PAUSED: 
			case INGAME:
				synchronized(entities) {
					entities.forEach((s, se) -> se.forEach(e -> e.draw(g)));
					entities.forEach((s, se) -> se.forEach(e -> e.drawCoordinates(g)));
				}
			default:
				break;
		}
		this.displayController.drawScreen(g);
	}
	
}

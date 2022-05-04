package game.logics.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
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
import game.logics.entities.pickups.shield.ShieldInstance;
import game.logics.entities.pickups.teleport.TeleportInstance;
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
	private double spawnInterval = 2.5;
	/**
	 * Defines the interval of each check for entities to clean.
	 */
	private double cleanInterval = 5.0;
	
	/**
	 * The frames passed since the last second.
	 */
	static int frameTime = 0;
	
	static int difficultyLevel = 1;
		
	private final Screen screen;
	private final KeyHandler keyH;
	private final Debugger debugger;
	
	/**
	 * Constructor that gets the screen information, the keyboard listener and the debugger, 
	 * initialize each entity category on the entities map and initialize the obstacle spawner.
	 * 
	 */
	public LogicsHandler() {		
		this.screen = GameWindow.gameScreen;
		this.keyH = GameWindow.keyHandler;
		this.debugger = GameWindow.debugger;
		
		EntityType.concreteGenericTypes
		.forEach(e -> entities.put(e, new HashSet<>()));
		
		playerEntity = new PlayerInstance(entities, () -> cleanEntities(t -> t.isGenerableEntity(), e -> true));
		
		displayController = new DisplayController(keyH,screen, g -> setGameState(g),
				() -> gameState, () -> playerEntity.getCurrentScore());
		
		spawner = new TileGenerator(screen.getTileSize(), entities, spawnInterval);
		this.initializeSpawner();
	}

	private void initializeSpawner() {
		spawner.setMissileCreator(p -> new MissileInstance(this, p, playerEntity, new SpeedHandler(500.0, 10.0, 5000.0)));
		spawner.setZapperBaseCreator(p -> new ZapperBaseInstance(this, p, new SpeedHandler(250.0, 15.0, 0)));
		spawner.setZapperRayCreator((b,p) -> new ZapperRayInstance(this, p, b.getX(), b.getY()));
		spawner.setShieldCreator(p -> new ShieldInstance(this, p, playerEntity, new SpeedHandler(250.0, 15.0, 0)));
		spawner.setTeleportCreator(p -> new TeleportInstance(this, p, playerEntity, new SpeedHandler(250.0, 15.0, 0)));
		
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
	
	private void resetEntities(final Predicate<EntityType> typeCondition, final Predicate<Entity> entityCondition) {
		synchronized(entities) {
			entities.entrySet().stream().filter(e -> typeCondition.test(e.getKey())).flatMap(e -> e.getValue().stream()).filter(entityCondition).collect(Collectors.toList())
			.forEach(e -> e.reset());
		}
	}
	
	private void cleanEntities(final Predicate<EntityType> typeCondition, final Predicate<Entity> entityCondition) {
		synchronized(entities) {
			entities.entrySet().stream().filter(e -> typeCondition.test(e.getKey())).flatMap(e -> e.getValue().stream()).filter(entityCondition).collect(Collectors.toList())
			.forEach(e -> GameWindow.debugger.printLog(Debugger.Option.LOG_CLEAN, "Cleaned::" + e.toString()));
			this.resetEntities(typeCondition, entityCondition);
			entities.entrySet().stream().filter(e -> typeCondition.test(e.getKey())).map(e -> e.getValue()).collect(Collectors.toList())
			.forEach(se -> se.removeIf(entityCondition));
		}
	}
	
	private void resetGame() {
		this.cleanEntities(t -> t != EntityType.PLAYER, e -> true);
		this.resetEntities(t -> t == EntityType.PLAYER, e -> true);
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
			case KeyEvent.VK_R:
				this.resetGame();
				this.setGameState(GameState.INGAME);
				keyH.resetKeyTyped();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Removes all entities that are on the "clear area" [x < -tile size].
	 */
	private void updateCleaner() {
		if(frameTime % GameWindow.fpsLimit * cleanInterval == 0) {
			this.cleanEntities(t -> t.isGenerableEntity(), e -> e.isOnClearArea());
		}
	}
	
	private void updateTimers() {
		frameTime++;
	}
	
	private void updateDifficulty() {
		final int increaseDiffPerScore = 250;
		
		difficultyLevel = playerEntity.getCurrentScore() / increaseDiffPerScore + 1;
	}
	
	private void drawDifficultyLevel(final Graphics2D g) {
		if(debugger.isFeatureEnabled(Debugger.Option.DIFFICULTY_LEVEL)) {
			g.setColor(Debugger.debugColor);
			g.setFont(Debugger.debugFont);
			g.drawString("DIFFICULTY: " + difficultyLevel, 3, 26);
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
					} else if (this.gameState == GameState.ENDGAME) {
						this.resetGame();
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
						spawner.stop();
					}
					this.cleanEntities(t -> true, e -> true);
					break;
				case ENDGAME:
					spawner.stop();
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
			case ENDGAME:
				playerEntity.update();
				break;
			case INGAME:
				if(playerEntity.hasDied()) {
					this.setGameState(GameState.ENDGAME);
				}
				this.updateDifficulty();
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
			case ENDGAME: 
			case PAUSED:
			case INGAME:
				synchronized(entities) {
					entities.entrySet().stream().sorted((e1, e2) -> Integer.compare(e2.getKey().ordinal(), e1.getKey().ordinal())).collect(Collectors.toList()).forEach(e -> e.getValue().forEach(se -> se.draw(g)));
					entities.forEach((s, se) -> se.forEach(e -> e.getHitbox().forEach(hitbox -> hitbox.draw(g))));
					entities.forEach((s, se) -> se.forEach(e -> e.drawCoordinates(g)));
				}
				spawner.drawNextSpawnTimer(g);
				this.drawDifficultyLevel(g);
			default:
				break;
		}
		this.displayController.drawScreen(g);
	}
	
}

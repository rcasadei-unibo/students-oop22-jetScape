package game.logics.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.github.cliftonlabs.json_simple.JsonException;

import java.awt.Graphics2D;
import java.awt.Component;
import java.awt.event.KeyEvent;

import java.io.FileNotFoundException;

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
import game.logics.background.Background;
import game.logics.background.BackgroundController;
import game.logics.display.controller.DisplayController;

import game.logics.generator.Generator;
import game.logics.generator.TileGenerator;

import game.logics.handler.Logics.GameInfoHandler;
import game.logics.records.Records;

import game.utility.debug.Debugger;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.EntityType;
import game.utility.other.FormatException;
import game.utility.other.GameState;

/**
 * The {@link LogicsHandler} class helps {@link GameWindow} to update
 * and draw logical parts of the game like the Interface, Entities, Collisions, etc....
 * 
 */
public class LogicsHandler extends AbstractLogics implements Logics {

    private final Background background;
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

    private final KeyHandler keyH;
    private final Debugger debugger;

    private final Records records;
    private final GameInfoHandler game;

    /**
     * Constructor that gets the screen information, the keyboard listener and the debugger, 
     * initialize each entity category on the entities map and initialize the obstacle spawner.
     * 
     */
    public LogicsHandler() {
        super();

        this.keyH = GameWindow.GAME_KEYHANDLER;
        this.debugger = GameWindow.GAME_DEBUGGER;

        EntityType.ALL_ENTITY_TYPE
                .forEach(e -> entities.put(e, new HashSet<>()));

        this.game = new GameInfoHandler(new GameInfo());

        this.playerEntity = new PlayerInstance(this, this.entities);

        this.records = new Records(game, playerEntity);

        this.background = new BackgroundController(super.getBackgroundMovementInfo());

        this.displayController = new DisplayController(this.keyH, g -> setGameState(g),
                () -> this.gameState, () -> this.playerEntity.getCurrentScore(),
                () -> this.game.getActualGame(), this.records);

        this.spawner = new TileGenerator(this.entities, super.getSpawningInteval());
        this.initializeSpawner();
    }

    private void initializeSpawner() {

        this.spawner.setMissileCreator(p -> new MissileInstance(this, p, this.playerEntity, super.getEntityMovementInfo(EntityType.MISSILE)));
        this.spawner.setZapperBaseCreator(p -> new ZapperBaseInstance(this, p, super.getEntityMovementInfo(EntityType.ZAPPERBASE)));
        this.spawner.setZapperRayCreator((b, p) -> new ZapperRayInstance(this, p, b.getX(), b.getY()));
        this.spawner.setShieldCreator(p -> new ShieldInstance(this, p, this.playerEntity, super.getEntityMovementInfo(EntityType.SHIELD)));
        this.spawner.setTeleportCreator(p -> new TeleportInstance(this, p, this.playerEntity, super.getEntityMovementInfo(EntityType.TELEPORT)));

        try {
            this.spawner.initialize();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog((Component) GameHandler.GAME_WINDOW, "Tiles information file cannot be found.\n\nDetails:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (JsonException e) {
            JOptionPane.showMessageDialog((Component) GameHandler.GAME_WINDOW, "An error occured while trying to load tiles.\n\nDetails:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (FormatException e) {
            JOptionPane.showMessageDialog((Component) GameHandler.GAME_WINDOW, "Tiles information file has an incorrect format.\n\nDetails:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public BiConsumer<Predicate<EntityType>, Predicate<Entity>> getEntitiesCleaner() {
        return new BiConsumer<Predicate<EntityType>, Predicate<Entity>>() {
            public void accept(final Predicate<EntityType> typeCondition,
                    final Predicate<Entity> entityCondition) {
                synchronized (entities) {
                    entities.entrySet().stream()
                            .filter(e -> typeCondition.test(e.getKey()))
                            .flatMap(e -> e.getValue().stream())
                            .filter(entityCondition)
                            .peek(e -> GameWindow.GAME_DEBUGGER.printLog(Debugger.Option.LOG_CLEAN, "cleaned::" + e.toString()))
                            .collect(Collectors.toList())
                            .forEach(e -> e.reset());
                    entities.entrySet().stream()
                            .filter(e -> typeCondition.test(e.getKey()))
                            .map(e -> e.getValue())
                            .collect(Collectors.toList())
                            .forEach(se -> se.removeIf(entityCondition));
                }
            }
        };
    }

    private void resetGame() {
        this.getEntitiesCleaner().accept(t -> t != EntityType.PLAYER, e -> true);
        this.playerEntity.reset();
    }

    /**
     * Handles the commands executed for each key pressed.
     */
    private void checkKeyboardInput() {
        switch (this.keyH.getKeyTyped()) {
            case KeyEvent.VK_Z:
                this.debugger.setDebugMode(!this.debugger.isDebugModeOn());
                this.keyH.resetKeyTyped();
                break;
            case KeyEvent.VK_P:
                this.setGameState(GameState.PAUSED);
                this.keyH.resetKeyTyped();
                break;
            default:
                break;
        }
    }

    /**
     * Removes all entities that are on the "clear area" [x &lt; - tile size].
     */
    private void updateCleaner() {
        if (super.getFrameTime() % GameWindow.FPS_LIMIT * super.getCleanerActivityInterval() == 0) {
            this.getEntitiesCleaner().accept(t -> t.isGenerableEntity(), e -> e.isOnClearArea());
        }
    }

    private void updateDifficulty() {
        super.setDifficultyLevel(this.playerEntity.getCurrentScore() / super.getIncreaseDiffPerScore() + 1);
    }

    private void drawDifficultyLevel(final Graphics2D g) {

        final int difficultyMeterXLocation = 3;
        final int difficultyMeterYLocation = 26;

        if (debugger.isFeatureEnabled(Debugger.Option.DIFFICULTY_LEVEL)) {
            g.setColor(Debugger.DEBUG_COLOR);
            g.setFont(Debugger.DEBUG_FONT);
            g.drawString("DIFFICULTY: " + super.getDifficultyLevel(), difficultyMeterXLocation, difficultyMeterYLocation);
        }
    }

    private void quitGame() {
        final String quitMessage = "Are you sure to quit the game?";
        final String quitTitle = "Quit Game";
        if (JOptionPane.showConfirmDialog((Component) GameHandler.GAME_WINDOW, quitMessage, quitTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
    }

    private void quitToMainMenu() {
        final String message = "Do you want to return to the main menu?\nYou will lose the current progress of this match.";
        final String title = "Return to main menu";
        if (JOptionPane.showConfirmDialog((Component) GameHandler.GAME_WINDOW, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }
        spawner.stop();
    }

    private void setGameState(final GameState gs) {
        if (this.gameState != gs) {
            switch (gs) {
                case EXIT:
                    this.quitGame();
                    break;
                case INGAME:
                    if (this.gameState != GameState.PAUSED) {
                        this.game.setActualGame(new GameInfo(this.game));
                        //this.gameID.generateNewGameUID();
                    }
                    if (this.gameState == GameState.ENDGAME) { // RETRY
                        this.records.refresh();
                        this.resetGame();
                    } else if (this.gameState == GameState.MENU) {
                        this.entities.get(EntityType.PLAYER).add(playerEntity);
                        this.records.refresh();
                    }
                    this.spawner.resume();
                    break;
                case MENU:
                    if (this.gameState == GameState.PAUSED) {
                        this.quitToMainMenu();
                    }
                    this.getEntitiesCleaner().accept(t -> true, e -> true);
                    break;
                case ENDGAME:
                    this.spawner.stop();
                    //this.gameOverDisplay.setRecords(getScore.get());
                    //this.records.fetch(this.getGame);
                    this.records.announceGameEnded(() -> this.game.getActualGame());
                    /*for (final Integer record : LogicsHandler.records.getRecordScores()) {
                        System.out.println(record);
                    }*/
                    this.records.update();
                    break;
                case PAUSED:
                    if (this.gameState != GameState.INGAME) {
                        return;
                    }
                    this.spawner.pause();
                    break;
                case RECORDS:
                    this.records.refresh();
                    break;
                default:
                    break;
            }
            this.gameState = gs;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateAll() {
        switch (this.gameState) {
            case EXIT:
                this.spawner.terminate();
                GameHandler.GAME_WINDOW.stopGame();
                GameHandler.GAME_FRAME.dispose();
                break;
            case ENDGAME:
                this.playerEntity.update();
                break;
            case INGAME:
                if (this.playerEntity.hasDied()) {
                    this.setGameState(GameState.ENDGAME);
                    break;
                }
                this.updateDifficulty();
                this.updateCleaner();
                synchronized (this.entities) {
                    this.entities.forEach((s, se) -> se.forEach(e -> e.update()));
                }
            default:
                break;
        }
        this.displayController.updateScreen();
        this.checkKeyboardInput();
        super.updateTimer();
    }

    /**
     * {@inheritDoc}
     */
    public void drawAll(final Graphics2D g) {
        switch (this.gameState) {
            case ENDGAME:
            case PAUSED:
            case INGAME:
                synchronized (this.entities) {
                    this.background.update(g);
                    this.entities.entrySet().stream()
                            .sorted((e1, e2) -> Integer.compare(e2.getKey().ordinal(),
                                    e1.getKey().ordinal()))
                            .collect(Collectors.toList())
                            .forEach(e -> e.getValue().forEach(se -> se.draw(g)));

                    this.entities.forEach((s, se) -> se.forEach(e -> e.getHitbox().draw(g)));
                    this.background.drawCoordinates(g);
                    this.entities.forEach((s, se) -> se.forEach(e -> e.drawCoordinates(g)));
                }
                this.spawner.drawNextSpawnTimer(g);
                this.drawDifficultyLevel(g);
                break;
            default:
                break;
        }
        this.displayController.drawScreen(g);
    }
}


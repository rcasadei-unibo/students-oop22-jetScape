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
import game.logics.entities.pickups.coin.CoinInstance;
import game.logics.entities.pickups.shield.ShieldInstance;
import game.logics.entities.pickups.teleport.TeleportInstance;
import game.logics.entities.player.Player;
import game.logics.entities.player.PlayerInstance;
import game.logics.background.Background;
import game.logics.background.BackgroundController;
import game.logics.display.controller.DisplayController;

import game.logics.generator.Generator;
import game.logics.generator.TileGenerator;
import game.logics.records.Records;
import game.logics.records.RecordsImpl;

import game.utility.debug.Debugger;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.EntityType;
import game.utility.other.FormatException;
import game.utility.other.GameState;
import game.utility.other.Sound;

/**
 * The {@link LogicsHandler} class helps {@link GameWindow} to update
 * and draw logical parts of the game like the Interface, Entities, Collisions, etc....
 * 
 */
public class LogicsHandler extends AbstractLogics implements Logics {

    /**
     * Contains the background on the game environment.
     */
    private final Background background;
    /**
     * Contains the current active entities on the game environment.
     */
    private final Map<EntityType, Set<Entity>> entities = new HashMap<>();
    /**
     * A reference to the player's entity.
     */
    private final Player playerEntity;

    /**
     * Generates sets of obstacles on the environment.
     */
    private final Generator spawner;

    private final DisplayController displayController;

    private GameState gameState = GameState.MENU;

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

        this.playerEntity = new PlayerInstance(this);

        this.records = new RecordsImpl(() -> this.game.getActualGame(), playerEntity);

        this.background = new BackgroundController(super.getBackgroundMovementInfo());

        this.displayController = new DisplayController(this.keyH,
                g -> setGameState(g), () -> this.gameState,
                () -> this.playerEntity.getCurrentScore(),
                () -> this.playerEntity.getCurrentCoinsCollected(),
                this.records);

        this.spawner = new TileGenerator(this.entities, super.getSpawningInteval());
        this.initializeSpawner();
    }

    private void initializeSpawner() {

        this.spawner.setMissileCreator(p -> new MissileInstance(this, p,
                this.playerEntity, super.getEntityMovementInfo(EntityType.MISSILE)));
        this.spawner.setZapperBaseCreator(p -> new ZapperBaseInstance(this, p,
                super.getEntityMovementInfo(EntityType.ZAPPERBASE)));
        this.spawner.setZapperRayCreator((b, p) -> new ZapperRayInstance(this, p, b.getX(), b.getY()));
        this.spawner.setShieldCreator(p -> new ShieldInstance(this, p,
                this.playerEntity, super.getEntityMovementInfo(EntityType.SHIELD)));
        this.spawner.setTeleportCreator(p -> new TeleportInstance(this, p,
                this.playerEntity, super.getEntityMovementInfo(EntityType.TELEPORT)));
        this.spawner.setCoinCreator(p -> new CoinInstance(this, p,
                this.playerEntity, super.getEntityMovementInfo(EntityType.COIN)));

        try {
            this.spawner.initialize();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog((Component) GameHandler.GAME_WINDOW,
                    "Tiles information file cannot be found.\n\nDetails:\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (JsonException e) {
            JOptionPane.showMessageDialog((Component) GameHandler.GAME_WINDOW,
                    "An error occured while trying to load tiles.\n\nDetails:\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (FormatException e) {
            JOptionPane.showMessageDialog((Component) GameHandler.GAME_WINDOW,
                    "Tiles information file has an incorrect format.\n\nDetails:\n"
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    /**
     * {@inheritDoc}
     */
    public Map<EntityType, Set<Entity>> getEntities() {
        return entities;
    }

    private void resetGame() {
        this.getEntitiesCleaner().accept(t -> t != EntityType.PLAYER, e -> true);
        this.playerEntity.reset();
        this.background.reset();
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
                GameWindow.GAME_SOUND.stop(Sound.JETPACK);
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

    private boolean quitGame() {
        final String quitMessage = "Are you sure to quit the game?";
        final String quitTitle = "Quit Game";
        return JOptionPane.showConfirmDialog((Component) GameHandler.GAME_WINDOW,
                quitMessage, quitTitle, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION;
    }

    private boolean quitToMainMenu() {
        final String message = "Do you want to return to the main menu?\n"
                + "You will lose the current progress of this match.";
        final String title = "Return to main menu";
        return JOptionPane.showConfirmDialog((Component) GameHandler.GAME_WINDOW,
                message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION;
    }

    private void setupInGame() {
        if (this.gameState != GameState.PAUSED) {
            this.game.setActualGame(new GameInfo(this.game));
            //this.game.generateNewGameInfo();
        }
        if (this.gameState == GameState.ENDGAME) { // RETRY
            this.records.refresh();
            this.resetGame();
        } else if (this.gameState == GameState.MENU) { // START
            this.records.refresh();
            this.resetGame();
            this.entities.get(EntityType.PLAYER).add(playerEntity);
        }
        this.spawner.resume();
    }

    private void setupEndgame() {
        final GameInfo actualGame = this.game.getActualGame();
        this.spawner.stop();

        // Declares game ended: sets game end date and gets final score
        if (!actualGame.isGameEnded()) {
            actualGame.setGameEnded(this.playerEntity.getCurrentScore(),
                    this.playerEntity.getCurrentCoinsCollected());

            this.records.fetch(actualGame);
        }
        this.records.update();
    }

    private void setGameState(final GameState gs) {
        if (this.gameState != gs) {
            switch (gs) {
                case EXIT:
                    if (this.quitGame()) {
                       return;
                    }
                    break;
                case INGAME:
                    this.setupInGame();
                    break;
                case MENU:
                    if (this.gameState == GameState.PAUSED
                            && this.quitToMainMenu()) {
                        this.spawner.stop();
                        return;
                    }
                    this.getEntitiesCleaner().accept(t -> true, e -> true);
                    break;
                case ENDGAME:
                    this.setupEndgame();
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
                this.background.update();
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
                this.background.draw(g);
                synchronized (this.entities) {
                    this.entities.entrySet().stream()
                            .sorted((e1, e2) -> Integer.compare(e2.getKey().ordinal(),
                                    e1.getKey().ordinal()))
                            .collect(Collectors.toList())
                            .forEach(e -> e.getValue().forEach(se -> se.draw(g)));
                    this.entities.forEach((s, se) -> se.forEach(e -> e.getHitbox().draw(g)));
                    this.entities.forEach((s, se) -> se.forEach(e -> e.drawCoordinates(g)));
                }
                this.background.drawCoordinates(g);
                this.spawner.drawNextSpawnTimer(g);
                this.drawDifficultyLevel(g);
                break;
            default:
                this.background.draw(g);
                break;
        }
        this.displayController.drawScreen(g);
    }

    /**
     * {@inheritDoc}
     */
    public GameInfoHandler getGame() {
        return this.game;
    }
}


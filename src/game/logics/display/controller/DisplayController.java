package game.logics.display.controller;

import game.logics.display.handlers.DisplayHandler;
import game.logics.display.handlers.MenuHandler;
import game.logics.display.handlers.SettingsHandler;
import game.logics.display.view.DisplayGameOver;
import game.logics.display.view.DisplayHUD;
import game.logics.display.view.DisplayMainMenu;
import game.logics.display.view.DisplayPause;
import game.logics.display.view.DisplayRecords;
import game.logics.display.view.DisplaySettings;

import game.logics.handler.Logics.GameInfo;
import game.logics.records.Records;

import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;

import java.awt.Graphics2D;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The <code>DisplayController</code> class helps {@link game.logics.handler.LogicsHandler LogicsHandler}
 * to update and draw the correct {@link game.logics.display.view.Display Display} on the screen.
 */
public class DisplayController {
    private final Supplier<GameState> getState;
    private final Supplier<Integer> getScore;
    private final Supplier<GameInfo> getGame;

    /*
     * Screen's displays
     */
    private final DisplayHUD hud;
    private final DisplayPause pauseDisplay;
    private final DisplayMainMenu mainMenuDisplay;
    private final DisplayRecords recordsDisplay;
    private final DisplayGameOver gameOverDisplay;
    private final DisplaySettings gameSettings;
    /*
     * Handlers for every display with a menu
     */
    private final DisplayHandler pauseHandler;
    private final DisplayHandler titleHandler;
    private final DisplayHandler recordsHandler;
    private final DisplayHandler gameOverHandler;
    private final SettingsHandler settingsHandler;
    // TODO: eventually add shop 

    /**
     * {@link DisplayController} builder: builds all displayed cards and
     * all {@link MenuHandler} needed instances.
     *
     * @param keyH
     * @param setState Consumer to set new value of State
     * @param getState Supplier to get new value from State
     * @param getScore Supplier to get new value of Score
     */
    public DisplayController(final KeyHandler keyH,
            final Consumer<GameState> setState,
            final Supplier<GameState> getState,
            final Supplier<Integer> getScore,
            final Supplier<GameInfo> getGame, final Records records) {

        this.getState = getState;
        this.getScore = getScore;
        this.getGame = getGame;

        this.hud = new DisplayHUD();
        this.pauseDisplay = new DisplayPause();
        this.mainMenuDisplay = new DisplayMainMenu();
        this.gameSettings = new DisplaySettings();
        this.recordsDisplay = new DisplayRecords(records);
        this.gameOverDisplay = new DisplayGameOver(records);

        this.pauseHandler = new MenuHandler(keyH, pauseDisplay, setState);
        this.titleHandler = new MenuHandler(keyH, mainMenuDisplay, setState);
        this.recordsHandler = new MenuHandler(keyH, recordsDisplay, setState);
        this.gameOverHandler = new MenuHandler(keyH, gameOverDisplay, setState);
        this.settingsHandler = new SettingsHandler(keyH, gameSettings, setState);
    }

    /**
     * Displays the correct screen for the current game state.
     * @param g
     */
    public void drawScreen(final Graphics2D g) {
        switch (getState.get()) {
            case MENU :
                this.mainMenuDisplay.drawScreen(g,
                        titleHandler.getSelectedOption());
                break;
            case RECORDS :
                this.recordsDisplay.drawScreen(g,
                        recordsHandler.getSelectedOption());
                break;
            case INGAME :
                this.hud.drawScreen(g);
                break;
           case PAUSED :
                this.pauseDisplay.drawScreen(g,
                        pauseHandler.getSelectedOption());
                break;
           case ENDGAME :
                this.gameOverDisplay.drawScreen(g,
                        gameOverHandler.getSelectedOption());
                break;
           case SETTINGS :
               this.gameSettings.drawScreen(g,
                        settingsHandler.getSelectedOption());
           default:
                break;
        }
    }

    /**
     * Updates the current screen (selected options or score), in menus changes
     * game state when you choose an option.
     */
    public void updateScreen() {
        switch (getState.get()) {

            case MENU :
                titleHandler.update();
                break;
            case RECORDS :
                recordsHandler.update();
                break;
            case PAUSED :
                this.pauseHandler.update();
                break;
            case INGAME :
                this.hud.updateScore(getScore.get());
                break;
            case ENDGAME :
                this.gameOverHandler.update();
                break;
            case SETTINGS :
                this.settingsHandler.update();
            default :
                break;
        }
    }
}

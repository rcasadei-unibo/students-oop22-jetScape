package game.logics.display.controller;

import game.logics.display.handlers.DisplayHandler;
import game.logics.display.handlers.MenuHandler;
import game.logics.display.view.DisplayGameOver;
import game.logics.display.view.DisplayHUD;
import game.logics.display.view.DisplayMainMenu;
import game.logics.display.view.DisplayRecords;
import game.logics.display.view.DisplayPause;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.screen.Screen;

import java.awt.Graphics2D;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayController {
	private final Supplier<GameState> getState;
	private final Supplier<Integer> getScore;
	private final DisplayHUD hud;
	private final DisplayPause pauseDisplay;
	private final DisplayMainMenu mainMenuDisplay;
	private final DisplayRecords recordsDisplay;
	private final DisplayGameOver gameOverDisplay;
	private final DisplayHandler pauseHandler;
	private final DisplayHandler titleHandler;
	private final DisplayHandler recordsHandler;
	private final DisplayHandler gameOverHandler;

	/* TO DO eventually add shop */
	
	public DisplayController(final KeyHandler keyH, final Screen gScreen, 
			Consumer<GameState> setState, Supplier<GameState> getState,
			Supplier<Integer> getScore) {
		super();
		this.getState = getState;
		this.getScore = getScore;
		this.hud = new DisplayHUD(gScreen);
		this.pauseDisplay = new DisplayPause(gScreen);
		this.mainMenuDisplay = new DisplayMainMenu(gScreen);
		this.recordsDisplay = new DisplayRecords(gScreen);
		this.gameOverDisplay = new DisplayGameOver(gScreen);

		this.pauseHandler = new MenuHandler(keyH, pauseDisplay, setState);
		this.titleHandler = new MenuHandler(keyH, mainMenuDisplay, setState);
		this.recordsHandler = new MenuHandler(keyH, recordsDisplay, setState);
		this.gameOverHandler = new MenuHandler(keyH, gameOverDisplay, setState);
	}
	
	/*
	 * displays the correct screen for the current game state
	 */
	public void drawScreen (Graphics2D g) {
		switch(getState.get()) {
		case MENU :
			this.mainMenuDisplay.drawScreen(g, titleHandler.getSelectedOption());
			break;
		case RECORDS :
			this.recordsDisplay.drawScreen(g, recordsHandler.getSelectedOption());
			break;
		case INGAME :
			this.hud.drawScreen(g);
			break;
		case PAUSED :
			this.pauseDisplay.drawScreen(g, pauseHandler.getSelectedOption());
			break;
		case ENDGAME :
			this.gameOverDisplay.drawScreen(g, gameOverHandler.getSelectedOption());
			break;
		default:
			break;
		}
	}
	
	/*
	 * Updates the current screen (selected options or score), in menus changes game state
	 * when you choose an option 
	 */
	public void updateScreen () {
		switch(getState.get()) {
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
		default :
			break;
		}
	}
	
}

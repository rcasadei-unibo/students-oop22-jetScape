package game.logics.display.controller;

import game.logics.display.handlers.DisplayHandler;
import game.logics.display.handlers.MenuHandler;
import game.logics.display.view.DisplayHUD;
import game.logics.display.view.DisplayMainMenu;
import game.logics.display.view.DisplayPause;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.screen.Screen;

import java.awt.Graphics2D;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayController {
	private final Supplier<GameState> getState ;
	private final Supplier<Integer> getScore ;
	private final DisplayHUD hud ;
	private final DisplayPause pauseDisplay ;
	private final DisplayMainMenu mainMenuDisplay ;
	private final DisplayHandler pauseHandler ;
	private final DisplayHandler mainMenuHandler ;
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
		this.pauseHandler = new MenuHandler(keyH, pauseDisplay, setState);
		this.mainMenuHandler = new MenuHandler(keyH, mainMenuDisplay, setState);
	}
	
	/*
	 * displays the correct screen for the current game state
	 */
	public void drawScreen (Graphics2D g) {
		switch(getState.get()) {
		case MENU :
			this.mainMenuDisplay.drawScreen(g, mainMenuHandler.getSelectedOption());
			break;
		case INGAME :
			this.hud.drawScreen(g);
			break;
		case PAUSED :
			this.pauseDisplay.drawScreen(g, pauseHandler.getSelectedOption());
			break;
		default:
			break;
		}
	}
	
	public void updateScreen () {
		switch(getState.get()) {
		case MENU :
			mainMenuHandler.handle();
			break;
		case PAUSED :
			this.pauseHandler.handle();
			break;
		case INGAME :
			this.hud.updateScore(getScore.get());
			break;
		default :
			break;
		}
	}
	
}

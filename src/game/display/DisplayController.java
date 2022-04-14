package game.display;

import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.screen.Screen;

import java.awt.Graphics2D;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayController {
	private final Supplier<GameState> getState ;
	private final Supplier<Integer> getScore ;
	private final Consumer<GameState> setState ;
	private final Display hud ;
	private final Display pause ;
	private final Display mainMenu ;
	/* TO DO eventually add shop */
	
	public DisplayController(final KeyHandler keyH, final Screen gScreen, 
			Consumer<GameState> setState, Supplier<GameState> getState,
			Supplier<Integer> getScore) {
		super();
		this.getState = getState;
		this.setState = setState;
		this.getScore = getScore;
		this.hud = new DisplayHUD(gScreen);
		this.pause = new DisplayPause(gScreen, keyH);
		this.mainMenu = new DisplayMainMenu(gScreen, keyH);
	}
	
	/*
	 * displays the correct screen for the current game state
	 */
	public void drawScreen (Graphics2D g) {
		switch(getState.get()) {
		case MENU :
			mainMenu.drawScreen(g);
			break;
		case INGAME :
			hud.drawScreen(g);
			break;
		case PAUSED :
			pause.drawScreen(g);
			break;
		default:
			break;
		}
	}
	
	public void updateScreen () {
		switch(getState.get()) {
		case MENU :
			setState.accept(this.mainMenu.getHandler().get().handle());
			break;
		case PAUSED :
			setState.accept(this.pause.getHandler().get().handle());
			break;
		case INGAME :
			((DisplayHUD) hud).updateScore(getScore.get());
			break;
		default :
			break;
		}
	}
	
}

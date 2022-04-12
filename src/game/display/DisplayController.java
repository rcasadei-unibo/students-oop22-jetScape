package game.display;

import game.logics.handler.DisplayHandler;
import game.logics.handler.MenuHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.screen.Screen;

import java.awt.Graphics2D;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayController {
	private final Display hud ;
	private final DisplayHandler menu ;
	private final Display pause ; 
	/* TO DO eventually add shop etc... */
	private final Supplier<GameState> getState ;
	private final Consumer<GameState> setState ;

	public DisplayController(final KeyHandler keyH, final Screen gScreen, Consumer<GameState> setState, Supplier<GameState> getState) {
		super();
		this.hud = new DisplayHUD(gScreen);
		this.menu = new MenuHandler(keyH,gScreen);
		this.pause = new DisplayPause(gScreen) ;
		this.getState = getState;
		this.setState = setState;
	}
	
	/*
	 * displays the correct screen for the current game state
	 */
	public void drawScreen (Graphics2D g) {
		switch(getState.get()) {
		case MENU :
			this.menu.draw(g);	
			break;
		case INGAME :
			hud.drawScreen(g);
			break;
		case PAUSED :
			pause.drawScreen(g);
			break;
		}
	}
	
	public void updateScreen () {
		switch(getState.get()) {
		case MENU :
			setState.accept(this.menu.handle());
			break;		
		}
	}
	public void updateHUD(int score) {
		((DisplayHUD) hud).updateScore(score);
	}

}

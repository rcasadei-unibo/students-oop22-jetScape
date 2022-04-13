package game.display;

import game.logics.handler.DisplayHandler;
import game.logics.handler.MenuHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

import java.awt.Graphics2D;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DisplayController {
	private final List<Pair<String,GameState>> mainMenuOptions;
	private final List<Pair<String,GameState>> pauseMenuOptions;
	private final Supplier<GameState> getState ;
	private final Consumer<GameState> setState ;
	private final Display hud ;
	/* TO DO eventually add shop */
	private final DisplayHandler menuH ;
	private final DisplayHandler pauseH ;
	
	public DisplayController(final KeyHandler keyH, final Screen gScreen, 
			Consumer<GameState> setState, Supplier<GameState> getState) {
		super();
		this.mainMenuOptions = List.of(new Pair<>("Start",GameState.INGAME),
				new Pair<>("Shop",GameState.MENU),
				new Pair<>("Quit",GameState.EXIT));
		this.pauseMenuOptions = List.of(new Pair<>("Resume",GameState.INGAME),
				new Pair<>("back to menu",GameState.MENU));
		this.getState = getState;
		this.setState = setState;
		this.hud = new DisplayHUD(gScreen);
		this.menuH = new MenuHandler(new DisplayMainMenu(gScreen), mainMenuOptions,
				keyH, GameState.MENU);
		this.pauseH = new MenuHandler(new DisplayPause(gScreen), pauseMenuOptions,
				keyH, GameState.PAUSED);
	}
	
	/*
	 * displays the correct screen for the current game state
	 */
	public void drawScreen (Graphics2D g) {
		switch(getState.get()) {
		case MENU :
			menuH.draw(g);
			break;
		case INGAME :
			hud.drawScreen(g, List.of());
			break;
		case PAUSED :
			pauseH.draw(g);
			break;
		default:
			break;
		}
	}
	
	public void updateScreen () {
		switch(getState.get()) {
		case MENU :
			setState.accept(this.menuH.handle());
			break;
		case PAUSED :
			setState.accept(this.pauseH.handle());
			break;
		default :
			break;
		}
	}
	
	public void updateHUD(int score) {
		((DisplayHUD) hud).updateScore(score);
	}

}

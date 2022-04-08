package game.display;

import game.utility.other.GameState;
import game.utility.screen.Screen;

import java.awt.Graphics2D;

public class DisplayController {
	private final Display hud ;
	private final Display menu ;
	private final Display pause ; 
	/* TO DO eventually add shop etc... */
	
	public DisplayController(final Screen gScreen) {
		super();
		this.hud = new DisplayHUD(gScreen);
		this.menu = new DisplayMainMenu(gScreen);
		this.pause = new DisplayPause(gScreen) ;
	}
	
	/*
	 * displays the correct screen for the current game state
	 */
	public void displayScreen (Graphics2D g, GameState gs) {
		switch(gs) {
		case MENU :
			menu.drawScreen(g);
			break;
		case INGAME :
			hud.drawScreen(g);
			break;
		case PAUSED :
			pause.drawScreen(g);
			break;
		}
	}
	
	public void updateHUD(int score) {
		((DisplayHUD) hud).updateScore(score);
	}

}

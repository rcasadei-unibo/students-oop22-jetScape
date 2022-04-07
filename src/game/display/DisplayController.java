package game.display;

import game.utility.other.GameState;
import game.utility.screen.Screen;

import java.awt.Graphics2D;

public class DisplayController {
	private final Display hud ;
	private final Display menu ;
	/* TO DO eventually add shop etc... */
	
	public DisplayController(final Screen gScreen) {
		super();
		this.hud = new DisplayHUD();
		this.menu = new DisplayMainMenu(gScreen);
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
		}
	}

}

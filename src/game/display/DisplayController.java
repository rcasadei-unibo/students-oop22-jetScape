package game.display;

import game.utility.other.GameState;
import java.awt.Graphics2D;

public class DisplayController {
	private final Display hud = new DisplayHUD();
	private final Display menu = new DisplayMainMenu();
	/* TO DO eventually add shop etc... */
	
	/*
	 * displays the correct screen for the current game state
	 */
	void displayScreen (Graphics2D g, GameState gs) {
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

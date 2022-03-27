package game.main;

import game.frame.GameHandler;
import game.frame.Game;

public class Main {
	
	static final boolean debugMode = false;

	public static void main(String[] args) {
		Game g = new GameHandler(debugMode);
		g.initialize();
	}
}

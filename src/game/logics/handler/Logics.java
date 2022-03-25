package game.logics.handler;

import java.awt.Graphics2D;

import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Screen;

public interface Logics {
	
	void updateAll();
	
	void drawAll(Graphics2D g);
		
	Screen getScreenInfo();
	
	KeyHandler getKeyHandler();
	
	boolean isDebugModeOn();
}

package game.logics.handler;

import java.awt.Graphics2D;

import game.utility.debug.Debugger;
import game.utility.input.keyboard.KeyHandler;
import game.utility.screen.Screen;

public interface Logics {
		
	Screen getScreenInfo();
	
	KeyHandler getKeyHandler();
	
	Debugger getDebugger();
	
	void updateAll();
	
	void drawAll(Graphics2D g);
}

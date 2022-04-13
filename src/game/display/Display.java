package game.display;

import java.awt.Graphics2D;
import java.util.List;

import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

public interface Display {
	/*
	 * displays the screen 
	 */
	public void drawScreen(Graphics2D g, List<Pair<String,GameState>> options);
	
	public default int getCenteredX(Screen gScreen, Graphics2D g, String text) {
		int lenght = (int)g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return gScreen.getWidth()/2 - lenght/2;
	}
	
	public void setCursorIndex(int index);
	
}

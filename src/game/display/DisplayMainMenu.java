package game.display;

import java.awt.Font;
import java.awt.Graphics2D;

import game.utility.screen.Screen;

public class DisplayMainMenu implements Display {
	private final Screen gScreen;

	public DisplayMainMenu(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}

	@Override
	public void drawScreen(Graphics2D g) {
		String title = "JetScape";
		g.setFont(g.getFont().deriveFont(Font.BOLD, 96F));
		g.drawString(title,this.getCenteredX(g, title),gScreen.getTileSize()*3);

	}
	
	private int getCenteredX(Graphics2D g, String text) {
		int lenght = (int)g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return this.gScreen.getWidth()/2 - lenght/2;
	}

}

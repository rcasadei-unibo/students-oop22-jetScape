package game.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.utility.screen.Screen;

public class DisplayMainMenu implements Display {
	static final int titleTile = 2;
	static final int msgTile = 7;
	static final int titleShift = 5;
	static final int msgShift = 3;
	static final Font font = new Font("magneto", Font.PLAIN, 112);
	
	private final Screen gScreen;

	public DisplayMainMenu(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}

	@Override
	public void drawScreen(Graphics2D g) {
		int x = 0;
		String title = "JetScape";
		String msg1 = "Press \"enter\" to start";
		
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(font);
		x = this.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		//MESSAGE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(g.getFont().deriveFont(Font.ITALIC, 48F));
		x = this.getCenteredX(gScreen, g, msg1);
		g.drawString(msg1, x + msgShift, gScreen.getTileSize() * msgTile);
		//MESSAGE
		g.setColor(Color.white);
		g.drawString(msg1, x, gScreen.getTileSize() * msgTile);
	}
	
	

}

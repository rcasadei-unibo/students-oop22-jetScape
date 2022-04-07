package game.display;

import java.awt.Color;
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
		String msg1 = "Press \"enter\" to start";
		g.setColor(Color.white);
		g.setFont(g.getFont().deriveFont(Font.LAYOUT_RIGHT_TO_LEFT, 110F));
		g.drawString(title,this.getCenteredX(gScreen, g, title),gScreen.getTileSize()*2);
		g.setFont(g.getFont().deriveFont(Font.ITALIC, 35F));
		g.drawString(msg1,this.getCenteredX(gScreen, g, msg1),gScreen.getTileSize()*7);
	}
	
	

}

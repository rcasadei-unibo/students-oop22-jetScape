package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;

import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplayMainMenu extends Display {
	static final int titleTile = 2;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final String title = "JetScape";

	public DisplayMainMenu(final Screen gScreen) {
		super(gScreen);
		
		this.options.add(MenuOption.START);
		this.options.add(MenuOption.SHOP);
		this.options.add(MenuOption.QUIT);
		this.options.add(MenuOption.RECORDS);
	}
	
	public void drawScreen(final Graphics2D g, final MenuOption selected) {
		this.selectedOption = selected;
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(super.titleFont);
		int x = super.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		
		//OPTIONS SHADOW
		g.setColor(Color.darkGray);
		super.drawText(g, textShift);
		
		//OPTIONS
		g.setColor(Color.white);
		super.drawText(g,0);
	}
}

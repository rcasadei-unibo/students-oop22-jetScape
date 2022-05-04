package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;

import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplayGameOver extends Display {
	static final int titleTile = 2;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final int textTile = 5;
	static final int resultTile = 4;
	static final String title = "Game Over";

	public DisplayGameOver(final Screen gScreen) {
		super(gScreen);
		this.options.add(MenuOption.RETRY);
		this.options.add(MenuOption.MENU);
		super.setTextTile(DisplayGameOver.textTile);
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
		
		//OPTIONS AND RESULT SHADOW
		g.setColor(Color.darkGray);
		super.drawText(g, textShift);
		//OPTIONS AND RESULT
		g.setColor(Color.white);
		super.drawText(g,0);
	}	
}

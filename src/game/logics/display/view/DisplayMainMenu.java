package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayMainMenu extends Display {
	static final int titleTile = 2;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final String title = "JetScape";
	static final GameState currentGS = GameState.MENU;

	public DisplayMainMenu(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Start";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Shop", GameState.MENU);
		this.options.put("Records", GameState.RECORDS);
		this.options.put("Quit", GameState.EXIT);
		this.buildText(firstOption);
	}
	
	public void drawScreen(final Graphics2D g, final String selected) {
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

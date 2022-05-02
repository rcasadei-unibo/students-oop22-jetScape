package game.logics.display.view;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayPause extends Display {
	static final int titleTile = 2;
	static final int titleShift = 3;
	static final int textShift = 2;
	static final String title = "Paused";
	static final Font font = new Font("magneto", Font.PLAIN, 112);
	static final Font fontText = new Font("calibri", Font.PLAIN, 48);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	static final GameState currentGS = GameState.PAUSED;

	public DisplayPause(Screen gScreen) {
		super(gScreen);
		
		firstOption = "Resume";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Back to menu", GameState.MENU);
		
		this.buildText(firstOption);
	}

	public void drawScreen(Graphics2D g, String selected) {
		super.selectedOption = selected;
		int x = 0;
		
		//TITLE SHADOW
		g.setColor(Color.black);
		g.setFont(font);
		x = super.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		//OPTIONS SHADOW
		g.setColor(Color.black);
		super.drawText(g, textShift);
		//OPTIONS
		g.setColor(Color.white);
		super.drawText(g,0);
	}	
}

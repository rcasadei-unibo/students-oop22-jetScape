package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayMainMenu extends Display {
	static final int titleTile = 2;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final String title = "JetScape";
	static final Font font = new Font("magneto", Font.PLAIN, 112);
	static final Font fontText = new Font("calibri", Font.PLAIN, 48);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	static final GameState currentGS = GameState.MENU;
	
	private int x = 0;

	public DisplayMainMenu(Screen gScreen) {
		super(gScreen);
		
		firstOption = "Start";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Shop", GameState.MENU);
		this.options.put("Quit", GameState.EXIT);
		this.buildText(firstOption);
	}
	
	public void drawScreen(Graphics2D g, String selected) {
		this.selectedOption = selected;
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(font);
		x = super.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		//CREATE TEXT LIST
		g.setFont(DisplayMainMenu.fontText);
//		super.buildText(firstOption);
		//OPTIONS SHADOW
		g.setColor(Color.darkGray);
		super.drawText(g, textShift);
		//OPTIONS
		g.setColor(Color.white);
		super.drawText(g,0);
		//SELECTED OPTION SHADOW
//		g.setColor(Color.darkGray);
//		g.setFont(DisplayMainMenu.selectedTextFont);
//		selectedOption = ("> "+ this.selectedOption +" <");
//		x = super.getCenteredX(gScreen, g, selectedOption);
//		g.drawString(selectedOption, x + textShift,
//				gScreen.getTileSize() *(textTile + this.getCenteredX(gScreen, g, selectedOption)));
//		// SELECTED OPTION
//		g.setColor(Color.white);
//		g.drawString(selectedOption, x,
//				gScreen.getTileSize() *(textTile + this.getCenteredX(gScreen, g, selectedOption)));
	}
	
}

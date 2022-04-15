package game.logics.display.view;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;

import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

public class DisplayPause extends Display {
	static final int titleTile = 2;
	static final int titleShift = 3;
	static final int textShift = 2;
	static final String title = "Paused";
	static final Font font = new Font("Stencil", Font.PLAIN, 112);
	static final Font fontText = new Font("calibri", Font.PLAIN, 48);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	static final GameState currentGS = GameState.PAUSED;
	
	private int x = 0;

	public DisplayPause(Screen gScreen) {
		super(gScreen);
		super.options.put("Resume", GameState.INGAME);
		super.options.put("Back to menu", GameState.MENU);
	}

	public void drawScreen(Graphics2D g, Pair<String,Integer> selected) {
		super.selectedOption = selected;
		//TITLE SHADOW
		g.setColor(Color.black);
		g.setFont(font);
		x = super.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		//CREATE TEXT LIST
		g.setFont(DisplayMainMenu.fontText);
		super.buildText(g);
		//MESSAGE SHADOW
		g.setColor(Color.black);
		super.drawText(g, textShift);
		//MESSAGE
		g.setColor(Color.white);
		super.drawText(g,0);
		//SELECTED TEXT SHADOW
		g.setColor(Color.darkGray);
		g.setFont(DisplayMainMenu.selectedTextFont);
		super.selectedOption.setX("> "+ super.selectedOption.getX() +" <");
		x = super.getCenteredX(gScreen, g, selectedOption.getX());
		g.drawString(selectedOption.getX(), x + textShift,
				gScreen.getTileSize() *(textTile + super.selectedOption.getY()));
		// SELECTED TEXT 
		g.setColor(Color.white);
		g.drawString(selectedOption.getX(), x, 
				gScreen.getTileSize() *(textTile + super.selectedOption.getY()));
	}	
}

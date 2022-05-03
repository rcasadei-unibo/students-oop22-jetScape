package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayGameOver extends Display {
	static final int titleTile = 2;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final int textTile = 5;
	static final int resultTile = 4;
	static final String title = "Game Over";
	static final String result = "Your score is";
	static final GameState currentGS = GameState.ENDGAME;
	private int score;

	public DisplayGameOver(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Retry";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Back to Menu", GameState.MENU);
		this.buildText(firstOption);
		super.setTextTile(DisplayGameOver.textTile);
	}
	
	public void drawScreen(final Graphics2D g, final String selected) {
		this.selectedOption = selected;
		
		//TITLE SHADOW
		g.setColor(Color.darkGray);
		g.setFont(Display.titleFont);
		int x = super.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		
		//OPTIONS AND RESULT SHADOW
		g.setColor(Color.darkGray);
		super.drawText(g, textShift);
		x = super.getCenteredX(gScreen, g, result + this.score);
		g.drawString(result + this.score, x + textShift, gScreen.getTileSize() * resultTile);
		
		//OPTIONS AND RESULT
		g.setColor(Color.white);
		super.drawText(g,0);
		x = super.getCenteredX(gScreen, g, result + this.score);
		g.drawString(result + this.score, x, gScreen.getTileSize() * resultTile);
	}
	
	public void updateScore(int score) {
		this.score = score;
	}
	
}

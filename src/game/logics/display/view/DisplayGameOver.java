package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayGameOver extends Display {
	static final int textTile = 4;
	static final int optionTile = 7;
	static String title = "Game Over";
	static String scoreString = "Your score was: ";
	static final GameState currentGS = GameState.ENDGAME;
	int finalScore;

	public DisplayGameOver(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Retry";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Back to Menu", GameState.MENU);
		this.buildTextOptions(firstOption);
	}
	
	public void drawScreen(final Graphics2D g, final String selected) {
		this.selectedOption = selected;
		
		// TITLE
		super.drawTitleText(g, title, Function.identity());
		
		// SCORE	
		super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.scoreString + this.finalScore, x -> x,
				DisplayGameOver.textTile * gScreen.getTileSize(), super.getTextShift());
		
		// OPTIONS
		super.drawOptions(g, DisplayGameOver.optionTile);
	}

	public void setFinalScore(final int finalScore) {
		this.finalScore = finalScore;
	}

	@Override
	protected Color getShiftColor() {
		return Color.DARK_GRAY;
	}	
}

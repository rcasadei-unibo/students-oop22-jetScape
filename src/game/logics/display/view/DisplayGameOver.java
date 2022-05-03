package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayGameOver extends Display {
	static final int optionTile = 7;
	static String title = "Game Over";
	static final GameState currentGS = GameState.ENDGAME;
	static int finalScore;

	public DisplayGameOver(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Retry";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Back to Menu", GameState.MENU);
		this.buildTextOptions(firstOption);
	}
	
	public void setFinalScore(final int finalScore) {
		DisplayGameOver.finalScore = finalScore;
	}
	
	public void drawScreen(final Graphics2D g, final String selected) {
		this.selectedOption = selected;
		
		// TITLE
		super.drawCenteredText(g, Display.FontChoose.TITLE_FONT, title, Function.identity());
		
		// OPTIONS
		super.drawOptions(g, DisplayGameOver.optionTile);
	}

	@Override
	protected Color getShiftColor() {
		return Color.DARK_GRAY;
	}	
}

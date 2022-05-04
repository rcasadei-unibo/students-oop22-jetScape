package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.utility.screen.Screen;

public class DisplayHUD extends Display {
	private static final int scoreTile = 1;
	private static final int scoreShift = 3;
	private static final int textXTile = 10;
	private static final Font scoreFont = GameWindow.fLoader.getOptionsFont().deriveFont(32f);
	private int score = 0;
	
	public DisplayHUD(final Screen gScreen) {
		super(gScreen);
	}

	public void drawScreen(final Graphics2D g) {
		String scoreString = "SCORE: " + this.score;
		
		// SCORE
		super.drawText(g, scoreFont, scoreString, super.gScreen.getTileSize() * textXTile,
				super.gScreen.getTileSize() * scoreTile, scoreShift);
	}
	
	public void updateScore(final int score) {
		this.score = score;
	}
	
	@Override
	protected Color getShiftColor() {
		return Color.DARK_GRAY;
	}
}

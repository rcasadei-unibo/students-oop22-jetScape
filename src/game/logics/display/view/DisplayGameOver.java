package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayGameOver extends Display {
	private static final int textTile = 3;
	private static final int optionTile = 7;
	private static String title = "Game Over";
	private static String scoreString = "Your score was: ";
	private static String recordString = "NEW RECORD";
	private static String[] playingRecordString = {"BARRY COULD", "LIVE LONGER"};

	private static final GameState currentGS = GameState.ENDGAME;
	
	private static int playingRecord = 0; // higher score obtained by playing consecutively
	private static boolean isNewPlayingRecord = false;

	private final int record; // absolute new record
	private boolean isNewRecord = false;

	int finalScore;

	public DisplayGameOver(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Retry";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Back to Menu", GameState.MENU);
		this.buildTextOptions(firstOption);
		
		this.record = 0;//StatisticsReader.getRecord(); // TODO read record
	}
	
	public void drawScreen(final Graphics2D g, final String selected) {
		this.selectedOption = selected;
		
		// TITLE
		super.drawTitleText(g, title, Function.identity());

		// SCORE
		super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.scoreString + this.finalScore, x -> x,
				DisplayGameOver.textTile * gScreen.getTileSize(), super.getTextShift());
		
		// RECORD
		if(this.isNewRecord) {
			super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.recordString, x -> x/2,
					(DisplayGameOver.textTile + 1) * gScreen.getTileSize(), super.getTextShift());
		} else if(isNewPlayingRecord) {
			super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.playingRecordString[0], x -> x/2,
					(DisplayGameOver.textTile + 1) * gScreen.getTileSize(), super.getTextShift());
			super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.playingRecordString[1], x -> x/2,
					(DisplayGameOver.textTile + 2) * gScreen.getTileSize(), super.getTextShift());
		}

		// OPTIONS
		super.drawOptions(g, DisplayGameOver.optionTile);
	}

	public void setFinalScore(final int finalScore) {
		
		this.finalScore = finalScore;
		
		if (finalScore > DisplayGameOver.playingRecord) {
			DisplayGameOver.isNewPlayingRecord = true;
			DisplayGameOver.playingRecord = finalScore;
		} else if (finalScore < DisplayGameOver.playingRecord) {
			DisplayGameOver.isNewPlayingRecord = false;
		}

		if (finalScore > this.record) {
			this.isNewRecord = true;
			//StatisticsReader.writeRecord(finalScore); // TODO write new record
		} else if (finalScore < this.record) {
			this.isNewRecord = false;
		}
	}

	@Override
	protected Color getShiftColor() {
		return Color.DARK_GRAY;
	}	
}
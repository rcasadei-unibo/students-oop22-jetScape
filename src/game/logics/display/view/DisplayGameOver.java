package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.logics.records.Records;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplayGameOver extends Display {

	private final Records records;

	private static final int textTile = 3;
	private static final int optionTile = 7;
	private static String title = "Game Over";
	private static String scoreString = "Your score was: ";
	private static String recordScoreString = "NEW RECORD";
	private static String[] playingRecordScoreString = {"BARRY COULD", "LIVE LONGER"};
	
	//private int recordScore; // absolute new record
	private int finalScore;

	public DisplayGameOver(final Screen gScreen, final Records records) {
		super(gScreen);
		this.records = records;
		
		this.options.add(MenuOption.RETRY);
		this.options.add(MenuOption.MENU);
	}
	
	/**
	 *  Reads record and score from {@link Records}.
	 */
	private void updateRecords() {
		// TODO Check if this work and if it is good
		this.finalScore = this.records.getScore();
		//this.recordScore = this.records.getRecordScore();
	}

	public void drawScreen(final Graphics2D g, final MenuOption selected) {
		this.selectedOption = selected;
		
		this.updateRecords();

		// TITLE
		super.drawTitleText(g, title, Function.identity());

		// SCORE
		super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.scoreString + this.finalScore, x -> x,
				DisplayGameOver.textTile * gScreen.getTileSize(), super.getTextShift());
		
		// RECORD
		if(records.isNewRecordScore()) {
			super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.recordScoreString, x -> x/2,
					(DisplayGameOver.textTile + 1) * gScreen.getTileSize(), super.getTextShift());
		} else if(records.isNewPlayingRecordScore()) {
			super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.playingRecordScoreString[0], x -> x/2,
					(DisplayGameOver.textTile + 1) * gScreen.getTileSize(), super.getTextShift());
			super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.playingRecordScoreString[1], x -> x/2,
					(DisplayGameOver.textTile + 2) * gScreen.getTileSize(), super.getTextShift());
		}

		// OPTIONS
		super.drawOptions(g, DisplayGameOver.optionTile);
	}

	@Override
	protected Color getShiftColor() {
		return Color.DARK_GRAY;
	}	
}
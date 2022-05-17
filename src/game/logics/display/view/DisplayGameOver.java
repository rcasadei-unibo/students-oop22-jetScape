package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.logics.records.Records;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;

/**
 * <p>This class contains what it is shown when Barry dies.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplayGameOver extends Display implements MenuDisplay {

    private static final int TEXT_TILE = 3;
    private static final int OPTION_TILE = 7;
    private static String title = "Game Over";
    private static String scoreString = "Your score was: ";
    private static String recordScoreString = "NEW RECORD";
    private static String[] playingRecordScoreString = {"BARRY COULD", "LIVE LONGER"};

    private final Records records;

    //private static int playingRecord = 0; // higher score obtained by playing consecutively
    //private static boolean isNewPlayingRecord = false;

    //private int recordScore; // absolute new record
    //private boolean isNewRecord = false;
    private int finalScore;

    /**
     * {@link DisplayGameOver} constructor: add options to be shown.
     * @param gScreen
     */
    public DisplayGameOver(final Screen gScreen, final Records records) {
        super(gScreen);
        this.records = records;

        this.getOptions().add(MenuOption.RETRY);
        this.getOptions().add(MenuOption.MENU);
    }

    /**
     *  Reads record and score from {@link Records}.
     */
    private void updateRecords() {
        // TODO Check if this work and if it is good
        this.finalScore = this.records.getScore();
        //this.recordScore = this.records.getRecordScore();
    }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g, final MenuOption selected) {

        this.setSelectedOption(selected);
        this.updateRecords();

        // TITLE
        super.drawTitleText(g, title, Function.identity());

        // SCORE
        super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.scoreString + this.finalScore, x -> x,

        DisplayGameOver.TEXT_TILE * gScreen.getTileSize(), super.getTextShift());

        // RECORD
        if(records.isNewRecordScore()) {
            super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.recordScoreString, x -> x / 2,
                    (DisplayGameOver.TEXT_TILE + 1) * gScreen.getTileSize(), super.getTextShift());
        } else if(records.isNewPlayingRecordScore()) {
            super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.playingRecordScoreString[0], x -> x / 2,
                    (DisplayGameOver.TEXT_TILE + 1) * gScreen.getTileSize(), super.getTextShift());
            super.drawCenteredText(g, super.getTextFont(), DisplayGameOver.playingRecordScoreString[1], x -> x / 2,
                    (DisplayGameOver.TEXT_TILE + 2) * gScreen.getTileSize(), super.getTextShift());
        }

        // OPTIONS
        super.drawOptions(g, DisplayGameOver.OPTION_TILE);
    }

    /**
     * {@inheritDoc}
     */
    //TODO remove magic setting from here
    /*public void drawScreen(final Graphics2D g) {
        this.setSelectedOption(Optional.of(MenuOption.RETRY));
        this.drawScreen(g, Optional.of(this.getSelectedOption()));
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getShiftColor() {
        return Color.DARK_GRAY;
    }
}

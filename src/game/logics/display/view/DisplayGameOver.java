package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.UnaryOperator;

import game.logics.records.Records;
import game.utility.other.MenuOption;

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
    //private static String[] moneyString = {"You've collected: ", " coins"};
    private static String recordScoreString = "NEW RECORD";
    private static String[] playingRecordScoreString = {"BARRY COULD", "LIVE LONGER"};

    private final Records records;

    private int finalScore;
    private int finalMoney;

    /**
     * {@link DisplayGameOver} constructor: add options to be shown.
     *
     * @param records Object of type {@link Records} used to read what have to be displayed
     */
    public DisplayGameOver(final Records records) {
        super();
        this.records = records;

        this.getOptions().add(MenuOption.RETRY);
        this.getOptions().add(MenuOption.MENU);
    }

    /**
     *  Reads record and score from {@link Records}.
     */
    private void readRecords() {
        this.finalScore = this.records.getScore();
        this.finalMoney = this.records.getMoney();

        //this.recordScore = this.records.getRecordScore();
    }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g, final MenuOption selected) {

        this.setSelectedOption(selected);
        this.readRecords();

        // TITLE
        super.drawTitleText(g, title, UnaryOperator.identity());

        // SCORE
        super.drawCenteredText(g, super.getTextFont(),
                DisplayGameOver.scoreString + this.finalScore, x -> x / 2,
                DisplayGameOver.TEXT_TILE * super.getGameScreen().getTileSize(),
                super.getTextShift());

        // Use game.utility.sprites.Drawer to load a sprite

        // MONEY
        super.drawCenteredText(g, super.getTextFont(),
                " M " + this.finalMoney, x -> 2 * super.getGameScreen().getWidth() / 3,
                DisplayGameOver.TEXT_TILE * super.getGameScreen().getTileSize(),
                super.getTextShift());

        // RECORD
        if (records.isNewScoreRecord()) {
            super.drawCenteredText(g, super.getTextFont(),
                    DisplayGameOver.recordScoreString, x -> x / 3,
                    (DisplayGameOver.TEXT_TILE + 1) * super.getGameScreen().getTileSize(),
                    super.getTextShift());
        } else if (records.isNewPlayingScoreRecord()) {
            super.drawCenteredText(g, super.getTextFont(),
                    DisplayGameOver.playingRecordScoreString[0], x -> x / 3,
                    (DisplayGameOver.TEXT_TILE + 1) * super.getGameScreen().getTileSize(),
                    super.getTextShift());
            super.drawCenteredText(g, super.getTextFont(),
                    DisplayGameOver.playingRecordScoreString[1], x -> x / 3,
                    (DisplayGameOver.TEXT_TILE + 2) * super.getGameScreen().getTileSize(),
                    super.getTextShift());
        }

        // OPTIONS
        super.drawOptions(g, DisplayGameOver.OPTION_TILE);
    }

    // TODO: remove magic setting from here
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

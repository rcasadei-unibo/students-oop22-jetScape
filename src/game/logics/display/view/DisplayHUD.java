package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.frame.GameWindow;
import game.utility.screen.Screen;

/**
 * <p>This class contains what is shown when playing, so the score.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplayHUD extends Display {
    private static final int scoreTile = 1;
    private static final int scoreShift = 3;
    private static final int textXTile = 10;
    private static final double fontScale = 18;
    private final Font scoreFont;

    private int score;
    
    /**
     * {@link DisplayHUD} constructor: loads scoreFont.
     * @param gScreen
     */
    public DisplayHUD(final Screen gScreen) {
        super(gScreen);

        this.score = 0;
        this.scoreFont = GameWindow.GAME_FONTLOADER.getOptionsFont().deriveFont(getScaledSize(fontScale));
    }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g) {
        final String scoreString = "SCORE: " + this.score;

        // SCORE
        super.drawText(g, scoreFont, scoreString, super.gScreen.getTileSize() * textXTile,
                super.gScreen.getTileSize() * scoreTile, scoreShift);
    }

    /**
     * Update internal score to be shown.
     * @param score
     */
    public void updateScore(final int score) {
        this.score = score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getShiftColor() {
        return Color.DARK_GRAY;
    }
}

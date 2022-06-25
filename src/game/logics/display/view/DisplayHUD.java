package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.frame.GameWindow;

/**
 * <p>This class contains what is shown when playing, so the score.</p>
 * 
 * <p>This class extends {@link Display}.</p>
 */
public class DisplayHUD extends Display {
    private static final int SCORE_TILE = 1;
    private static final int SCORE_SHIFT = 3;
    private static final int TEXT_X_TILE = 10;
    private static final double FONT_SCALE = 18;
    private final Font scoreFont;

    /**
     * Actual score value, that have to be shown on screen.
     */
    private int score;
    /**
     * Actual number of coins collected, that have to be shown on screen.
     */
    private int coins;

    /**
     * {@link DisplayHUD} constructor: loads scoreFont.
     */
    public DisplayHUD() {
        super();

        this.score = 0;
        this.coins = 0;
        this.scoreFont = GameWindow.GAME_FONTLOADER.getOptionsFont().deriveFont(getScaledSize(FONT_SCALE));
    }

    /**
     * {@inheritDoc}
     */
    public void drawScreen(final Graphics2D g) {
        final String scoreString = "SCORE: " + this.score;
        final String coinsString = "COINS: " + this.coins;

        // SCORE
        super.drawText(g, scoreFont, scoreString,
                super.getGameScreen().getTileSize() * TEXT_X_TILE,
                super.getGameScreen().getTileSize() * SCORE_TILE, SCORE_SHIFT);

        // COINS
        super.drawText(g, scoreFont, coinsString,
                super.getGameScreen().getTileSize() * TEXT_X_TILE,
                super.getGameScreen().getTileSize() * (SCORE_TILE + 1), SCORE_SHIFT);
    }

    /**
     * Update internal {@link #score} variable to be shown on HUD.
     * @param score the new score value.
     */
    public void updateScore(final int score) {
        this.score = score;
    }

    /**
     * Update internal {@link #coins} counter to be shown on HUD.
     * @param coins the new number of coins collected.
     */
    public void updateCoins(final int coins) {
        this.coins = coins;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Color getShiftColor() {
        return Color.DARK_GRAY;
    }
}

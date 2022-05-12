package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import game.frame.GameWindow;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;

/**
 * Abstract class that contains useful methods for draw text on screen.
 *
 * @author davide
 */
public abstract class Display {
    protected final Screen gScreen;

    /*
     * List of options to be displayed.
     */
    private final List<MenuOption> options;
    private MenuOption selectedOption;

    /**
     * All text main color.
     */
    private static final Color COLOR = Color.white;
    /**
     * Title text default scale.
     */
    private static final double TITLE_SCALE = 5.14;
    /**
     * Selected text default scale.
     */
    private static final double SELECTED_SCALE = 9;
    /**
     * Option text default scale.
     */
    private static final double OPTIONS_SCALE = 12;

    /*
     * Fonts for every type of text
     */
    private final Font titleFont;
    private final Font optionsFont;
    private final Font textFont;
    private final Font selectedOptionsFont;

    /**
     * Default writing tile for text.
     */
    private int textTile = 5;

    /**
     * Default writing tile for options.
     */
    private static final int OPTION_TILE = 5;
    /**
     * Default writing tile for title.
     */
    private static final int TTITLE_TILE = 2;
    /**
     * options shadow shift.
     */
    private static final int optionShift = 2;
    private static final int titleShift = 5;
    private static final int textShift = 2;

    /**
     * Class constructor: load fonts with {@link game.utility.fonts.FontLoader}.
     * @param gScreen
     */
    public Display(final Screen gScreen) {
        this.gScreen = gScreen;

        this.titleFont = GameWindow.fLoader.getTitleFont()
                .deriveFont(getScaledSize(TITLE_SCALE));
        this.optionsFont = GameWindow.fLoader.getOptionsFont()
                .deriveFont(getScaledSize(OPTIONS_SCALE));
        this.selectedOptionsFont = GameWindow.fLoader.getOptionsFont()
                .deriveFont(getScaledSize(SELECTED_SCALE));
        this.textFont = GameWindow.fLoader.getTextFont()
                .deriveFont(getScaledSize(OPTIONS_SCALE));

        this.options = new ArrayList<>();
    }
    /**
     * Calculates the ordinate's value such as the given string is centered in
     * the current screen.
     * @param gScreen
     * @param g
     * @param text the string be centered
     * @return this ordinate's value
     */
    private int getCenteredX(final Screen gScreen, final Graphics2D g, final String text) {

        final int lenght = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();

        return gScreen.getWidth() / 2 - lenght / 2;
    }

    /**
     * Get list of menu options.
     * @return Display's menu options 
     */
    public List<MenuOption> getOptions() {
        return this.options;
    }

    /**
     * Get selected option.
     * @return Display's selected option 
     */
    protected MenuOption getSelectedOption() {
        return this.selectedOption;
    }

    /**
     * Set selected option.
     * @param selectedOption Display's new selected option 
     */
    protected void setSelectedOption(final MenuOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    protected void drawText(final Graphics2D g, /*final Color color,*/ final Font font,
            final String text, final int xPos, final int yPos, final int shift) {
        g.setFont(font);

        if (shift != 0) {
            g.setColor(this.getShiftColor());
            g.drawString(text, xPos + shift, yPos);
        }

        //g.setColor(color);
        g.setColor(Display.COLOR);
        g.drawString(text, xPos, yPos);
    }

    protected void drawCenteredText(final Graphics2D g, /*final Color color,*/ final Font font,
            final String text, final Function<Integer, Integer> f, final int yPos, final int shift) {
        g.setFont(font);
        this.drawText(g, font, text, f.apply(this.getCenteredX(gScreen, g, text)), yPos, shift);
    }

    protected void drawTitleText(final Graphics2D g, final String text, final Function<Integer, Integer> function) {
        this.drawCenteredText(g, this.titleFont, text, Function.identity(),
                gScreen.getTileSize() * Display.TTITLE_TILE, Display.titleShift);
    }

    /**
     * Draw list of options with custom yTile.
     * @param g
     * @param yTile custom value that is used to choose the vertical position.
     */
    protected void drawOptions(final Graphics2D g, final int yTile) {
        int i = 0;
        for (final MenuOption option : this.options) {
            if (option.equals(this.selectedOption)) {
                final String selected = "> " + option + " <";
                this.drawCenteredText(g, this.selectedOptionsFont, selected,
                        x -> x,
                        gScreen.getTileSize() * (i + yTile), Display.optionShift);
            } else {
                this.drawCenteredText(g, this.optionsFont, option.toString(),
                        x -> x,
                        gScreen.getTileSize() * (i + yTile), Display.optionShift);
            }
            i++;
        }
    }

    /**
     * Draw list of options with default yTile.
     * @param g
     */
    protected void drawOptions(final Graphics2D g) {
        this.drawOptions(g, Display.OPTION_TILE);
    }

    /**
     * Calculate scaled font size.
     *
     * @param scale
     * @return font's scaled size based on screen height
     */
    protected float getScaledSize(final double scale) {
        return (float) (gScreen.getHeight() / scale);
    }

    /**
     * Abstract method that is used to get chosen shift color from the child classes.
     * @return chosen Color
     */
    protected abstract Color getShiftColor();


    /**
     * Get standard title font.
     * @return Display.titleFont
     */
    protected Font getTitleFont() {
        return this.titleFont;
    }

    /**
     * Get standard text font.
     * @return Display.textFont
     */
    protected Font getTextFont() {
        return this.textFont;
    }

    /**
     * Get standard text shift.
     * @return Display.textShift
     */
    protected int getTextShift() {
        return Display.textShift;
    }
}

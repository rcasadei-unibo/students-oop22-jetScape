package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import game.frame.GameWindow;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;

/**
 * Abstract class that contains useful methods for draw text on screen.
 */
public abstract class Display {
    /**
     * The graphics drawer of type {@link Screen}.
     */
    private final Screen gScreen = GameWindow.GAME_SCREEN;

    /**
     * List of options to be displayed.
     */
    private final List<MenuOption> options;
    /**
     * The selected option among {@link #options}.
     */
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

    /**
     * Fonts for every type of text.
     */
    private final Font titleFont;
    private final Font optionsFont;
    private final Font textFont;
    private final Font selectedOptionsFont;

    /**
     * Default writing tile for options.
     */
    private static final int OPTION_TILE = 5;
    /**
     * Default writing tile for title.
     */
    private static final int TITLE_TILE = 2;

    /**
     * Shadow shift for options.
     */
    private static final int OPTIONS_SHIFT = 2;
    /**
     * Shadow shift for title.
     */
    private static final int TITLE_SHIFT = 5;
    /**
     * Shadow shift for normal text.
     */
    private static final int TEXT_SHIFT = 2;

    /**
     * Class constructor: load fonts with {@link game.utility.fonts.FontLoader}.
     */
    public Display() {
        this.titleFont = GameWindow.GAME_FONTLOADER.getTitleFont()
                .deriveFont(getScaledSize(TITLE_SCALE));
        this.optionsFont = GameWindow.GAME_FONTLOADER.getOptionsFont()
                .deriveFont(getScaledSize(OPTIONS_SCALE));
        this.selectedOptionsFont = GameWindow.GAME_FONTLOADER.getOptionsFont()
                .deriveFont(getScaledSize(SELECTED_SCALE));
        this.textFont = GameWindow.GAME_FONTLOADER.getTextFont()
                .deriveFont(getScaledSize(OPTIONS_SCALE));

        this.options = new ArrayList<>();
    }

    /**
     * Calculates the ordinate's value such as the given string is centered in
     * the current screen.
     * @param g the graphics drawer
     * @param text the string be centered
     * @return this ordinate's value
     */
    private int getCenteredX(final Graphics2D g, final String text) {

        final int length = (int) g.getFontMetrics()
                .getStringBounds(text, g)
                .getWidth();

        return gScreen.getWidth() / 2 - length / 2;
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

    /**
     * <p>This method is used to draw some text on screen.
     * This is the most exhaustive and generic version.</p>
     * 
     * <p>The default text color is {@link Display#COLOR}, while the shift
     * color is implementation-defined as it is passed by
     * {@link #getShiftColor}.</p>
     * 
     * <p>If the {@code shift} parameter is zero the shadow will not be drawn.</p>
     *
     * @param g the graphics drawer
     * @param font the character style of the text
     * @param text the string with the text that have to be printed on screen
     * @param xPos the horizontal text starting position (in pixel)
     * @param yPos the vertical text starting position (in pixel)
     * @param shift the shift that have to be applied on the text shadow
     */
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

    /**
     * <p>This method is used to draw some centered text on screen.
     * This is the most exhaustive method.</p>
     * 
     * <p>Explanation of how the function parameter works:<ul>
     * <li>passing to {@code f} a {@link UnaryOperator#identity}
     *   will center the text on the axis lying in the middle of the screen</li>
     * <li>passing {@code x -> x - WIDTH_OF_SCREEN / 4}
     *   will center the text on the axis corresponding to the first quarter of
     *   the screen</li>
     * <li>internally the text is first centered on to the median axis and
     *   later moved through the function parameter, so x corresponds to the
     *   value of the abscissa of the median axis.</li>
     * </ul></p>
     *
     * @param g the graphics drawer
     * @param font the character style of the text
     * @param text the string with the text that have to be printed on screen
     * @param function a function used to calculate the median axis to which the
     *          text to be written will be aligned
     * @param yPos the vertical text starting position (in pixel)
     * @param shift the shift that have to be applied on the text shadow
     *
     * @implNote this method calls {@link #drawText(Graphics2D g, Font font, String text, int xCalculated, int yPos, int shift)}
     */
    protected void drawCenteredText(final Graphics2D g, /*final Color color,*/ final Font font,
            final String text, final UnaryOperator<Integer> function, final int yPos, final int shift) {
        g.setFont(font); // this has to be called before this.getCenteredX
        this.drawText(g, font, text, function.apply(this.getCenteredX(g, text)), yPos, shift);
    }

    /**
     * Draw a title on screen.
     * This is a specialized version of {@link #drawCenteredText}
     *
     * @param g the graphics drawer
     * @param text the string with the text that have to be printed on screen
     * @param function a function used to calculate the median axis to which the
     *          text to be written will be aligned
     *
     * @implNote this method calls calls the aforementioned method using {@linkplain #titleFont font} and
     * {@linkplain Display#TITLE_SHIFT default} values, furthermore yPos is calculated.
     */
    protected void drawTitleText(final Graphics2D g, final String text, final UnaryOperator<Integer> function) {
        this.drawCenteredText(g, this.titleFont, text, UnaryOperator.identity(),
                gScreen.getTileSize() * Display.TITLE_TILE, Display.TITLE_SHIFT);
    }

    /**
     * Draw list of options with custom yTile.
     * @param g the graphics drawer
     * @param yTile custom value that is used to choose the vertical position.
     * 
     * @implNote The option shadow shift is {@link Display#OPTIONS_SHIFT}.
     */
    protected void drawOptions(final Graphics2D g, final int yTile) {
        int i = 0;
        for (final MenuOption option : this.options) {
            if (option.equals(this.selectedOption)) {
                final String selected = "> " + option + " <";
                this.drawCenteredText(g, this.selectedOptionsFont, selected,
                        x -> x,
                        gScreen.getTileSize() * (i + yTile), Display.OPTIONS_SHIFT);
            } else {
                this.drawCenteredText(g, this.optionsFont, option.toString(),
                        x -> x,
                        gScreen.getTileSize() * (i + yTile), Display.OPTIONS_SHIFT);
            }
            i++;
        }
    }

    /**
     * Draw list of options with default yTile.
     * @param g the graphics drawer
     * @implNote this method calls {@link #drawOptions(Graphics2D, int)} with
     * {@link Display#OPTION_TILE}.
     */
    protected void drawOptions(final Graphics2D g) {
        this.drawOptions(g, Display.OPTION_TILE);
    }

    /**
     * Calculate scaled font size.
     *
     * @param scale the scale inverse coefficient
     * @return font's scaled size based on screen height
     */
    protected float getScaledSize(final double scale) {
        return (float) (gScreen.getHeight() / scale);
    }

    /**
     * Abstract method that is used to get chosen shift {@link Color} from
     * the child classes.
     *
     * @return chosen shift {@link Color}, usually {@link Color#DARK_GRAY}
     */
    protected abstract Color getShiftColor();

    /**
     * Get standard title font.
     * @return {@link #titleFont}
     */
    protected Font getTitleFont() {
        return this.titleFont;
    }

    /**
     * Get standard options font.
     * @return {@link #optionsFont}
     */
    protected Font getOptionsFont() {
        return this.optionsFont;
    }

    /**
     * Get standard text font.
     * @return {@link #textFont}
     */
    protected Font getTextFont() {
        return this.textFont;
    }

    /**
     * Get standard text shift.
     * @return {@link Display#TEXT_SHIFT}
     */
    protected int getTextShift() {
        return Display.TEXT_SHIFT;
    }

    /**
     * Get standard option shift.
     * @return {@link Display#OPTIONS_SHIFT}
     */
    protected int getOptionShift() {
        return Display.OPTIONS_SHIFT;
    }

    /**
     * Get current game screen.
     * @return {@link #gScreen}
     */
    protected Screen getGameScreen() {
       return gScreen;
    }
}

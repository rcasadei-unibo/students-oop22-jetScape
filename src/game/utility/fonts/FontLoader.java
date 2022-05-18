package game.utility.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

// TODO: Add javadoc
/**
 * Complete me.
 */
public class FontLoader {
    private static final String SEPARATOR = System.getProperty("file.separator");
    // TODO: Add javadoc
    /**
     * Complete me.
     */
    public static final String DEFAULT_DIR = System.getProperty("user.dir") + SEPARATOR
            + "res" + SEPARATOR + "game" + SEPARATOR + "fonts" + SEPARATOR;
    private final Font debuggerFont;
    private final Font titleFont;
    private final Font optionsFont;
    private final Font textFont;

    // TODO: Add javadoc
    /**
     * Complete me.
     */
    public FontLoader() {
        this.debuggerFont = this.loadFont(DEFAULT_DIR + "debuggerFont.otf");
        this.titleFont = this.loadFont(DEFAULT_DIR + "titleFont.ttf");
        this.optionsFont = this.loadFont(DEFAULT_DIR + "optionsFont.otf");
        this.textFont = this.loadFont(DEFAULT_DIR + "textFont.ttf");
    }

    private Font loadFont(final String name) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(name));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    // TODO: Add javadoc
    /**
     * Complete me.
     * @return complete
     */
    public Font getDebuggerFont() {
        return debuggerFont;
    }

    // TODO: Add javadoc
    /**
     * Complete me.
     * @return complete
     */
    public Font getTitleFont() {
        return titleFont;
    }

    // TODO: Add javadoc
    /**
     * Complete me.
     * @return complete
     */
    public Font getOptionsFont() {
        return optionsFont;
    }

    // TODO: Add javadoc
    /**
     * Complete me.
     * @return complete
     */
    public Font getTextFont() {
        return textFont;
    }

}

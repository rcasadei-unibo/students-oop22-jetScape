package game.utility.fonts;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    private static final String separator = System.getProperty("file.separator");
    public static final String defaultDir = System.getProperty("user.dir") + separator + 
            "res" + separator + "game" + separator + "fonts" + separator;
    private final Font debuggerFont;
    private final Font titleFont;
    private final Font optionsFont;
    private final Font textFont;
    
    public FontLoader() {
        this.debuggerFont = this.loadFont(defaultDir + "debuggerFont.otf");
        this.titleFont = this.loadFont(defaultDir + "titleFont.ttf");
        this.optionsFont = this.loadFont(defaultDir + "optionsFont.otf");
        this.textFont = this.loadFont(defaultDir + "textFont.ttf");
    }
    
    private Font loadFont(String name) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(name));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }

    public Font getDebuggerFont() {
        return debuggerFont;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public Font getOptionsFont() {
        return optionsFont;
    }

    public Font getTextFont() {
        return textFont;
    }

}

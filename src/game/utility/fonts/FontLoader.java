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
	private final Font gameFont;
	private final Font optionsFont;
	
	public FontLoader() {
		this.debuggerFont = this.loadFont(defaultDir + "debuggerFont.otf");
		this.gameFont = this.loadFont(defaultDir + "titleFont.ttf");
		this.optionsFont = this.loadFont(defaultDir + "optionsFont.otf");
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

	public Font getGameFont() {
		return gameFont;
	}

	public Font getOptionsFont() {
		return optionsFont;
	}

}

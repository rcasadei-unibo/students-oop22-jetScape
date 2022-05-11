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

public abstract class Display {
	protected final Screen gScreen;

	protected final List<MenuOption> options;
	protected MenuOption selectedOption;

	/**
	 * All text main color.
	 */
	private static final Color COLOR = Color.white;

	private static final double titleScale = 5.14;
	private static final double selectedScale = 9;
	private static final double optionsScale = 12;

	/*
	 * Fonts for every type of text
	 */
	private final Font titleFont;
	private final Font optionsFont;
	private final Font textFont;
	private final Font selectedOptionsFont;

	/**
	 * default writing tile for text
	 */
	protected int textTile = 5;
	
	/**
	 * default writing tile for options
	 */
	private static final int optionTile = 5;
	/**
	 * options shadow shift 
	 */
	private static final int optionShift = 2;

	private static final int titleTile = 2;
	private static final int titleShift = 5;
	
	private static final int textShift = 2;
	
	public Display(final Screen gScreen) {
		this.gScreen = gScreen;

		this.titleFont = GameWindow.fLoader.getTitleFont()
				.deriveFont(getScaledSize(titleScale));
		this.optionsFont = GameWindow.fLoader.getOptionsFont()
				.deriveFont(getScaledSize(optionsScale));
		this.selectedOptionsFont = GameWindow.fLoader.getOptionsFont()
				.deriveFont(getScaledSize(selectedScale));
		this.textFont = GameWindow.fLoader.getTextFont()
				.deriveFont(getScaledSize(optionsScale));

		this.options = new ArrayList<>();
	}
	/**
	 * @return the ordinate's value such as the given string is centered in the current screen
	 */
	private int getCenteredX(final Screen gScreen, final Graphics2D g, final String text) {
		final int lenght = (int) g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return gScreen.getWidth()/2 - lenght/2;
	}
	
	/**
	 * @return Display's menu options 
	 */
	public List<MenuOption> getOptions() {
		return this.options;
	}
	
	protected void drawText(final Graphics2D g, /*final Color color,*/ final Font font,
			final String text, final int xPos, final int yPos, final int shift) {
		g.setFont(font);
	
		if(shift != 0) {
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
				gScreen.getTileSize() * Display.titleTile, Display.titleShift);
	}
	
	protected void drawOptions(final Graphics2D g, final int yTile) {
		int i = 0;
		for(final MenuOption option : this.options) {
			if(option.equals(this.selectedOption)) {
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

	protected void drawOptions(final Graphics2D g) {
		this.drawOptions(g, Display.optionTile);
	}

	protected Font getTitleFont() {
		return this.titleFont;
	}

	protected abstract Color getShiftColor();

	protected Font getTextFont() {
		return this.textFont;
	}

	protected int getTextShift() {
		return Display.textShift;
	}

	/**
	 * @return font's scaled size based on screen height 
	 */
	protected float getScaledSize(final double scale) {
		return (float) (gScreen.getHeight() / scale);
	}
}

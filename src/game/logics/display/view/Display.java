package game.logics.display.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import game.frame.GameWindow;
import game.utility.other.GameState;
import game.utility.screen.Screen;

public abstract class Display {
	protected final Screen gScreen;
	protected String selectedOption;
	protected String firstOption = "";
	protected final Map<String,GameState> options = new HashMap<>();
	private final List<String> textOptions = new ArrayList<>();
	
	private static final Font titleFont = GameWindow.fLoader.getTitleFont().deriveFont(112f);
	private static final Font textFont = GameWindow.fLoader.getTextFont().deriveFont(36f);
	private static final Font optionFont = GameWindow.fLoader.getOptionsFont().deriveFont(48f);
	private static final Font selectedOptionFont = GameWindow.fLoader.getOptionsFont().deriveFont(64f);
	
	private static final Color color = Color.white;
	
	private static final int optionTile = 5;
	private static final int optionShift = 2;

	private static final int titleTile = 2;
	private static final int titleShift = 5;
	
	private static final int textShift = 2;
	
	public Display(final Screen gScreen) {
		this.gScreen = gScreen;
	}
	
	public void buildTextOptions(final String firstOption) {
		if(this.textOptions.isEmpty()) {
			this.textOptions.add(firstOption);
			for(final String option : this.options.keySet().stream()
					.filter(s -> s.compareTo(firstOption) != 0)
					.collect(Collectors.toSet())) {
				this.textOptions.add(option);
			}
		}
	}
	
	private int getCenteredX(final Screen gScreen, final Graphics2D g, final String text) {
		int lenght = (int) g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return gScreen.getWidth()/2 - lenght/2;
	}
	
	public Map<String,GameState> getOptions() {
		return this.options;
	}
	
	public String getFirstOption() {
		return this.firstOption;
	}
	
	public List<String> getOrderedText() {
		return this.textOptions;
	}
	
	protected void drawText(final Graphics2D g, /*final Color color,*/ final Font font,
			final String text, final int xPos, final int yPos, final int shift) {
		g.setFont(font);
	
		if(shift != 0) {
			g.setColor(this.getShiftColor());
			g.drawString(text, xPos + shift, yPos);
		}
	
		//g.setColor(color);
		g.setColor(Display.color);
		g.drawString(text, xPos, yPos);
	}
	
	protected void drawCenteredText(final Graphics2D g, /*final Color color,*/ final Font font,
			final String text, final Function<Integer, Integer> f, final int yPos, final int shift) {
		g.setFont(font);
		this.drawText(g, font, text, f.apply(this.getCenteredX(gScreen, g, text)), yPos, shift);
	}
	
	protected void drawTitleText(final Graphics2D g, final String text, final Function<Integer, Integer> function) {
		this.drawCenteredText(g, Display.titleFont, text, Function.identity(),
				gScreen.getTileSize() * Display.titleTile, Display.titleShift);
	}
	
	protected void drawOptions(final Graphics2D g, final int yTile) {
		int i = 0;
		for(final String option : this.textOptions) {
			if(option.equals(this.selectedOption)) {
				String selected = "> "+option+" <";
				this.drawCenteredText(g, Display.selectedOptionFont, selected,
						x -> x,
						gScreen.getTileSize() * (i + yTile), Display.optionShift);
			} else {
				this.drawCenteredText(g, Display.optionFont, option,
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
		return Display.titleFont;
	}
	
	protected abstract Color getShiftColor();
	
	protected Font getTextFont() {
		return Display.textFont;
	}
	
	protected int getTextShift() {
		return Display.textShift;
	}
}

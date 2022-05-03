package game.logics.display.view;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import game.utility.other.GameState;
import game.utility.screen.Screen;


public abstract class Display {
	protected int textTile = 5;
	protected final Screen gScreen;
	protected String selectedOption;
	protected String firstOption = "";
	protected final Map<String,GameState> options = new HashMap<>();
	private final List<String> text = new ArrayList<>();
	
	protected static final Font titleFont = new Font("magneto", Font.PLAIN, 112);
	protected static final Font textFont = new Font("calibri", Font.PLAIN, 48);
	protected static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	
	public Display(final Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}
	
	public void buildText(final String firstOption) {
		if(this.text.isEmpty()) {
			this.text.add(firstOption);
			for(String option : this.options.keySet().stream()
					.filter(s -> s.compareTo(firstOption) != 0)
					.collect(Collectors.toSet())) {
				this.text.add(option);
			}
		}
	}
	
	public int getCenteredX(final Screen gScreen, final Graphics2D g, final String text) {
		int lenght = (int)g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return gScreen.getWidth()/2 - lenght/2;
	}
	
	public Map<String,GameState> getOptions() {
		return this.options;
	}
	
	public String getFirstOption() {
		return this.firstOption;
	}
	
	public List<String> getOrderedText() {
		return this.text;
	}
	
	protected void drawText(final Graphics2D g, final int shift) {
		int i = 0;
		for(String option : this.text) {
			if(option.equals(this.selectedOption)) {
				g.setFont(DisplayMainMenu.selectedTextFont);
				String selected = "> "+option+" <";
				g.drawString(selected, this.getCenteredX(gScreen, g, selected) + shift,
						gScreen.getTileSize() * (this.textTile + i));
			} else {
				g.setFont(DisplayMainMenu.textFont);
				g.drawString(option, this.getCenteredX(gScreen, g, option) + shift,
					gScreen.getTileSize() * (this.textTile + i));
			}
			i++;
		}
	}
	
	protected void setTextTile(final int textTile) {
		this.textTile = textTile;
	}
	
}

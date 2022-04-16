package game.logics.display.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public abstract class Display {
	static final int textTile = 6;
	protected final Screen gScreen;
	protected String selectedOption;
	protected String firstOption = "";
	protected final Map<String,GameState> options = new HashMap<>();
	private final List<String> text = new ArrayList<>();
	
	public Display(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}
	
	public void buildText(String firstOption) {
		if(this.text.isEmpty()) {
			this.text.add(firstOption);
			for(String option : this.options.keySet().stream().filter(s -> s.compareTo(firstOption) != 0).collect(Collectors.toSet())) {
				this.text.add(option);
			}
		}
	}
	
	public int getCenteredX(Screen gScreen, Graphics2D g, String text) {
		int lenght = (int)g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return gScreen.getWidth()/2 - lenght/2;
	}
	
	public Map<String,GameState> getOptions(){
		return this.options;
	}
	
	public String getFirstOption() {
		return firstOption;
	}
	
	public List<String> getOrderedText(){
		return text;
	}
	
	protected void drawText(Graphics2D g, int shift) {
		int i = 0;
		for(String option : this.text) {
			if(option.equals(this.selectedOption)) {
				g.setFont(DisplayMainMenu.selectedTextFont);
				String selected = "> "+option+" <";
				g.drawString(selected, this.getCenteredX(gScreen, g, selected) + shift,
						gScreen.getTileSize() * (textTile + i));
			} else {
				g.setFont(DisplayMainMenu.fontText);
				g.drawString(option, this.getCenteredX(gScreen, g, option) + shift,
					gScreen.getTileSize() * (textTile + i));
			}
			i++;
		}
	}
	
	
}

package game.logics.display.view;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

public abstract class Display {
	static final int textTile = 6;
	protected final Screen gScreen;
	protected Pair<String,Integer> selectedOption;
	protected final Map<String,GameState> options = new HashMap<>() ;
	private final Map<String,Integer> text = new HashMap<>();
	
	public Display(Screen gScreen) {
		super();
		this.gScreen = gScreen;
	}
	
	public void buildText(Graphics2D g) {
		if(this.text.isEmpty()) {
			for(String option : this.options.keySet()) {
				this.text.put(option, this.getCenteredX(gScreen, g, option));
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
	
	protected void drawText(Graphics2D g, int shift) {
		int i = 0;
		for(String option : this.text.keySet()) {
			if(!option.equals(this.selectedOption.getX())) {
				g.drawString(option, this.text.get(option) + shift,
						gScreen.getTileSize() * (textTile + i));
			}
			i++;
		}
	}
	
	
}

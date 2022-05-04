package game.logics.display.view;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.frame.GameWindow;
import game.utility.other.MenuOption;
import game.utility.screen.Screen;


public abstract class Display {
	static final double titleScale = 5.14;
	static final double selectedScale = 9;
	static final double optionsScale = 12;	
	protected final Screen gScreen;
	protected final List<MenuOption> options;
	protected final Font titleFont;
	protected final Font textFont;
	protected final Font selectedTextFont;
	protected int textTile = 5;
	protected MenuOption selectedOption;
	
	public Display(final Screen gScreen) {
		super();
		this.gScreen = gScreen;
		this.titleFont = GameWindow.fLoader.getTitleFont()
				.deriveFont(getScaledSize(textTile));
		this.selectedTextFont = GameWindow.fLoader.getOptionsFont()
				.deriveFont(getScaledSize(selectedScale));
		this.textFont = GameWindow.fLoader.getOptionsFont()
				.deriveFont(getScaledSize(optionsScale));
		this.options = new ArrayList<>();
	}
	
	public int getCenteredX(final Screen gScreen, final Graphics2D g, final String text) {
		int lenght = (int)g.getFontMetrics().getStringBounds(text,g).getWidth();
		
		return gScreen.getWidth()/2 - lenght/2;
	}
	
	public List<MenuOption> getOptions() {
		return this.options;
	}
	
	protected void drawText(final Graphics2D g, final int shift) {
		int i = 0;
		for(MenuOption option : this.options) {
			if(option.equals(this.selectedOption)) {
				g.setFont(this.selectedTextFont);
				String selected = "> " + option.toString() + " <";
				g.drawString(selected, this.getCenteredX(gScreen, g, selected) + shift,
						gScreen.getTileSize() * (this.textTile + i));
			} else {
				g.setFont(this.textFont);
				g.drawString(option.toString(), 
						this.getCenteredX(gScreen, g, option.toString()) + shift,
					gScreen.getTileSize() * (this.textTile + i));
			}
			i++;
		}
	}
	
	protected void setTextTile(final int textTile) {
		this.textTile = textTile;
	}
	
	protected float getScaledSize(double scale) {
		return (float)(gScreen.getHeight()/scale);
	}
	
}

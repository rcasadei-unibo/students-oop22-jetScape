package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplayPause extends Display {

	private static final String title = "Paused";

	public DisplayPause(final Screen gScreen) {
		super(gScreen);
		this.options.add(MenuOption.RESUME);
		this.options.add(MenuOption.MENU);
	}

	public void drawScreen(final Graphics2D g, final MenuOption selected) {
		super.selectedOption = selected;

		// TITLE
		super.drawTitleText(g, title, Function.identity());
		
		// OPTIONS
		super.drawOptions(g);
	}
	
	@Override
	protected Color getShiftColor() {
		return Color.BLACK;
	}	
}

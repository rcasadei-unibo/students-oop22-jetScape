package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.MenuOption;
import game.utility.screen.Screen;

public class DisplayMainMenu extends Display {

	private static final String title = "JetScape";

	public DisplayMainMenu(final Screen gScreen) {
		super(gScreen);

		this.options.add(MenuOption.START);
		this.options.add(MenuOption.SHOP);
		this.options.add(MenuOption.QUIT);
		this.options.add(MenuOption.RECORDS);
	}
	
	public void drawScreen(final Graphics2D g, final MenuOption selected) {
		this.selectedOption = selected;
		
		// TITLE
		super.drawTitleText(g, title, Function.identity());
		
		// OPTIONS
		super.drawOptions(g);
	}
	
	@Override
	protected Color getShiftColor() {
		return Color.DARK_GRAY;
	}
}

package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayMainMenu extends Display {
	static final String title = "JetScape";
	static final GameState currentGS = GameState.MENU;

	public DisplayMainMenu(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Start";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Shop", GameState.MENU);
		this.options.put("Records", GameState.RECORDS);
		this.options.put("Quit", GameState.EXIT);
		this.buildTextOptions(firstOption);
	}
	
	public void drawScreen(final Graphics2D g, final String selected) {
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

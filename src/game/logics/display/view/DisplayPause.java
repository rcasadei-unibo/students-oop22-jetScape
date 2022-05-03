package game.logics.display.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

import game.utility.other.GameState;
import game.utility.screen.Screen;

public class DisplayPause extends Display {
	private static final String title = "Paused";
	private static final GameState currentGS = GameState.PAUSED;

	public DisplayPause(final Screen gScreen) {
		super(gScreen);
		
		firstOption = "Resume";
		this.options.put(firstOption, GameState.INGAME);
		this.options.put("Back to menu", GameState.MENU);
		this.buildTextOptions(firstOption);
	}

	public void drawScreen(final Graphics2D g, final String selected) {
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

package game.display;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.awt.Color;

import game.logics.handler.DisplayHandler;
import game.logics.handler.MenuHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

public class DisplayPause implements Display {
	static final int titleTile = 2;
	static final int textTile = 6;
	static final int titleShift = 3;
	static final int textShift = 2;
	static final String title = "Paused";
	static final Font font = new Font("Stencil", Font.PLAIN, 112);
	static final Font fontText = new Font("calibri", Font.PLAIN, 48);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	static final GameState currentGS = GameState.PAUSED;
	
	private final Map<String,GameState> options = new HashMap<>();
	private final Map<String,Integer> text = new HashMap<>();
	private final Screen gScreen;
	private final DisplayHandler pauseH;
	
	private int x = 0;
	private Pair<String,Integer> selectedOption;

	public DisplayPause(Screen gScreen, KeyHandler keyH) {
		super();
		this.gScreen = gScreen;
		this.options.put("Resume", GameState.INGAME);
		this.options.put("Back to menu", GameState.MENU);
		this.pauseH = new MenuHandler(this.options, keyH, DisplayPause.currentGS);
	}

	@Override
	public void drawScreen(Graphics2D g) {
		this.selectedOption = this.pauseH.getSelectedOption();
		//TITLE SHADOW
		g.setColor(Color.black);
		g.setFont(font);
		x = this.getCenteredX(gScreen, g, title);
		g.drawString(title, x + titleShift, gScreen.getTileSize() * titleTile);
		//TITLE
		g.setColor(Color.white);
		g.drawString(title, x, gScreen.getTileSize() * titleTile);
		//CREATE TEXT LIST
		g.setFont(DisplayMainMenu.fontText);
		this.buildText(g);
		//MESSAGE SHADOW
		g.setColor(Color.black);
		this.drawText(g, textShift);
		//MESSAGE
		g.setColor(Color.white);
		this.drawText(g,0);
		//SELECTED TEXT SHADOW
		g.setColor(Color.darkGray);
		g.setFont(DisplayMainMenu.selectedTextFont);
		this.selectedOption.setX("> "+ this.selectedOption.getX() +" <");
		x = this.getCenteredX(gScreen, g, selectedOption.getX());
		g.drawString(selectedOption.getX(), x + textShift,
				gScreen.getTileSize() *(textTile + this.selectedOption.getY()));
		// SELECTED TEXT 
		g.setColor(Color.white);
		g.drawString(selectedOption.getX(), x, gScreen.getTileSize() *(textTile + this.selectedOption.getY()));
	}
	
	@Override
	public Optional<DisplayHandler> getHandler() {
		return Optional.of(this.pauseH);
	}
	
	private void buildText(Graphics2D g) {
		if(this.text.isEmpty()) {
			for(String option : this.options.keySet()) {
				this.text.put(option, this.getCenteredX(gScreen, g, option));
			}
		}
	}
	
	private void drawText(Graphics2D g, int shift) {
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

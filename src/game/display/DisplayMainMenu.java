package game.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import game.logics.handler.DisplayHandler;
import game.logics.handler.MenuHandler;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

public class DisplayMainMenu implements Display {
	static final int titleTile = 2;
	static final int textTile = 6;
	static final int titleShift = 5;
	static final int textShift = 2;
	static final String title = "JetScape";
	static final Font font = new Font("magneto", Font.PLAIN, 112);
	static final Font fontText = new Font("calibri", Font.PLAIN, 48);
	static final Font selectedTextFont = new Font("calibri", Font.BOLD, 64);
	static final GameState currentGS = GameState.MENU;
	
	private final Map<String,GameState> options = new HashMap<>() ;
	private final Map<String,Integer> text = new HashMap<>();
	private final Screen gScreen;
    private final DisplayHandler menuH;
	
	private int x = 0;
	private Pair<String,Integer> selectedOption;

	public DisplayMainMenu(Screen gScreen, KeyHandler keyH) {
		super();
		this.gScreen = gScreen;
		this.options.put("Start", GameState.INGAME);
		this.options.put("Shop", GameState.MENU);
		this.options.put("Quit", GameState.EXIT);
		this.menuH = new MenuHandler(this.options, keyH, DisplayMainMenu.currentGS);
	}
	
	@Override
	public void drawScreen(Graphics2D g) {
		this.selectedOption = this.menuH.getSelectedOption();
		//TITLE SHADOW
		g.setColor(Color.darkGray);
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
		g.setColor(Color.darkGray);
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
		return Optional.of(this.menuH);
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

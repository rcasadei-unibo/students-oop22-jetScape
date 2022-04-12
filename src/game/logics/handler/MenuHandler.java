package game.logics.handler;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

import game.display.Display;
import game.display.DisplayMainMenu;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;
import game.utility.screen.Screen;

public class MenuHandler implements DisplayHandler {
	static final GameState currentGS = GameState.MENU;
	private final Display menu ;
	private final List<Pair<String,GameState>> options;
	private final KeyHandler keyH;
	private int cursor = 0;
	
	public MenuHandler(KeyHandler keyH, Screen gScreen) {
		super();
		this.options = List.of(new Pair<>("Start",GameState.INGAME),
				new Pair<>("Shop",GameState.MENU),
				new Pair<>("Exit",GameState.EXIT));
		this.keyH = keyH;
		this.menu = new DisplayMainMenu(gScreen);
	}

	public GameState handle () {
		if (this.keyH.isKeyTyped(KeyEvent.VK_UP)) {
			this.goUp();
		} else if (this.keyH.isKeyTyped(KeyEvent.VK_DOWN)) {
			this.goDown();
		} else if (this.keyH.input.get("enter")) {
			return this.options.get(this.cursor).getY();
		}
		return currentGS;
	}
	
	public void draw(Graphics2D g) {
		this.menu.setCursorIndex(cursor);
		this.menu.drawScreen(g);
	}
	
	private void goUp () {
		this.cursor--;
		if(this.cursor < 0) {
			this.cursor = this.options.size() - 1;
		}
	}
	
    private void goDown () {
    	this.cursor++;
		if(this.cursor > this.options.size() - 1) {
			this.cursor = 0;
		}
	}

}

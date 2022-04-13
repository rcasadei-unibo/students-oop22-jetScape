package game.logics.handler;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

import game.display.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;

public class MenuHandler implements DisplayHandler {
	private final Display menu ;
	private final List<Pair<String,GameState>> options;
	private final KeyHandler keyH;
	private int cursor = 0;
	private final GameState currentGameState;

	public MenuHandler(Display menu, List<Pair<String, GameState>> options, KeyHandler keyH,
			GameState currentGameState) {
		super();
		this.menu = menu;
		this.options = options;
		this.keyH = keyH;
		this.currentGameState = currentGameState;
	}

	public GameState handle () {
		if (this.keyH.isKeyTyped(KeyEvent.VK_UP)) {
			this.goUp();
		} else if (this.keyH.isKeyTyped(KeyEvent.VK_DOWN)) {
			this.goDown();
		} else if (this.keyH.isKeyTyped(KeyEvent.VK_ENTER)) {
			return this.options.get(this.cursor).getY();
		}
		return currentGameState;
	}
	
	public void draw(Graphics2D g) {
		this.menu.setCursorIndex(cursor);
		this.menu.drawScreen(g, this.options);
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

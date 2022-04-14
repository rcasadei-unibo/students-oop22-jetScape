package game.logics.handler;

import java.awt.event.KeyEvent;
import java.util.Map;

import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;

public class MenuHandler implements DisplayHandler {
	private final Map <String,GameState> options;
	private final KeyHandler keyH;
	private final GameState currentGameState;
	private int cursor = 0;
	private Pair<String,Integer> selectedOption;

	public MenuHandler(Map <String, GameState> options, KeyHandler keyH,
			GameState currentGameState) {
		super();
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
			return this.options.get(this.getSelectedOption().getX());
		}
		return currentGameState;
	}
	
	public Pair<String,Integer> getSelectedOption() {
		this.updateSelectedOption();
		return this.selectedOption;
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
    
    private void updateSelectedOption() {
    	int i = 0;
		for (String s : this.options.keySet()) {
			if(i == cursor) {
				this.selectedOption = new Pair<>(s,cursor);
				break;
			}
			i++;
		}
    }

}

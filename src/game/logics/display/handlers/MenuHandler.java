package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.function.Consumer;

import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Pair;

public class MenuHandler implements DisplayHandler {
	private final KeyHandler keyH;
	private final Display display;
	private final Map <String,GameState> options;
	private final Consumer <GameState> setGameState;
	
	private int cursor = 0;
	private Pair<String,Integer> selectedOption;

	public MenuHandler(KeyHandler keyH, Display display, Consumer<GameState> setGameState) {
		super();
		this.keyH = keyH;
		this.display = display;
		this.options = display.getOptions();
		this.setGameState = setGameState;
	}

	public void handle () {
		switch(this.keyH.getLastKeyTyped()) {
		case KeyEvent.VK_UP :
			this.goUp();
			break;
		case KeyEvent.VK_DOWN:
			this.goDown();
			break;
		case KeyEvent.VK_ENTER:
			this.setGameState.accept(this.options.get(this.getSelectedOption().getX()));
			break;
		default:
			break;
		}
	}
	
	public Pair<String,Integer> getSelectedOption() {
		this.updateSelectedOption();
		return this.selectedOption;
	
	}
	
	public Display getDisplay() {
		return this.display;
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

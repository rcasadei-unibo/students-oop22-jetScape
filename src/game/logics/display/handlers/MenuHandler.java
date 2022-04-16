package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;

public class MenuHandler implements DisplayHandler {
	private final KeyHandler keyH;
	private final Display display;
	private final Map <String,GameState> options;
	private final List<String> text;
	private final Consumer<GameState> setGameState;
	
	private int cursor = 0;
	private String selectedOption;

	public MenuHandler(KeyHandler keyH, Display display, Consumer<GameState> setGameState) {
		super();
		this.keyH = keyH;
		this.display = display;
		this.options = display.getOptions();
		this.selectedOption = display.getFirstOption();
		this.text = display.getOrderedText();
		this.setGameState = setGameState;
	}

	public void update() {
		switch(keyH.getKeyTyped()) {
			case KeyEvent.VK_UP :
				this.goUp();
				keyH.resetKeyTyped();
				break;
			case KeyEvent.VK_DOWN:
				this.goDown();
				keyH.resetKeyTyped();
				break;
			case KeyEvent.VK_ENTER:
				this.setGameState.accept(this.options.get(this.getSelectedOption()));
				keyH.resetKeyTyped();
				break;
			default:
				break;
		}
	}
	
	public String getSelectedOption() {
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
		for (String s : this.text) {
			if(i == cursor) {
				this.selectedOption = s;
				break;
			}
			i++;
		}
    }

}

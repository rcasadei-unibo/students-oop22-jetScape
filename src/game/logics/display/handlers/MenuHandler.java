package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.MenuOption;

public class MenuHandler implements DisplayHandler {
	private final KeyHandler keyH;
	private final Display display;
	private final List<MenuOption> options;
	private final Consumer<GameState> setGameState;
	
	private int cursor = 0;
	private MenuOption selectedOption;

	public MenuHandler(KeyHandler keyH, Display display, Consumer<GameState> setGameState) {
		super();
		this.keyH = keyH;
		this.display = display;
		this.options = display.getOptions();
		this.selectedOption = options.get(0);
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
				this.setGameState.accept(this.selectedOption.getOptionsGS());
				keyH.resetKeyTyped();
				break;
			default:
				break;
		}
	}
	
	public MenuOption getSelectedOption() {
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
		for (MenuOption s : this.options) {
			if(i == cursor) {
				this.selectedOption = s;
				break;
			}
			i++;
		}
    }

}

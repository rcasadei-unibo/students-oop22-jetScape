package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.MenuOption;
/**
 * The <code>MenuHandler</code> class manages <class>Display</class> menus
 * 
 * @author Giacomo Amadio
 */
public class MenuHandler implements DisplayHandler {
    private final KeyHandler keyH;
    private final Consumer<GameState> setGameState;
    
    /**
     * current display's menu options
     */
    private final List<MenuOption> options;
    
    /**
     * current cursor index
     */
    private int cursor = 0;
    
    /**
     * current selected option
     */
    private MenuOption selectedOption;

    public MenuHandler(KeyHandler keyH, Display display, Consumer<GameState> setGameState) {
        super();
        this.keyH = keyH;
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
    
    /**
     * moves the cursor up 
     */
    private void goUp () {
        this.cursor--;
        if(this.cursor < 0) {
            this.cursor = this.options.size() - 1;
        }
    }
    
    /**
     * moves the cursor down 
     */
    private void goDown () {
        this.cursor++;
        if(this.cursor > this.options.size() - 1) {
            this.cursor = 0;
        }
    }
    
    /**
     * updates the selected option
     */
    private void updateSelectedOption() {
        this.selectedOption = this.options.get(this.cursor);
    }

}

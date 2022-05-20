package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.MenuOption;
/**
 * The {@link MenuHandler} class manages {@link Display} menus.
 */
public class MenuHandler implements DisplayHandler {
    private final KeyHandler keyH;
    private final Consumer<GameState> setGameState;

    /**
     * current display's menu options.
     */
    private final List<MenuOption> options;

    /**
     * current cursor index.
     */
    private int cursor;

    /**
     * current selected option.
     */
    private MenuOption selectedOption;

    /**
     * Initializes the menu handler with the given display's options and sets 
     * the first one as currently selected, performing setGameState when enter is pressed.
     * @param keyH
     * @param display
     * @param setGameState
     */
    public MenuHandler(final KeyHandler keyH, final Display display, final Consumer<GameState> setGameState) {
        super();
        this.keyH = keyH;
        this.cursor = 0;
        this.options = display.getOptions();
        this.selectedOption = options.get(this.cursor);
        this.setGameState = setGameState;
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        switch (keyH.getKeyTyped()) {
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
    /**
     * {@inheritDoc}
     */
    public MenuOption getSelectedOption() {
        this.updateSelectedOption();
        return this.selectedOption;
    }

    /**
     * moves the cursor up .
     */
    private void goUp() {
        this.cursor--;
        if (this.cursor < 0) {
            this.cursor = this.options.size() - 1;
        }
    }

    /**
     * moves the cursor down .
     */
    private void goDown() {
        this.cursor++;
        if (this.cursor > this.options.size() - 1) {
            this.cursor = 0;
        }
    }

    /**
     * updates the selected option.
     */
    private void updateSelectedOption() {
        this.selectedOption = this.options.get(this.cursor);
    }

}

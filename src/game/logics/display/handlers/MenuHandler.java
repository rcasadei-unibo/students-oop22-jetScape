package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

import game.frame.GameWindow;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.MenuOption;
import game.utility.other.Sound;

/**
 * The {@code MenuHandler} class manages {@link game.logics.display.view.Display Display} menus.
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
     * Initializes the {@link MenuHandler} with the given display's options and sets 
     * the first one as currently selected, performing setGameState when enter is pressed.
     * @param keyH {@link KeyHandler} instance
     * @param options {@link List} of {@link MenuOption} that will be shown
     * @param setGameState a {@link Consumer} of {@link GameState}
     */
    public MenuHandler(final KeyHandler keyH, final List<MenuOption> options, final Consumer<GameState> setGameState) {
        super();
        this.keyH = keyH;
        this.cursor = 0;
        this.options = options;
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
                this.keyH.resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            case KeyEvent.VK_DOWN:
                this.goDown();
                this.keyH.resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            case KeyEvent.VK_ENTER:
                this.setGameState.accept(this.selectedOption.getOptionsGS());
                this.keyH.resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
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
     * moves the cursor up.
     */
    protected void goUp() {
        this.cursor--;
        if (this.cursor < 0) {
            this.cursor = this.options.size() - 1;
        }
    }

    /**
     * moves the cursor down.
     */
    protected void goDown() {
        this.cursor++;
        if (this.cursor > this.options.size() - 1) {
            this.cursor = 0;
        }
    }

    /**
     * updates the selected option.
     */
    protected void updateSelectedOption() {
        this.selectedOption = this.options.get(this.cursor);
    }

    /**
     * @return this handler's KeyHandler
     */
    protected KeyHandler getKeyH() {
        return keyH;
    }

    /**
     * @return this handler's setter for game state
     */
    protected Consumer<GameState> getSetGameState() {
        return setGameState;
    }

    /**
     * @return this handler's list of options
     */
    protected List<MenuOption> getOptions() {
        return options;
    }

    /**
     * @return this handler's cursor value
     */
    protected int getCursor() {
        return cursor;
    }
}

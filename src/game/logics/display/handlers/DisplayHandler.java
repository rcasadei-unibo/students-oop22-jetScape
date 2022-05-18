package game.logics.display.handlers;

import game.utility.other.MenuOption;

/**
 * The {@link DisplayHandler} interface is used for accessing {@link MenuHandler} methods.
 * 
 * <p>
 * The {@link MenuHandler} class manages {@link Display} menus
 * </p>
 */
public interface DisplayHandler {
    /**
     * updates the menu cursor position.
     */
    void update();

    /**
     * @return current selected option
     */
    MenuOption getSelectedOption();
}

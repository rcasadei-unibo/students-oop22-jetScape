package game.logics.display.handlers;

import game.utility.other.MenuOption;

/**
 * The <code>DisplayHandler</code> interface is used for accessing <code>MenuHandler</code> methods.
 * 
 * <p>
 * The <code>MenuHandler</code> class manages <class>Display</class> menus
 * </p>
 * 
 * @author Giacomo Amadio
 */
public interface DisplayHandler {
    /**
     * updates the menu cursor position  
     */
    public void update();
    
    /**
     * @return current selected option
     */
    public MenuOption getSelectedOption();
}

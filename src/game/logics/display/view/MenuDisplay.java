package game.logics.display.view;

import java.awt.Graphics2D;
//import java.util.Optional;

import game.utility.other.MenuOption;

/**
 * Interface that must be implemented by all classes that want to write
 * text on screen.
 *
 */
public interface MenuDisplay {

    /**
     * Method that is called repeatedly to update the graphic view of the
     * display card when it has a options menu.
     *.
     * @param g the graphics drawer
     * @param selected Selected menu option, if it is present a menu
     */
    void drawScreen(Graphics2D g, MenuOption selected);

    /**
     * Method that is called the first time to update the graphic view of
     * the display card, with the standard selected option for the right card.
     *.
     * @param g the graphics drawer
     */
    /*void drawScreen(Graphics2D g);*/
}

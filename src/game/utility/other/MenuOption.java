package game.utility.other;

import java.util.Map;

/**
* {@link MenuOption} enumerates all the different options used in menus.
*/
public enum MenuOption {
   /**
    * Menu options.
    */
    START, SHOP, QUIT, RECORDS, MENU, RETRY, RESUME;

    static final Map<MenuOption, GameState> OPTIONS = Map.of(START, GameState.INGAME,
            SHOP, GameState.MENU, QUIT, GameState.EXIT, RECORDS, GameState.RECORDS,
            MENU, GameState.MENU, RETRY, GameState.INGAME, RESUME, GameState.INGAME);


   /**
    *@return the {@link GameState} associated with the current option
    */
    public GameState getOptionsGS() {
        return MenuOption.OPTIONS.get(this);
    }

    @Override
    public String toString() {
        if (this.equals(MENU)) {
            return "back to menu";
        }
        return super.toString().toLowerCase();
    }
}

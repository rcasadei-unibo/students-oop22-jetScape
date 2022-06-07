package game.utility.other;

import java.util.Locale;
import java.util.Map;

/**
* {@link MenuOption} enumerates all the different options used in menus.
*/
public enum MenuOption {
   /**
    * Menu options.
    */
    START, SETTINGS, QUIT, RECORDS, MENU, RETRY, RESUME, MUSIC, SOUND;

    static final Map<MenuOption, GameState> OPTIONS = Map.of(START, GameState.INGAME,
            SETTINGS, GameState.SETTINGS, QUIT, GameState.EXIT, RECORDS, GameState.RECORDS,
            MENU, GameState.MENU, RETRY, GameState.INGAME, RESUME, GameState.INGAME,
            MUSIC, GameState.SETTINGS, SOUND, GameState.SETTINGS);

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
        return super.toString().toLowerCase(Locale.ENGLISH);
    }
}

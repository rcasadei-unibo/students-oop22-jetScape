package game.utility.other;

import java.util.Map;

// TODO: add javadoc
/**
* Complete me.
*/
public enum MenuOption {
    // TODO: add javadoc
   /**
    * Complete me.
    */
    START, SHOP, QUIT, RECORDS, MENU, RETRY, RESUME;

    static final Map<MenuOption, GameState> OPTIONS = Map.of(START, GameState.INGAME,
            SHOP, GameState.MENU, QUIT, GameState.EXIT, RECORDS, GameState.RECORDS,
            MENU, GameState.MENU, RETRY, GameState.INGAME, RESUME, GameState.INGAME);

    // TODO: add javadoc
   /**
    * Complete me.
    * @return complete
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

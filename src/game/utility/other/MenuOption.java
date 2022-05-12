package game.utility.other;

import java.util.Map;

public enum MenuOption {
    START, SHOP, QUIT, RECORDS, MENU, RETRY, RESUME;
    
    static final Map<MenuOption,GameState> options = Map.of(START, GameState.INGAME,
            SHOP, GameState.MENU, QUIT, GameState.EXIT, RECORDS, GameState.RECORDS,
            MENU, GameState.MENU, RETRY, GameState.INGAME, RESUME, GameState.INGAME);
    
    public GameState getOptionsGS () {
        return MenuOption.options.get(this);
    }
    
    @Override
    public String toString() {
        if(this.equals(MENU)) {
            return "back to menu";
        }
        return super.toString().toLowerCase();        
    }
}

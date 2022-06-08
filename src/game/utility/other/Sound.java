package game.utility.other;

import java.util.Map;

public enum Sound {
    MAIN_THEME, MENU_SELECTION;

    static final Map<Sound, String> FILES = Map.of(MAIN_THEME, "MainTheme.wav",
            MENU_SELECTION,"MenuSelect.wav");

    /**
     *@return the associated track's file name 
     */
     public String getFileName() {
         return FILES.get(this);
     }
}

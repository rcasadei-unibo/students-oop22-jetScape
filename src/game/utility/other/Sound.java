package game.utility.other;

import java.util.Map;

public enum Sound {
    MAIN_THEME, MENU_SELECTION, ZAPPED, COIN, SHIELD_UP,
    SHIELD_DOWN, TELEPORT, MISSILE, JETPACK, MISSILE_WARNING;

    public static final Map<Sound, String> FILES = Map.of(MAIN_THEME, "MainTheme.wav",
            MENU_SELECTION, "MenuSelect.wav", ZAPPED, "Zapped.wav",
            COIN, "Coin.wav", SHIELD_UP, "ShieldUp.wav",
            TELEPORT, "Teleport.wav", MISSILE, "Missile.wav",
            JETPACK, "Jetpack.wav", SHIELD_DOWN,"ShieldDown.wav",
            MISSILE_WARNING,"MissileWarning.wav");

    /**
     *@return the associated track's file name 
     */
     public String getFileName() {
         return FILES.get(this);
     }
}

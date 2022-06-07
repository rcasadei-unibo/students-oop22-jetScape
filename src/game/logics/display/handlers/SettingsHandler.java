package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import game.frame.GameWindow;
import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.MenuOption;

public class SettingsHandler extends MenuHandler {

    public SettingsHandler(KeyHandler keyH, Display display, Consumer<GameState> setGameState) {
        super(keyH, display, setGameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        switch (super.getKeyH().getKeyTyped()) {
            case KeyEvent.VK_UP :
                this.goUp();
                super.getKeyH().resetKeyTyped();
                break;
            case KeyEvent.VK_DOWN:
                this.goDown();
                super.getKeyH().resetKeyTyped();
                break;
            case KeyEvent.VK_LEFT:
                this.lower();
                super.getKeyH().resetKeyTyped();
                break;
            case KeyEvent.VK_RIGHT:
                this.raise();
                super.getKeyH().resetKeyTyped();
                break;
            case KeyEvent.VK_ENTER:
                super.getSetGameState().accept(super.getSelectedOption().getOptionsGS());
                super.getKeyH().resetKeyTyped();
                break;
            default:
                break;
        }
    }

    private void lower() {
        if(super.getSelectedOption().equals(MenuOption.MUSIC)){
            GameWindow.GAME_SOUND.lowerVolumeLevel();
        }
    }

    private void raise() {
        if(super.getSelectedOption().equals(MenuOption.MUSIC)){
            GameWindow.GAME_SOUND.raiseVolumeLevel();
         }
    }
}

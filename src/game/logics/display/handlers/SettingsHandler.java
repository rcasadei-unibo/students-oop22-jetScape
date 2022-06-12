package game.logics.display.handlers;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import game.frame.GameWindow;
import game.logics.display.view.Display;
import game.utility.input.keyboard.KeyHandler;
import game.utility.other.GameState;
import game.utility.other.Sound;

/**
 * this class is used to handle settings regulations through sliders use.
 */
public class SettingsHandler extends MenuHandler {

    /**
     * initializes Setting Handler class.
     * @param keyH
     * @param display
     * @param setGameState
     */
    public SettingsHandler(final KeyHandler keyH, final Display display,
            final Consumer<GameState> setGameState) {
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
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            case KeyEvent.VK_DOWN:
                this.goDown();
                super.getKeyH().resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            case KeyEvent.VK_LEFT:
                this.lower();
                super.getKeyH().resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            case KeyEvent.VK_RIGHT:
                this.raise();
                super.getKeyH().resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            case KeyEvent.VK_ENTER:
                super.getSetGameState().accept(super.getSelectedOption().getOptionsGS());
                super.getKeyH().resetKeyTyped();
                GameWindow.GAME_SOUND.play(Sound.MENU_SELECTION);
                break;
            default:
                break;
        }
    }

    private void lower() {
        switch (super.getSelectedOption()) {
            case MUSIC:
                GameWindow.GAME_MUSIC.lowerVolumeLevel();
                break;
            case SOUND:
                GameWindow.GAME_SOUND.lowerVolumeLevel();
                break;
            default:
                break;
        }
    }

    private void raise() {
        switch (super.getSelectedOption()) {
        case MUSIC:
            GameWindow.GAME_MUSIC.raiseVolumeLevel();
            break;
        case SOUND:
            GameWindow.GAME_SOUND.raiseVolumeLevel();
            break;
        default:
            break;
    }
    }
}

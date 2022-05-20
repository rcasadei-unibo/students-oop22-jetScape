package game.utility.input.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link KeyHandler} class is used for knowing 
 * which keys are being pressed or released on the keyboard.
 */
public class KeyHandler implements KeyListener {

    /**
     * A map that tells if a key is being pressed or not.
     * 
     * <p>
     * If <code>input.get(key)</code> is <code>true</code> means
     * that the current key is being pressed.
     * <br>
     * If <code>input.get(key)</code> is <code>false</code> means
     * that the current key is not being pressed.
     * </p>
     * 
     */
    private final Map<Integer, Boolean> input = new HashMap<>();
    private Optional<Integer> lastKeyTyped = Optional.empty();

    /**
     * Initializes a {@link KeyHandler}.
     */
    public KeyHandler() {
        input.put(KeyEvent.VK_SPACE, false);
        input.put(KeyEvent.VK_X, false);
        input.put(KeyEvent.VK_Z, false);
        input.put(KeyEvent.VK_ENTER, false);
        input.put(KeyEvent.VK_E, false);
        input.put(KeyEvent.VK_R, false);
        input.put(KeyEvent.VK_P, false);
        input.put(KeyEvent.VK_C, false);
        input.put(KeyEvent.VK_V, false);
        input.put(KeyEvent.VK_UP, false);
        input.put(KeyEvent.VK_DOWN, false);
    }

    /**
     * @return the key code of the last key typed, if no key has been pressed returns -1
     */
    public int getKeyTyped() {
        if (this.lastKeyTyped.isPresent()) {
            return lastKeyTyped.get();
        }
        return -1;
    }
    /**
     * Reset the last key memorized after has been used.
     */
    public void resetKeyTyped() {
        this.lastKeyTyped = Optional.empty();
    }
    /**
     * @param key the key code of the key to check
     * @return <code>true</code> if the key is pressed in the current frame, <code>false</code> if it isn't pressed or
     * the key code is not supported
     */
    public boolean getCurrentInput(final int key) {
        if (input.containsKey(key)) {
            return input.get(key);
        }
        return false;
    }

    @Override
    public void keyTyped(final KeyEvent e) { }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        if (input.containsKey(e.getKeyCode())) {
            if (!input.get(e.getKeyCode())) {
                this.lastKeyTyped = Optional.of(e.getKeyCode());
            }
            input.replace(e.getKeyCode(), true);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e) {
        if (input.containsKey(e.getKeyCode())) {
            input.replace(e.getKeyCode(), false);
        }
    }

}

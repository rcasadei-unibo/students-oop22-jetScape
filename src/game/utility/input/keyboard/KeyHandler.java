package game.utility.input.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The <code>KeyHandler</code> class is used for knowing 
 * which keys are being pressed or released on the keyboard.
 * 
 * @author Daniel Pellanda
 */
public class KeyHandler implements KeyListener{

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
	public Map<Integer,Boolean> input = new HashMap<>();
	private Optional<Integer> lastKeyTyped = Optional.empty();
	
	/**
	 * Initializes a <code>KeyHandler</code>.
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

	public int getKeyTyped() {
		if (this.lastKeyTyped.isPresent()) {
			return lastKeyTyped.get();
		}
		return -1;
	}
	
	public void resetKeyTyped() {
		this.lastKeyTyped = Optional.empty();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(input.containsKey(e.getKeyCode())){
			if(!input.get(e.getKeyCode())) {
				this.lastKeyTyped = Optional.of(e.getKeyCode());
			}
			input.replace(e.getKeyCode(), true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(input.containsKey(e.getKeyCode())){
			input.replace(e.getKeyCode(), false);
		}
	}

}
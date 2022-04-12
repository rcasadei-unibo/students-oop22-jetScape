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
	public Map<String,Boolean> input = new HashMap<>();
	private Optional<Integer> lastKeyPressed = Optional.empty();
	
	/**
	 * Initializes a <code>KeyHandler</code>.
	 */
	public KeyHandler() {
		input.put("spacebar", false);
		input.put("x", false);
		input.put("z", false);
		input.put("enter", false);
		input.put("e", false);
		input.put("r", false);
		input.put("p", false);
		input.put("c", false);
		input.put("v", false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				input.replace("spacebar", true);
				break;
			case KeyEvent.VK_X:
				input.replace("x", true);
				break;
			case KeyEvent.VK_Z:
				input.replace("z", true);
				break;
			case KeyEvent.VK_ENTER:
				input.replace("enter", true);
				break;
			case KeyEvent.VK_E:
				input.replace("e", true);
				break;
			case KeyEvent.VK_C:
				input.replace("c", true);
				break;
			case KeyEvent.VK_V:
				input.replace("v", true);
				break;
			case KeyEvent.VK_P:
				input.replace("p", true);
				break;
			case KeyEvent.VK_R:
				input.replace("r", true);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				input.replace("spacebar", false);
				setLastKey(Optional.of(KeyEvent.VK_SPACE));
				break;
			case KeyEvent.VK_X:
				input.replace("x", false);
				setLastKey(Optional.of(KeyEvent.VK_X));
				break;
			case KeyEvent.VK_Z:
				input.replace("z", false);
				setLastKey(Optional.of(KeyEvent.VK_Z));
				break;
			case KeyEvent.VK_ENTER:
				input.replace("enter", false);
				setLastKey(Optional.of(KeyEvent.VK_ENTER));
				break;
			case KeyEvent.VK_E:
				input.replace("e", false);
				setLastKey(Optional.of(KeyEvent.VK_E));
				break;
			case KeyEvent.VK_C:
				input.replace("c", false);
				setLastKey(Optional.of(KeyEvent.VK_C));
				break;
			case KeyEvent.VK_V:
				input.replace("v", false);
				setLastKey(Optional.of(KeyEvent.VK_V));
				break;
			case KeyEvent.VK_P:
				input.replace("p", false);
				setLastKey(Optional.of(KeyEvent.VK_P));
				break;
			case KeyEvent.VK_R:
				input.replace("r", false);
				setLastKey(Optional.of(KeyEvent.VK_R));
				break;
		}
	}
	
	public int ConsumeLastKey() {
		int e = this.lastKeyPressed.get();
		setLastKey(Optional.empty());
		return  e;
	}
	
	private void setLastKey(Optional<Integer> e) {
		this.lastKeyPressed = e;
	}
}
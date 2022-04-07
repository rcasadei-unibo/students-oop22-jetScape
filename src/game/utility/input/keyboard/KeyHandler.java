package game.utility.input.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

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
	
	/**
	 * Initializes a <code>KeyHandler</code>.
	 */
	public KeyHandler() {
		input.put("spacebar", false);
		input.put("x", false);
		input.put("z", false);
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
			case KeyEvent.VK_C:
				input.replace("c", true);
				break;
			case KeyEvent.VK_V:
				input.replace("v", true);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				input.replace("spacebar", false);
				break;
			case KeyEvent.VK_X:
				input.replace("x", false);
				break;
			case KeyEvent.VK_Z:
				input.replace("z", false);
				break;
			case KeyEvent.VK_C:
				input.replace("c", false);
				break;
			case KeyEvent.VK_V:
				input.replace("v", false);
				break;
		}
	}
	
}
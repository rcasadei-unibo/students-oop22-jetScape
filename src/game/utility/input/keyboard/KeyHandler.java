package game.utility.input.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyHandler implements KeyListener{

	public Map<String,Boolean> input = new HashMap<>();
	
	public KeyHandler() {
		input.put("spacebar", false);
		input.put("x", false);
		input.put("z", false);
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
		}
	}
	
}
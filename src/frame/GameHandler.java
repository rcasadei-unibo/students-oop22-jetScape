package game.frame;

import javax.swing.JFrame;

public class GameHandler implements Game {
	
	private static final String windowTitle = "Jetpack Joyride dei negri";
	private static final boolean allowResize = false;
	
	private final JFrame gFrame = new JFrame();
	private final GameWindow gScreen;
	
	private boolean debugMode = false;
	
	public GameHandler(final boolean debug) {
		this.debugMode = debug;
		
		gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gFrame.setTitle(windowTitle);
		gFrame.setLocationRelativeTo(null);
		gFrame.setResizable(allowResize);
		
		gScreen = new GameWindow(debugMode);
		gFrame.add(gScreen);
		gFrame.pack();
	}
	
	@Override
	public void initialize() {
		gFrame.setVisible(true);
		gScreen.startLoop();
	}

}

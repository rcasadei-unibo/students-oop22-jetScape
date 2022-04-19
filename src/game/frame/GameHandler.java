package game.frame;

import javax.swing.JFrame;

/**
 * The <code>GameHandler</code> class is used to create and handle
 * the frame where the window of the game is going to take place
 * (which will be handled by the <code>GameWindow</code> class).
 * 
 * <p>
 * You can use <code>GameHandler.initialize()</code> for allowing
 * the game to start.
 * </p>
 * 
 * @author Daniel Pellanda
 *
 */
public class GameHandler implements Game {
	
	/**
	 * The title of the game shown on the top left of the window.
	 */
	private static final String windowTitle = "JetScape";
	/**
	 * Allows the window to get manually resized by the user.
	 * <p>It's suggested to leave it <code>false</code> as long as a way to change
	 * change Resolution run-time is not being implemented.</p> 
	 */
	private static final boolean allowResize = false;
	
	/**
	 * The <code>JFrame</code> where the game window will be contained.
	 */
	private final JFrame gFrame = new JFrame();
	private final GameWindow gScreen;
	
	private boolean debugMode = false;
	
	/**
	 * Basic constructor that creates a <code>JFrame</code> with a <code>GameWindow</code>
	 * attached to it.
	 * 
	 * @param debug decides in which debug mode the game has to start  
	 */
	public GameHandler(final boolean debug) {
		this.debugMode = debug;
		
		gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gFrame.setTitle(windowTitle);
		gFrame.setLocationRelativeTo(null);
		gFrame.setResizable(allowResize);
		
		gScreen = new GameWindow(debugMode);
		gFrame.add(gScreen);
		
		//Modifies the frame making it the same size of the game window
		gFrame.pack();
	}
	
	/**
	 * Allows the game to start by beginning the execution of the game window thread.
	 */
	@Override
	public void initialize() {
		gFrame.setVisible(true);
		gScreen.startGame();
	}

}

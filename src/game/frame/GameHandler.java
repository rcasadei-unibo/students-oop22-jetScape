package game.frame;

import javax.swing.JFrame;

import game.utility.screen.Screen;
import game.utility.sprites.AbstractSprite;

/**
 * The {@link GameHandler} class is used to create and handle
 * the frame where the window of the game is going to take place
 * (which will be handled by the {@link GameWindow} class).
 * 
 * <p>
 * You can use <code>{@link GameHandler}.initialize()</code> for allowing
 * the game to start.
 * </p>
 * 
 */
public class GameHandler implements Game {

    /**
     * The title of the game shown on the top left of the window.
     */
    private static final String WINDOW_TITLE = "JetScape";
    /**
     * The icon representing the game window.
     */
    private static final String WINDOW_ICON_PATH = AbstractSprite.getDefaultSpriteDirectory() + "icon.png";
    /**
     * Allows the window to get manually resized by the user.
     * <p>It's suggested to leave it <code>false</code> as long as a way to change
     * change Resolution run-time is not being implemented.</p> 
     */
    private static final boolean WINDOW_RESIZABLE = false;

    /**
     *  Starting debug mode.
     */
    public static final boolean DEBUG_MODE = false;

    /**
     * The {@link JFrame} where the game window will be contained.
     */
    public static final JFrame GAME_FRAME = new JFrame();

    /**
     * The {@link GameWindow} where the game will run.
     */
    public static final GameWindow GAME_WINDOW = new GameWindow();

    /**
     * Basic constructor that creates a {@link JFrame} with a {@link GameWindow}
     * attached to it.
     */
    public GameHandler() {
        final int windowLocation = 6;

        GAME_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GAME_FRAME.setTitle(WINDOW_TITLE);
        GAME_FRAME.setLocation((int) Screen.SYSTEM_RESOLUTION.getWidth() / windowLocation, (int) Screen.SYSTEM_RESOLUTION.getHeight() / windowLocation);
        GAME_FRAME.setIconImage(AbstractSprite.load(WINDOW_ICON_PATH));
        GAME_FRAME.setResizable(WINDOW_RESIZABLE);

        GAME_FRAME.add(GAME_WINDOW);

        //Modifies the frame making it the same size of the game window
        GAME_FRAME.pack();
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        GAME_FRAME.setVisible(true);
        GAME_WINDOW.startGame();
    }
}

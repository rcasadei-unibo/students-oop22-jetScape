package game.frame;

/**
 * The <code>Game</code> interface is used for accessing
 * <code>GameHandler</code> methods.
 *
 * <p>
 * The <code>GameHandler</code> class is used to create and handle
 * the frame where the window of the game is going to take place
 * (which will be handled by the <code>GameWindow</code> class).
 * </p>
 *
 * <p>
 * You can use <code>GameHandler.initialize()</code> for allowing
 * the game to start.
 * </p>
 *
 */
public interface Game {
    /**
     * Allows the game to start by beginning the execution of the game window thread.
     */
    void initialize();
}

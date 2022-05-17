package game.frame;

/**
 * The {@link Game} interface is used for accessing
 * {@link GameHandler} methods.
 *
 * <p>
 * The {@link GameHandler} class is used to create and handle
 * the frame where the window of the game is going to take place
 * (which will be handled by the {@link GameWindow} class).
 * </p>
 *
 * <p>
 * You can use <code>{@link GameHandler}.initialize()</code> for allowing
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

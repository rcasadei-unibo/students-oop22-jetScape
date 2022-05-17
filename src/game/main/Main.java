package game.main;

import game.frame.Game;
import game.frame.GameHandler;

/**
 * Main class.
 *
 */
public final class Main {

    private Main() {
    }

    /**
     * Main method.
     * @param args parameters passed from cli
     */
    public static void main(final String[] args) {
        final Game g = new GameHandler();
        g.initialize();
    }
}

package game.logics.handler;

import game.logics.entities.generic.Entity;
import game.utility.other.EntityType;
import java.awt.Graphics2D;

import java.util.Date;
import java.util.Optional;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * The <code>Logics</code> interface is used for accessing
 * {@link LogicsHandler} methods.
 *
 * <p>
 * The {@link LogicsHandler} class helps {@link game.frame.GameWindow GameWindow} to update and draw
 * logical parts of the game like the Interface, Entities, Collisions, etc....
 * </p>
 *
 */
public interface Logics {

    /**
     * @return a {@link BiConsumer} that can be used to clean up current active entities in the environment
     */
    BiConsumer<Predicate<EntityType>, Predicate<Entity>> getEntitiesCleaner();

    /**
     * Updates all the logical objects handled for a frame.
     */
    void updateAll();

    /**
     * Draws all visible and drawable object handled for a frame.
     *
     * @param g the graphics drawer
     */
    void drawAll(Graphics2D g);

    /**
     * This class models a Game, the GameInfo handler: this class keeps a
     *   reference to the actual UID of the current game.
     */
    class GameInfoHandler {

        private static int numbersOfGamesPlayed;
        private static GameInfo actualGame;

        public GameInfoHandler(final GameInfo newGame) {
            GameInfoHandler.actualGame = newGame;
        }

        public int getNumbersOfGamesPlayed() {
            return GameInfoHandler.numbersOfGamesPlayed;
        }

        public GameInfo getActualGame() {
           return actualGame;
        }

        void setActualGame(final GameInfo newGameID) {
            GameInfoHandler.actualGame = newGameID;
        }
    }

    /**
     * This class models a GameInfo, a unique game identifier that is used to
     * refer to the actual game.
     *
     */
    class GameInfo {

        private final int gameNumber;

        private final Date gameStartDate = new Date();
        private Optional<Date> gameEndDate = Optional.empty();
        private boolean gameEnded;
        private int finalScore;

        /**
         * Builds the first GameInfo at the first game played.
         * The unique identifier is set here.
         */
        public GameInfo() {
            this.gameNumber = 0;
        }

        /**
         * Builds a new GameInfo when the game begins.
         * The unique identifier is set here.
         *
         * @param game Game general informations
         */
        public GameInfo(final GameInfoHandler game) {
            this.gameNumber = game.getNumbersOfGamesPlayed() + 1;
        }

        /**
         * This method is used to get when a game has started.
         *
         * @return Date representing the time when the game started
         */
        public Date getGameStartDate() {
            return this.gameStartDate;
        }

        /**
         * This method is used to get when a game is over, if it is over.
         *
         * @return Optional&lt;Date&gt; Date representing the time when the game ended,
         *   if ended, otherwise returns Optional.empty()
         */
        public Optional<Date> getGameEndDate() {
            return this.gameEndDate;
        }

        /**
         * This method sets the game as ended.
         *
         * @param score score obtained by player
         */
        public void setGameEnded(final int score) {
           if (!this.gameEnded) {
               this.gameEndDate = Optional.of(new Date());
               this.finalScore = score;
               this.gameEnded = true;
           }
        }

        public int getFinalScore() {
            return this.finalScore;
        }

        /**
         * Getter method to receive the Unique IDentifier.
         * @return an int representing the UID
         */
        public int getUID() {
            return this.gameNumber;
        }

        /**
         * This method calculates the time elapsed from when the game started
         *   until it ended.
         *
         * @return the time elapsed, or better the time difference
         */
        public long getGameDuration() {
            // Calculates time difference in seconds
            return (this.getGameEndDate().get().getTime()
                    - this.getGameStartDate().getTime()) / 1000;

            /*
            // Calculate time difference in
            // seconds, minutes, hours, years and days
            long difference_In_Seconds
                = (difference_In_Time
                   / 1000)
                  % 60;

            long difference_In_Minutes
                = (difference_In_Time
                   / (1000 * 60))
                  % 60;
            */
        }

        public boolean isGameEnded() {
            return this.gameEnded;
        }
    }
}


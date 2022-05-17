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
 * The {@link LogicsHandler} class helps {@link GameWindow} to update and draw
 * logical parts of the game like the Interface, Entities, Collisions, etc....
 * </p>
 *
 * @author Daniel Pellanda
 */
public interface Logics {

    BiConsumer<Predicate<EntityType>,Predicate<Entity>> getEntitiesCleaner();

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

    public static class Game {

        private static int numbersOfGamesPlayed = 0;
        private static GameUID actualGame;

        public Game(final GameUID newGame) {
            Game.actualGame = newGame;
        }

        public int getNumbersOfGamesPlayed() {
            return Game.numbersOfGamesPlayed;
        }

        public GameUID getActualGame() {
           return actualGame;
        }

        void setActualGame(final GameUID newGameID) {
            Game.actualGame = newGameID;
        }
    }

    /**
     * This class models a GameID, a unique game identifier that is used to
     * refer to the actual game.
     *
     */
    public class GameUID {

        private Date gameStartDate = new Date();
        private Optional<Date> gameEndDate = Optional.empty();
        private boolean gameEnded = false;
        private final int gameNumber;

        /**
         * Builds the first GameUID at the first game played.
         * The unique identifier is set here.
         */
        public GameUID() {
            this.gameNumber = 0;
        }

        /**
         * Builds a new GameID when the game begins.
         * The unique identifier is set here.
         *
         * @param game Game general informations
         */
        public GameUID(final Game game) {
            this.gameNumber = game.getNumbersOfGamesPlayed() + 1;
        }

        public Date getGameStartDate() {
            return this.gameStartDate;
        }

        public Optional<Date> getGameEndDate() {
            return this.gameEndDate;
        }

        public void setGameEnded(final Date endGameDate) {
           if (!this.gameEnded) {
               this.gameEndDate = Optional.of(endGameDate);
               this.gameEnded = true;
           }
        }

        public int getUID() {
            return this.gameNumber;
        }

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
    }
}

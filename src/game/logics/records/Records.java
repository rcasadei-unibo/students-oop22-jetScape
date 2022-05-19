package game.logics.records;

import game.logics.entities.player.Player;
import game.logics.entities.player.Player.PlayerDeath;
import game.utility.input.JSONReader;
import game.utility.input.JSONWriter;
import game.logics.handler.Logics.GameInfoHandler;
import game.logics.handler.Logics.GameInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class Records {

    private final GameInfoHandler game;
    private final Player player;
    private final JSONWriter writer;
    private final JSONReader reader;
    //private GameInfo oldGameInfo;

    // Statistics and records list
    // TODO complete list
    private int burnedTimes;
    private int zappedTimes;
    // private String[] position;
    // private Map<String, Integer> salary;

    // Data read from game
    //private int score;
    private PlayerDeath causeOfDeath;

    private static int playingRecordScore = 0; // higher score obtained by playing consecutively
    private static boolean newPlayingRecordScore = false;

    private int recordScore; // absolute new score record
    private boolean newRecordScore = false;

    public Records(final GameInfoHandler game, final Player player) {
        this.game = game;
        
        this.writer = new JSONWriter(this);
        this.reader = new JSONReader(this);

        //this.oldGameInfo = game.getActualGame();
        //game.getNumbersOfGamesPlayed();
        this.player = player;
    }

    /****************************************/
    /***    In and to file operations     ***/
    /****************************************/

    /**
     * Read from file
     */
    public void refresh() {
        this.reader.read();
    }
/*
    private boolean checkAndSet(final GameUID newGameUID) {

        final BiPredicate<GameUID, GameUID> checkIfNew =
                (oldUID,newUID) -> oldUID.getGameDate() != newUID.getGameDate();
        boolean isNewUID = false;

        if (!newGameUID.isGamePlayed()) {
            isNewUID = true;
        } else if(checkIfNew.test(UIDGame, newGameUID)) {
            isNewUID = true;
            Records.UIDGame = newGameUID;
        }
        return isNewUID;
    }*/

    /**
     * Declares game ended: sets game end date and gets final score.
     *
     * @param getGameInfo Supplier of GameInfo to check
     */
    public void announceGameEnded(final Supplier<GameInfo> getGameInfo) {

        final GameInfo newGameInfo = getGameInfo.get();

        // if different --> new game
        //if (oldGameInfo.getUID() != newGameInfo.getUID()) {
            if (!newGameInfo.isGameEnded()) {

                final int score = player.getCurrentScore();
                System.out.println(score);
                newGameInfo.setGameEnded(score);
                this.fetch(newGameInfo);
            }
            //oldGameInfo = newGameInfo;
        //}
    }

    /**
     * Get data for updating in game, calling the data getters
     */
	//TODO add new records
    private void fetch(final GameInfo newGameInfo) {

        //System.out.println(gameUID.isGamePlayed());
        //gameUID.getGameDate().ifPresent(System.out::println);

        // Only if new gameUID (new game)
        //if (this.checkAndSet(gameUID)) {
            if (player.hasDied()) {
                this.causeOfDeath = player.getCauseOfDeath();
                switch (this.causeOfDeath) {
                    case BURNED:
                        this.burnedTimes ++;
                        break;
                    case ZAPPED:
                        this.zappedTimes ++;
                        break;
                    default:
                        break;
                }
                //System.out.println(causeOfDeath);
            }
            this.checkScore(newGameInfo.getFinalScore());
            //this.getScore();
        //}
    }

    /**
     * Write to file
     */
    public void update() {
        this.writer.write();
    }

    /****************************************/
    /***   Calculate and check records    ***/
    /****************************************/

	/*  TODO Move this method inside records
	 *  substituting it with getter methods .isNewAbsoluteRecordScore() & .isNewPlayingRecordScore()
	 */
    /**
     * This method checks if the new finalScore is a new record and only in this case saves it.
     * @param finalScore
     *   final score in the current game
     */
    public void checkScore(final int finalScore) {

        //this.score = finalScore;

        if (finalScore > Records.playingRecordScore) {
            Records.newPlayingRecordScore = true;
            Records.playingRecordScore = finalScore;
        } else if (finalScore < Records.playingRecordScore) {
            Records.newPlayingRecordScore = false;
        }

        if (finalScore > this.recordScore) {
            this.newRecordScore = true;
            this.setRecordScore(finalScore);
            //StatisticsReader.writeRecord(finalScore); // TODO write new record
        } else if (finalScore < this.recordScore) {
            this.newRecordScore = false;
        }
    }

    /****************************************/
    /*** Getters & Setters from / to file ***/
    /****************************************/

    public int getBurnedTimes() {
        return this.burnedTimes;
    }

    public int getZappedTimes() {
        return this.zappedTimes;
    }

/*
    public void setBurnedTimes(final int readBurnedTimes) {
        this.burnedTimes = readBurnedTimes;
    }

    public void setZappedTimes(final int readZappedTimes) {
        this.zappedTimes = readZappedTimes;
    }
*/

    public int getRecordScore() {
        return this.recordScore;
    }

    public void setRecordScore(int recordScore) {
        this.recordScore = recordScore;
    }

    /****************************************/
    /*** Getters & Setters from / to game ***/
    /****************************************/

    public int getScore() {
       // return this.score;
        return this.game.getActualGame().getFinalScore();
    }

    public boolean isNewRecordScore() {
        return this.newRecordScore;
    }

    public boolean isNewPlayingRecordScore() {
        return Records.newPlayingRecordScore;
    }
}

package game.logics.records;

import game.logics.entities.player.Player;
import game.logics.entities.player.Player.PlayerDeath;
import game.utility.input.JSONReader;
import game.utility.input.JSONWriter;
import game.logics.handler.Logics.GameInfoHandler;
import game.logics.handler.Logics.GameInfo;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
//import java.util.Optional;
import java.util.Set;
//import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * This class handles statistics and records, furthermore it manages its writing
 * and reading.
 *
 */
public final class Records {

    private static final int MAX_NUMBER_OF_SAVED_RECORD = 3;

    private final GameInfoHandler game;
    private final Player player;
    private final JSONWriter writer;
    private final JSONReader reader;
    //private GameInfo oldGameInfo;

    // Statistics and records list
    private final List<Integer> scoreRecords = new ArrayList<>(Records.MAX_NUMBER_OF_SAVED_RECORD); // absolute new score records
    private final List<Integer> moneyRecords = new ArrayList<>(Records.MAX_NUMBER_OF_SAVED_RECORD); // absolute new money records

    private int burnedTimes;
    private int zappedTimes;

    // Data read from game
    private PlayerDeath causeOfDeath;

    private int playingScoreRecord; // higher score obtained by playing consecutively
    private boolean newPlayingScoreRecord;

    private boolean newScoreRecord;
    private boolean newMoneyRecord;

    /*{
        this.scoreRecords.addAll(Collections.nCopies(Records.MAX_NUMBER_OF_SAVED_RECORD, Optional.empty()));
    }*/

    /**
     * Build a new {@link Records}.
     * @param game a {@link GameInfoHandler} object used to get current game informations.
     * @param player {@link Player} object used to get some player informations.
     */
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
                final int money = player.getCurrentCoinsCollected();
                //System.out.println(score);
                newGameInfo.setGameEnded(score, money);
                this.fetch(newGameInfo);
            }
            //oldGameInfo = newGameInfo;
        //}true if the new score is a new
    }

    /**
     * Get data for updating in game, calling the data getters.
     *
     *@param newGameInfo GameInfo passed via {@link Supplier} by {@link #announceGameEnded announceGameEnded()}
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
                        this.burnedTimes++;
                        break;
                    case ZAPPED:
                        this.zappedTimes++;
                        break;
                    default:
                        break;
                }
                //System.out.println(causeOfDeath);
            }
            this.checkScore(newGameInfo.getFinalScore());
            this.checkMoney(newGameInfo.getFinalMoney());
            //this.getScore();
        //}
    }

    /**
     * Read from file.
     */
    public void refresh() {
        this.reader.read();
    }

    /**
     * Write to file.
     */
    public void update() {
        this.writer.write();
    }

    /****************************************/
    /***   Calculate and check records    ***/
    /****************************************/

    //  TODO use new GameUID Date
    /**
     * This method checks if the new finalScore is a new record and only in
     * this case saves it.
     *
     * @param finalScore
     *   final score in the current game
     */
    public void checkScore(final int finalScore) {

        //this.score = finalScore;

        if (finalScore > this.playingScoreRecord) {
            this.newPlayingScoreRecord = true;
            this.playingScoreRecord = finalScore;
        } else if (finalScore < this.playingScoreRecord) {
            this.newPlayingScoreRecord = false;
        }

        this.newScoreRecord = finalScore > this.getHighestRecordScore();

        if (this.scoreRecords.size() < Records.getMaxSavedNumberOfRecords()
                || finalScore > this.getLowestRecordScore()) {
            this.addScoreRecord(finalScore);
        }

        //if (finalScore > this.getLowestRecordScore()) {
           // this.newScoreRecord = true;
        //    this.addScoreRecord(finalScore);
            //StatisticsReader.writeRecord(finalScore); // TODO write new record
        //} else if (finalScore < this.recordScore) {
           // this.newScoreRecord = false;
        //}
    }

    /**
     * This method checks if the new finalCoins is a new record and only in
     * this case saves it.
     *
     * @param finalCoinsCollected
     *   final number of coins collected in the current game
     */
    public void checkMoney(final int finalCoinsCollected) {

        if (this.moneyRecords.size() < Records.getMaxSavedNumberOfRecords()
                || finalCoinsCollected > this.getLowestMoneyRecord()) {
            this.newMoneyRecord = true;
            this.addMoneyRecord(finalCoinsCollected);
        } else {
            this.newMoneyRecord = false;
        }
    }

    /****************************************/
    /*** Getters & Setters from / to file ***/
    /****************************************/

    /**
     * This static method is used to get the constant value stored as
     * Records.NUMBER_OF_SAVED_RECORD.
     *
     * @return the number of records that will be written to file.
     */
    public static int getMaxSavedNumberOfRecords() {
        return Records.MAX_NUMBER_OF_SAVED_RECORD;
    }

    /**
     * This method is used to get burnedTimes value.
     *
     * @return how many times Barry died burned.
     */
    public int getBurnedTimes() {
        return this.burnedTimes;
    }

    /**
     * This method is used to get zappedTimes value.
     *
     * @return how many times Barry died electrocuted.
     */
    public int getZappedTimes() {
        return this.zappedTimes;
    }

    /**
     * This method is used to set burnedTimes value.
     *
     * @param readBurnedTimes how many times Barry died burned.
     */
    public void setBurnedTimes(final int readBurnedTimes) {
        this.burnedTimes = readBurnedTimes;
    }

    /**
     * This method is used to set zappedTimes value.
     *
     * @param readZappedTimes how many times Barry died electrocuted.
     */
    public void setZappedTimes(final int readZappedTimes) {
        this.zappedTimes = readZappedTimes;
    }

    public void addScoreRecord(final int newScoreRecord) {
        //this.recordScores.forEach(System.out::println);

        this.scoreRecords.add(newScoreRecord);
        this.scoreRecords.sort(Comparator.reverseOrder());

        if (this.scoreRecords.size() > Records.getMaxSavedNumberOfRecords()) {
            //System.out.println("ELIMINATO: "
            //    + this.scoreRecords.get(Records.getSavedNumberOfRecords()).toString());
            this.scoreRecords.remove(Records.getMaxSavedNumberOfRecords());
        }

        //System.out.println("--");
        //this.scoreRecords.stream().forEach(System.out::println);
        //System.out.println("--");
    }

    public List<Integer> getScoreRecords() {
        /*final List<Integer> recordScores = new ArrayList<>();

        for (final Optional<Integer> record : this.recordScores) {
            if (record.isPresent()) {
                recordScores.add(record.get());
            }
        }*/
        return List.copyOf(this.scoreRecords);
    }

    public void setScoreRecords(final List<Integer> recordScores) {
        this.scoreRecords.clear();
        this.scoreRecords.addAll(recordScores);
        /*for (final Integer record : recordScores) {
                this.scoreRecords.add(Optional.of(record));
        }
        if (this.scoreRecords.size() < Records.getMaxSavedNumberOfRecords()) {
            this.scoreRecords.addAll(Collections.nCopies(
                    recordScores.size() - Records.NUMBER_OF_SAVED_RECORD,
                    Optional.empty()));
        }*/
    }

    public void addMoneyRecord(final int newMoneyRecord) {

        this.moneyRecords.add(newMoneyRecord);
        this.moneyRecords.sort(Comparator.reverseOrder());

        if (this.moneyRecords.size() > Records.getMaxSavedNumberOfRecords()) {
            //System.out.println("ELIMINATO: "
            //    + this.moneyRecords.get(Records.getMaxSavedNumberOfRecords()).toString());
            this.moneyRecords.remove(Records.getMaxSavedNumberOfRecords());
        }
    }

    public List<Integer> getMoneyRecords() {
        return List.copyOf(this.moneyRecords);
    }

    public void setMoneyRecords(final List<Integer> recordCoins) {
        this.moneyRecords.clear();
        this.moneyRecords.addAll(recordCoins);
    }

    /****************************************/
    /*** Getters & Setters from / to game ***/
    /****************************************/

    /**
     * Get player score from {@link game.logics.handler.Logics.GameInfo GameInfo} instance.
     * @return player score
     */
    public int getScore() {
        //return this.score;
        return this.game.getActualGame().getFinalScore();
    }

    /**
     * Get current highest score obtained by player.
     * @return first element of the highest scores list
     */
    private Integer getHighestRecordScore() {
        return this.scoreRecords.get(0);
    }

    /**
     * Get current least score obtained by player.
     * @return last element of the highest scores list
     */
    private Integer getLowestRecordScore() {
        if (this.scoreRecords.isEmpty()) {
            return 0;
        } else {
            return this.scoreRecords.stream().sorted().findFirst().get();
            //return this.scoreRecords.get(this.recordScores.size() - 1);
        }
    }

    /**
     * Get if the new score is a new highest record.
     * @return true if the new score is a new highest record.
     */
    public boolean isNewScoreRecord() {
        return this.newScoreRecord;
    }

    /**
     * Get the playing consecutively record score.
     * @return the playing record score
     */
    public int getPlayingScoreRecord() {
        return this.playingScoreRecord;
    }

    /**
     * Get if the new score is a new playing consecutively record.
     * @return true if the new score is a new playing consecutively record.
     */
    public boolean isNewPlayingScoreRecord() {
        return this.newPlayingScoreRecord;
    }

    /**
     * Get coins collected by player given from {@link game.logics.handler.Logics.GameInfo GameInfo} instance.
     * @return player score
     */
    public int getMoney() {
        return this.game.getActualGame().getFinalMoney();
    }

    /**
     * Get current highest score obtained by player.
     * @return first element of the highest scores list
     */
    public Integer getHighestMoneyRecord() {
        return this.moneyRecords.get(0);
    }

    /**
     * Get current least score obtained by player.
     * @return last element of the highest scores list
     */
    private Integer getLowestMoneyRecord() {
        if (this.moneyRecords.isEmpty()) {
            return 0;
        } else {
            return this.moneyRecords.stream().sorted().findFirst().get();
        }
    }

    /**
     * Get if the new number of coins collected is a new highest record.
     * @return true if the new number of coins collected is a new highest record.
     */
    public boolean isNewMoneyRecord() {
        return this.newMoneyRecord;
    }
}

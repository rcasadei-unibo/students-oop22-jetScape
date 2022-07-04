package game.logics.records;

import game.logics.entities.player.Player;
import game.logics.entities.player.Player.PlayerDeath;

import game.logics.handler.Logics.GameInfo;
import game.utility.input.JSONReader;
import game.utility.input.JSONReaderImpl;
import game.utility.input.JSONWriter;
import game.utility.input.JSONWriterImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class handles statistics and records, furthermore it supersedes the
 * reading and writing of them, and is an implementation of {@link Records}.
 */
public final class RecordsImpl implements Records {

    private static final int MAX_NUMBER_OF_SAVED_RECORD = 3;

    private final Supplier<GameInfo> getGame;

    private final Player player;
    private final JSONWriter writer;
    private final JSONReader reader;

    // Statistics and records list
    private final List<Integer> scoreRecords = new ArrayList<>(RecordsImpl.MAX_NUMBER_OF_SAVED_RECORD); // absolute new score records
    private final List<Integer> moneyRecords = new ArrayList<>(RecordsImpl.MAX_NUMBER_OF_SAVED_RECORD); // absolute new money records

    // Statistics and records list
    private int burnedTimes;
    private int zappedTimes;
    private int savedMoney;

    // Data read from game
    private int playingScoreRecord; // higher score obtained by playing consecutively
    private boolean newPlayingScoreRecord;

    private boolean newScoreRecord;
    private boolean newMoneyRecord;

    /*{
        this.scoreRecords.addAll(Collections.nCopies(Records.MAX_NUMBER_OF_SAVED_RECORD, Optional.empty()));
    }*/

    /**
     * Build a new {@link Records}.
     * @param getGame {@link Supplier} of {@link GameInfo} objects used to get
     *   current game informations.
     * @param player {@link Player} object used to get some player informations.
     */
    public RecordsImpl(final Supplier<GameInfo> getGame, final Player player) {
        this.getGame = getGame;

        this.writer = new JSONWriterImpl(this);
        this.reader = new JSONReaderImpl(this);

        //this.game.getNumbersOfGamesPlayed();
        this.player = player;
    }

    /****************************************/
    /***    In and to file operations     ***/
    /****************************************/

    /**
     * {@inheritDoc}
     */
    public void fetch(final GameInfo newGameInfo) {

        final PlayerDeath causeOfDeath;

        //System.out.println(newGameInfo.isGamePlayed());
        //newGameInfo.getGameDate().ifPresent(System.out::println);

        if (player.hasDied()) {
            causeOfDeath = player.getCauseOfDeath();
            switch (causeOfDeath) {
                case BURNED:
                    this.burnedTimes++;
                    break;
                case ZAPPED:
                    this.zappedTimes++;
                    break;
                default:
                    break;
            }

            this.savedMoney = this.savedMoney + newGameInfo.getFinalMoney();
            //this.checkScore(this.getScore());
            this.checkScore(newGameInfo.getFinalScore());
            this.checkMoney(newGameInfo.getFinalMoney());
            //this.getScore();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        this.reader.read();
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        this.writer.write();
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        this.writer.clear();
    }

    /****************************************/
    /***   Calculate and check records    ***/
    /****************************************/

    // TODO use new GameUID Date
    /**
     * This method checks if the new finalScore is a new record and only in
     * this case saves it.
     *
     * @param finalScore
     *   final score in the current game
     */
    private void checkScore(final int finalScore) {

        if (finalScore > this.playingScoreRecord) {
            this.newPlayingScoreRecord = true;
            this.playingScoreRecord = finalScore;
        } else if (finalScore < this.playingScoreRecord) {
            this.newPlayingScoreRecord = false;
        }

        this.newScoreRecord = this.scoreRecords.isEmpty() || finalScore > this.getHighestRecordScore();

        if (this.newScoreRecord || finalScore < this.getHighestRecordScore()
                && finalScore > this.getLowestRecordScore()
                || this.scoreRecords.size() < RecordsImpl.getMaxSavedNumberOfRecords()) {
            this.addScoreRecord(finalScore);
        }
    }

    /**
     * This method checks if the new finalCoins is a new record and only in
     * this case saves it.
     *
     * @param finalCoinsCollected
     *   final number of coins collected in the current game
     */
    private void checkMoney(final int finalCoinsCollected) {

        if (this.moneyRecords.size() < RecordsImpl.getMaxSavedNumberOfRecords()
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
     * {@link RecordsImpl#MAX_NUMBER_OF_SAVED_RECORD}.
     *
     * @return the number of records that will be written to file.
     */
    public static int getMaxSavedNumberOfRecords() {
        return RecordsImpl.MAX_NUMBER_OF_SAVED_RECORD;
    }

    /**
     * {@inheritDoc}
     */
    public int getBurnedTimes() {
        return this.burnedTimes;
    }

    /**
     * {@inheritDoc}
     */
    public int getZappedTimes() {
        return this.zappedTimes;
    }

    /**
     * {@inheritDoc}
     */
    public int getSavedMoney() {
        return this.savedMoney;
    }

    /**
     * {@inheritDoc}
     */
    public void setBurnedTimes(final int readBurnedTimes) {
        this.burnedTimes = readBurnedTimes;
    }

    /**
     * {@inheritDoc}
     */
    public void setZappedTimes(final int readZappedTimes) {
        this.zappedTimes = readZappedTimes;
    }

    /**
     * {@inheritDoc}
     */
    public void setSavedMoney(final int savedMoney) {
        this.savedMoney = savedMoney;
    }

    /**
     * {@inheritDoc}
     */
    public void addScoreRecord(final int newScoreRecord) {
        //this.recordScores.forEach(System.out::println);

        this.scoreRecords.add(newScoreRecord);
        this.scoreRecords.sort(Comparator.reverseOrder());

        if (this.scoreRecords.size() > RecordsImpl.getMaxSavedNumberOfRecords()) {
            //System.out.println("ELIMINATO: "
            //    + this.scoreRecords.get(RecordsImpl.getMaxSavedNumberOfRecords()).toString());
            this.scoreRecords.remove(RecordsImpl.getMaxSavedNumberOfRecords());
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Integer> getScoreRecords() {
        /*final List<Integer> recordScores = new ArrayList<>();

        for (final Optional<Integer> record : this.recordScores) {
            if (record.isPresent()) {
                recordScores.add(record.get());
            }
        }*/
        return List.copyOf(this.scoreRecords);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public void addMoneyRecord(final int newMoneyRecord) {

        this.moneyRecords.add(newMoneyRecord);
        this.moneyRecords.sort(Comparator.reverseOrder());

        if (this.moneyRecords.size() > RecordsImpl.getMaxSavedNumberOfRecords()) {
            //System.out.println("ELIMINATO: "
            //    + this.moneyRecords.get(RecordsImpl.getMaxSavedNumberOfRecords()).toString());
            this.moneyRecords.remove(RecordsImpl.getMaxSavedNumberOfRecords());
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Integer> getMoneyRecords() {
        return List.copyOf(this.moneyRecords);
    }

    /**
     * {@inheritDoc}
     */
    public void setMoneyRecords(final List<Integer> recordCoins) {
        this.moneyRecords.clear();
        this.moneyRecords.addAll(recordCoins);
    }

    /****************************************/
    /*** Getters & Setters from / to game ***/
    /****************************************/

    /**
     * {@inheritDoc}
     */
    public int getScore() {
        return this.getGame.get().getFinalScore();
    }

    /**
     * {@inheritDoc}
     */
    public int getHighestRecordScore() {
        if (this.scoreRecords.isEmpty()) {
            return 0;
        } else {
            return this.scoreRecords.get(0);
        }
    }

    /**
     * Get current least score obtained by player.
     * @return the last element of the highest scores list
     */
    private int getLowestRecordScore() {
        if (this.scoreRecords.isEmpty()) {
            return 0;
        } else {
            return this.scoreRecords.stream().sorted().findFirst().get();
            //return this.scoreRecords.get(this.scoreRecords.size() - 1);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNewScoreRecord() {
        return this.newScoreRecord;
    }

    /**
     * {@inheritDoc}
     */
    public int getPlayingScoreRecord() {
        return this.playingScoreRecord;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNewPlayingScoreRecord() {
        return this.newPlayingScoreRecord;
    }

    /**
     * {@inheritDoc}
     */
    public int getCollectedMoney() {
        return this.getGame.get().getFinalMoney();
    }

    /**
     * {@inheritDoc}
     */
    public int getHighestMoneyRecord() {
        if (this.moneyRecords.isEmpty()) {
            return 0;
        } else {
            return this.moneyRecords.get(0);
        }
    }

    /**
     * Get current least score obtained by player.
     * @return last element of the highest scores list
     */
    private int getLowestMoneyRecord() {
        if (this.moneyRecords.isEmpty()) {
            return 0;
        } else {
            return this.moneyRecords.stream().sorted().findFirst().get();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isNewMoneyRecord() {
        return this.newMoneyRecord;
    }
}

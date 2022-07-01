package game.logics.records;

import java.util.List;

import game.logics.handler.Logics.GameInfo;

/**
 * This interface models a {@code Records} handler.
 */
public interface Records {

    /****************************************/
    /***    In and to file operations     ***/
    /****************************************/

    /**
     * Get data for updating in game, calling the data getters.
     *
     * @param newGameInfo GameInfo passed via {@link Supplier}
     * by {@link #fetchGameEnded()}
     */
    void fetch(GameInfo newGameInfo);

    /**
     * Read from file.
     */
    void refresh();

    /**
     * Write to file.
     */
    void update();

    /****************************************/
    /***   Calculate and check records    ***/
    /****************************************/

    /**
     * This method checks if the new finalScore is a new record and only in
     * this case saves it.
     *
     * @param finalScore
     *   final score in the current game
     */
    void checkScore(int finalScore);

    /**
     * This method checks if the new finalCoins is a new record and only in
     * this case saves it.
     *
     * @param finalCoinsCollected
     *   final number of coins collected in the current game
     */
    void checkMoney(int finalCoinsCollected);

    /****************************************/
    /*** Getters & Setters from / to file ***/
    /****************************************/

    /**
     * This method is used to get burnedTimes value.
     *
     * @return how many times Barry died burned.
     */
    int getBurnedTimes();

    /**
     * This method is used to get zappedTimes value.
     *
     * @return how many times Barry died electrocuted.
     */
    int getZappedTimes();

    /**
     * This method is used to get how much savedMoney the player owns.
     *
     * @return how many money the player has saved.
     */
    int getSavedMoney();

    /**
     * This method is used to set burnedTimes value.
     *
     * @param readBurnedTimes how many times Barry died burned.
     */
    void setBurnedTimes(int readBurnedTimes);

    /**
     * This method is used to set zappedTimes value.
     *
     * @param readZappedTimes how many times Barry died electrocuted.
     */
    void setZappedTimes(int readZappedTimes);

    /**
     * This method is used to set savedMoney value.
     *
     * @param savedMoney how much money the player has saved.
     */
    void setSavedMoney(int savedMoney);

    /**
     * This method adds a score record entry.
     *
     * @param newScoreRecord the new score record
     */
    void addScoreRecord(int newScoreRecord);

    /**
     * This method gets the score record list.
     * 
     * This method can be used to write the list to file.
     * 
     * @return the score record list
     */
    List<Integer> getScoreRecords();

    /**
     * This method sets the score record list.
     * 
     * This method is used to read that list from file.
     * 
     * @param recordScores the new score record list
     */
    void setScoreRecords(List<Integer> recordScores);

    /**
     * This method adds a money record entry.
     *
     * @param newMoneyRecord the new money record
     */
    void addMoneyRecord(int newMoneyRecord);

    /**
     * This method gets the money record list.
     * 
     * This method can be used to write the list to file.
     * 
     * @return the money record list
     */
    List<Integer> getMoneyRecords();

    /**
     * This method sets the money record list.
     * 
     * This method is used to read that list from file.
     * 
     * @param recordCoins the new money record list
     */
    void setMoneyRecords(List<Integer> recordCoins);

    /****************************************/
    /*** Getters & Setters from / to game ***/
    /****************************************/

    /**
     * Get player score form {@link game.logics.handler.Logics.GameInfo GameInfo} instance.
     * @return the player score
     */
    int getScore();

    /**
     * Get current highest score obtained by player.
     * @return the first element of the highest scores list
     */
    int getHighestRecordScore();

    /**
     * Get if the new score is a new highest record.
     * @return true if the new score is a new highest record.
     */
    boolean isNewScoreRecord();

    /**
     * Get the playing consecutively record score.
     * @return the playing record score
     */
    int getPlayingScoreRecord();

    /**
     * Get if the new score is a new playing consecutively record.
     * @return true if the new score is a new playing consecutively record.
     */
    boolean isNewPlayingScoreRecord();

    /**
     * Get coins collected by player given from {@link game.logics.handler.Logics.GameInfo GameInfo} instance.
     * @return player score
     */
    int getCollectedMoney();

    /**
     * Get current highest score obtained by player.
     * @return first element of the highest scores list
     */
    int getHighestMoneyRecord();

    /**
     * Get if the new number of coins collected is a new highest record.
     * @return true if the new number of coins collected is a new highest record.
     */
    boolean isNewMoneyRecord();
}

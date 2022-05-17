package game.logics.records;

import game.logics.entities.player.Player;
import game.logics.entities.player.Player.PlayerDeath;
import game.utility.input.JSONWriter;
import game.logics.handler.Logics.GameID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

public class Records {

    // Data for building JSON data table
    private final static Set<String> keySet = new HashSet<>();
    private final static Map<String, Object> recordsMap = new HashMap<>();

    private final JSONWriter writer = new JSONWriter(this);

    private final Player player;

    //TODO complete list
    // List of keys for JSON files
    private final static String keyName = "name";
    private final static String keyAge = "age";

    // Statistics and records list
    // TODO complete list
    private String name;
    private int age;
    // private String[] position;
    // private Map<String, Integer> salary;

    // Data read from game
    private int score;
    private PlayerDeath causeOfDeath;

    private static int playingRecordScore = 0; // higher score obtained by playing consecutively
    private static boolean newPlayingRecordScore = false;

    private int recordScore; // absolute new record
    private boolean newRecordScore = false;

    //TODO
    public Records(final Player player) {
        this.player = player;
    }

    // TODO Set (maybe) with singleton
    public void build() {
    	keySet.add(keyName);
    	keySet.add(keyAge);
    	
    	recordsMap.put(keyName, name);
    	recordsMap.put(keyAge, age);
    	
    	this.writer.build();
    }
    
    public static Set<String> getKeySet() {
    	return Records.keySet;
    }
    
    public static Map<String,Object> getRecordsMap() {
		return Records.recordsMap;
	}
    
    /****************************************/
    /***    In and to file operations    ***/
    /****************************************/

    /**
     * Read from file
     */
    public void refresh() {
    	this.writer.read();
    }
    
	private static GameID IDGame = new GameID();

	private boolean checkAndSet(final GameID newGameID) {

		final BiPredicate<GameID, GameID> checkIfNew =
				(oldID,newID) -> oldID.getGameDate() != newID.getGameDate();
		boolean isNewID = false;

        if (!newGameID.isGamePlayed()) {
            isNewID = true;
        } else if(checkIfNew.test(IDGame, newGameID)) {
            isNewID = true;
            Records.IDGame = newGameID;
        }
        return isNewID;
    }

    /**
     * Get data for updating in game, calling the data getters
     */
	//TODO add new records
    public void fetch(final GameID gameID) {

        //System.out.println(gameID.isGamePlayed());
        //gameID.getGameDate().ifPresent(System.out::println);

        // Only if new gameID
        if (this.checkAndSet(gameID)) {
            if(player.hasDied()) {
                this.score = player.getCurrentScore();
                System.out.println(score);
                this.causeOfDeath = player.getCauseOfDeath();
            }
        //this.getScore();
        }
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
	public void checkScoreRecord(final int finalScore) {
		
		this.score = finalScore;
		
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

    public String getName() {
    	return this.name;
    }
    
    public int getAge() {
    	return this.age;
    }
    
    public int getRecordScore() {
    	return this.recordScore;
    }

    public void setName(final String name) {
    	this.name = name;
    }
    
    public void setAge(final int age) {
    	this.age = age;
    }
    
    public void setRecordScore(int recordScore) {
    	this.recordScore = recordScore;
    }
    
    /****************************************/
    /*** Getters & Setters from / to game ***/
    /****************************************/
    
    public int getScore() {
    	return this.score;
    }

	public boolean isNewRecordScore() {
		return this.newRecordScore;
	}

	public boolean isNewPlayingRecordScore() {
		return Records.newPlayingRecordScore;
	}
}

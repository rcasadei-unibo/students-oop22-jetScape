package game.logics.records;

import game.logics.entities.player.Player;
import game.utility.input.JSONWriter;
import game.logics.handler.Logics.GameID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Records {

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
    private int recordScore;
    // private String[] position;
    // private Map<String, Integer> salary;

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

	// getters and setters
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
    
    public static Set<String> getKeySet() {
    	return Records.keySet;
    }
    
    public static Map<String,Object> getRecordsMap() {
		return Records.recordsMap;
	}

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

    	if(!newGameID.isGamePlayed()) {
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

    	//Pair<Integer, Date> gameID = getGameID.get();
    	
    	// Only if new gameID
    	if(this.checkAndSet(gameID)) {
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
}

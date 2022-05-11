package game.logics.records;

import game.logics.entities.player.Player;
import game.utility.input.JSONWriter;

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
    
    /**
     * Get records in game, calling the data getters
     */
    //TODO
    public void fetch() {
        //this.getScore();
    	/*if(player.hasDied()) {
    		this.setScore(player.getCurrentScore());
    		player.getCauseOfDeath();
    	}*/
    }
   
    /**
     * Write to file
     */
    public void update() {
        this.writer.write();
    }
}
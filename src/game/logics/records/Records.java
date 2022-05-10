package game.logics.records;

import game.utility.input.JSONWriter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Records {

    private final static Set<String> keySet = new HashSet<>();
    private final static Map<String, Object> recordsMap = new HashMap<>();

    //TODO
    private final static String keyName = "name";
    private final static String keyAge = "age";

    private final JSONWriter writer = new JSONWriter(this);

    // Statistics list
    private String name;
    private int age;

  //  private String[] position;
  //  private Map<String, Integer> salary;

    //TODO
    public Records() {

    }
    
    // Set as singleton
    public void build() {
    	keySet.add(keyName);
    	keySet.add(keyAge);
    	
    	recordsMap.put(keyName, name);
    	recordsMap.put(keyAge, age);
    	
    	this.writer.build();
    }

	//..getters setters
    public String getName() {
    	return this.name;
    }
    
    public int getAge() {
    	return this.age;
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
    	
    }
   
    /**
     * Write to file
     */
    //TODO
    public void update() {
        
        this.writer.write();
    }    
}
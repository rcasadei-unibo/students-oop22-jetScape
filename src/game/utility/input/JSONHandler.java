package game.utility.input;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.logics.records.Records;

public class JSONHandler {

    private final Records records;
    
    // Do not change path without updating .gitignore tracked file
    private static final String sep = File.separator;
    private static final String filePath = "res" + sep + "game" + sep + "data" + sep + "records.json";
    private static final File file = new File(filePath);

    // Data for building JSON data table
    private final static List<String> keyList = new ArrayList<>();
    private final static Map<String, Object> recordsMap = new HashMap<>();
    
    //TODO complete list
    // List of keys for JSON files
    private final static String keyRecordScore = "record";
    private final static String keyBurned = "burned";
    private final static String keyZapped = "zapped";

    // Static initializer
    static {
        //TODO add missing
        keyList.add(keyRecordScore);
        keyList.add(keyBurned);
        keyList.add(keyZapped);

        //this.writer.build();
    }

    public JSONHandler(final Records records) {
        this.records = records;
        
        this.buildMap();
    }

    public void buildMap() {
        recordsMap.put(keyRecordScore, this.records.getRecordScore());
        recordsMap.put(keyBurned, this.records.getBurnedTimes());
        recordsMap.put(keyZapped, this.records.getZappedTimes());
    }
    
    /**
     * Refresh recordsMap with data read from records, to write
     */
    protected void download() {
        recordsMap.replace(keyRecordScore, this.records.getRecordScore());
        recordsMap.replace(keyBurned, this.records.getBurnedTimes());
        recordsMap.replace(keyZapped, this.records.getZappedTimes());
    }

    protected static List<String> getKeyList() {
        return JSONHandler.keyList;
    }

    protected static Map<String,Object> getRecordsMap() {
        return JSONHandler.recordsMap;
    }

    protected File getFile() {
        return JSONHandler.file;
    }
}

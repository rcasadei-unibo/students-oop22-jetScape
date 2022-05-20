package game.utility.input;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import game.logics.records.Records;

public class JSONHandler {

    private final Records records;
    
    // Do not change path without updating .gitignore tracked file
    private static final String SEP = File.separator;
    private static final String FILE_PATH = "res" + SEP + "game" + SEP + "data" + SEP + "records.json";
    private static final File FILE = new File(FILE_PATH);

    // Data for building JSON data table
    private final static List<String> KEY_LIST = new ArrayList<>();
    private final static Map<String, Object> RECORDS_MAP = new HashMap<>();

    //TODO complete list
    // List of keys for JSON files
    private final static String KEY_RECORD_SCORE = "record%i"; // %i represent record index
    private final static List<String> KEY_RECORD_SCORES = new ArrayList<>();

    private final static String KEY_BURNED = "burned";
    private final static String KEY_ZAPPED = "zapped";

    // Static initializer
    static {
        //TODO add missing keys
        for (Integer i = 0; i < Records.getSavedNumberOfRecords(); i++) {
            KEY_RECORD_SCORES.add(KEY_RECORD_SCORE.replace("%i", i.toString()));
        }
        KEY_LIST.addAll(KEY_RECORD_SCORES);
        KEY_LIST.add(KEY_BURNED);
        KEY_LIST.add(KEY_ZAPPED);

        //this.writer.build();
    }

    private void buildMap() {

        IntStream.range(0, Records.getSavedNumberOfRecords()).forEach(i -> {
            RECORDS_MAP.put(KEY_RECORD_SCORES.get(i), 0);
        });
        //RECORDS_MAP.forEach((x,y) -> System.out.println(x + " - " + y));

        RECORDS_MAP.put(KEY_BURNED, this.records.getBurnedTimes());
        RECORDS_MAP.put(KEY_ZAPPED, this.records.getZappedTimes());
    }

    public JSONHandler(final Records records) {
        this.records = records;
        //KEY_LIST.forEach(System.out::println);
        //RECORDS_MAP.keySet().forEach(System.out::println);
        this.buildMap();
    }

    
    /**
     * Refresh recordsMap with data read from records, to write
     */
    protected void download() {

        IntStream.range(0, this.records.getRecordScores().size()).forEach(i -> {
            RECORDS_MAP.replace(KEY_RECORD_SCORES.get(i), this.records.getRecordScores().get(i));
        });

        final Map<String, Object> recordsMap = new HashMap<>(RECORDS_MAP);
        recordsMap.entrySet().stream()
               .filter(e -> KEY_RECORD_SCORES.stream().anyMatch(x -> x == e.getKey()))
               .filter(e -> ((Integer) e.getValue()) == 0)
              // .forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
               .forEach(x -> RECORDS_MAP.remove(x.getKey()));

        RECORDS_MAP.replace(KEY_BURNED, this.records.getBurnedTimes());
        RECORDS_MAP.replace(KEY_ZAPPED, this.records.getZappedTimes());
        
        //RECORDS_MAP.forEach((x,y) -> System.out.println(x + " - " + y));
    }

    protected static List<String> getKeyList() {
        return JSONHandler.KEY_LIST;
    }

    protected static Map<String,Object> getRecordsMap() {
        return JSONHandler.RECORDS_MAP;
    }

    protected File getFile() {
        return JSONHandler.FILE;
    }
}

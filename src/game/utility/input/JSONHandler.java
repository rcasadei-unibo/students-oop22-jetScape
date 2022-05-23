package game.utility.input;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import game.logics.records.Records;

/**
 * Non-instantiable class used by {@link JSONWriter} and {@link JSONReader} to
 * get common file information used to read &amp; write information to and from a
 * JSON formatted file.
 */
public class JSONHandler {

    private final Records records;

    // Do not change path without updating .gitignore tracked file
    private static final String SEP = File.separator;
    private static final String FILE_PATH = "res" + SEP + "game" + SEP + "data" + SEP + "records.json";
    private static final File FILE = new File(FILE_PATH);

    // Data for building JSON data table
    private static final List<String> KEY_LIST = new ArrayList<>();
    private static final Map<String, Object> RECORDS_MAP = new HashMap<>();

    //TODO complete list
    // List of keys for JSON files
    private static final String KEY_RECORD_SCORE = "record%i"; // %i represent record index
    private static final List<String> KEY_RECORD_SCORES = new ArrayList<>();

    private static final String KEY_BURNED = "burned";
    private static final String KEY_ZAPPED = "zapped";

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

    /**
     * {@link JSONHandler} constructor, called to set internal {@link Records} parameter.
     *
     * @param records {@link Records} Place to get and set statistics &amp; records data
     */
    protected JSONHandler(final Records records) {
        this.records = records;
        //KEY_LIST.forEach(System.out::println);
        //RECORDS_MAP.keySet().forEach(System.out::println);
        this.buildMap();
    }

    /**
     * Refresh recordsMap with information data read from records that have to
     *   be written to file.
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

    /**
     * This method is used to get the ordered list of keys.
     * @return the {@link List} JSONHandler.KEY_LIST
     */
    protected static List<String> getKeyList() {
        return JSONHandler.KEY_LIST;
    }

    /**
     * This method is used to get actual records.
     * @return the {@link Map} JSONHandler.RECORDS_MAP
     */
    protected static Map<String, Object> getRecordsMap() {
        return JSONHandler.RECORDS_MAP;
    }

    /**
     * This method is used to get the file used for writing and reading.
     * @return the {@link File} JSONHandler.FILE
     */
    protected File getFile() {
        return JSONHandler.FILE;
    }
}

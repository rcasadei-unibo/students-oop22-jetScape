package game.utility.input;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import game.logics.records.Records;

/**
 * Non-instantiable class used by {@link JSONWriter} and {@link JSONReader} to
 * get common file information used to read &amp; write information to and from a
 * JSON formatted file.
 */
public class JSONHandler {

    // Do not change path without updating .gitignore tracked file
    private static final String SEP = File.separator;
    private static final String FILE_PATH = System.getProperty("user.dir") + SEP
            + "res" + SEP + "game" + SEP + "data" + SEP + "records.json";
    private static final File FILE = new File(FILE_PATH);

    // Data for building JSON data table
    private static final List<JsonKey> KEY_LIST = new ArrayList<>();
    private static final Map<JsonKey, Object> RECORDS_MAP = new HashMap<>(Records.getSavedNumberOfRecords());

    //TODO complete list
    // List of keys for JSON files
    private static final JsonKey PSEUDOKEY_RECORD_SCORE = new FileKey("record%i", 0); // %i represents record index
    private static final String STRING_RECORD_SCORE = "record";

    private static final List<JsonKey> KEY_RECORD_SCORES = new ArrayList<>(Records.getSavedNumberOfRecords());

    private static final JsonKey KEY_BURNED = new FileKey("burned", 0);
    private static final JsonKey KEY_ZAPPED = new FileKey("zapped", 0);

    private final Records records;

    // Static initializer
    static {
        // create all Record Score keys using PSEUDOKEY_RECORD_SCORE and an index
        for (Integer i = 0; i < Records.getSavedNumberOfRecords(); i++) {
            KEY_RECORD_SCORES.add(new FileKey(
                    PSEUDOKEY_RECORD_SCORE.getKey().replace("%i", i.toString()),
                    PSEUDOKEY_RECORD_SCORE.getValue()));
        }
        KEY_LIST.addAll(KEY_RECORD_SCORES);
        KEY_LIST.add(KEY_BURNED);
        KEY_LIST.add(KEY_ZAPPED);

        //TODO add missing keys
    }

    private void buildMap() {

        IntStream.range(0, Records.getSavedNumberOfRecords()).forEach(i -> {
            RECORDS_MAP.put(KEY_RECORD_SCORES.get(i), 0);
        });
        //RECORDS_MAP.forEach((x, y) -> System.out.println(x + " - " + y));

        RECORDS_MAP.put(KEY_BURNED, 0);
        RECORDS_MAP.put(KEY_ZAPPED, 0);

        //KEY_LIST.forEach(System.out::println);
        //RECORDS_MAP.keySet().forEach(System.out::println);
    }

    /**
     * {@link JSONHandler} constructor, called to set internal {@link Records} parameter.
     *
     * @param records {@link Records} Place to get and set statistics &amp; records data
     */
    protected JSONHandler(final Records records) {
        this.records = records;
        this.buildMap();
    }

    /**
     * Refresh recordsMap with information data read from records that have to
     *   be written to file.
     */
    protected void download() {

        // Refreshes the map
        final List<Integer> recordsList = this.records.getRecordScores();
        IntStream.range(0, this.records.getRecordScores().size()).forEach(i -> {
            RECORDS_MAP.replace(KEY_RECORD_SCORES.get(i), recordsList.get(i));
        });

        RECORDS_MAP.replace(KEY_BURNED, this.records.getBurnedTimes());
        RECORDS_MAP.replace(KEY_ZAPPED, this.records.getZappedTimes());

        //RECORDS_MAP.forEach((x,y) -> System.out.println(x + " - " + y));
    }

    /**
     * Overwrite recordsMap with information data read from file that have to
     *   be loaded as new records.
     * @param json 
     */
    protected void upload(final JsonObject json) {

        final List<Integer> recordsReadList = new ArrayList<>(Records.getSavedNumberOfRecords());

        // Overwrites the map
        IntStream.range(0, Records.getSavedNumberOfRecords()).forEach(x -> {
            final int receivedValue = json.getIntegerOrDefault(KEY_RECORD_SCORES.get(x));
            if (receivedValue != (int) PSEUDOKEY_RECORD_SCORE.getValue()) {
                recordsReadList.add(x, receivedValue);
            }
        });
        records.setRecordScores(recordsReadList);

        records.setBurnedTimes(json.getInteger(KEY_BURNED));
        records.setZappedTimes(json.getInteger(KEY_ZAPPED));

        try {
            json.requireKeys(KEY_BURNED, KEY_ZAPPED);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to get the ordered list of keys.
     * @return the {@link List} JSONHandler.KEY_LIST
     */
    protected static List<JsonKey> getKeyList() {
        return JSONHandler.KEY_LIST;
    }

    /**
     * This method is used to get record base string used to form related keys.
     * @return {@link String} JSONHandler.STRING_RECORD_SCORE
     */
    protected static String getStringRecordScore() {
        return JSONHandler.STRING_RECORD_SCORE;
    }

    /**
     * This method is used to get actual records.
     * @return the {@link Map} JSONHandler.RECORDS_MAP
     */
    protected static Map<JsonKey, Object> getRecordsMap() {
        return JSONHandler.RECORDS_MAP;
    }

    /**
     * This method is used to get the file used for writing and reading.
     * @return the {@link File} JSONHandler.FILE
     */
    protected File getFile() {
        return JSONHandler.FILE;
    }

    /**
     * Represents a key that could be written to file and owns also a default
     * value.
     *
     */
    protected static final class FileKey implements JsonKey, Serializable {

        private static final long serialVersionUID = 1L;

        private final String key;
        private final Object defaultValue;

        /**
         * Builds a new {@link FileKey}.
         *
         * @param key the new key
         * @param value defaultVaue assigned to {@code key}
         */
        public FileKey(final String key, final Object value) {
            this.key = key;
            this.defaultValue = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getKey() {
            return this.key;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValue() {
            return this.defaultValue;
        }
    }
}

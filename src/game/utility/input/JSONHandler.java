package game.utility.input;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import game.logics.records.Records;
import game.logics.records.RecordsImpl;

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
    private static final Map<JsonKey, Object> RECORDS_MAP = new HashMap<>(RecordsImpl.getMaxSavedNumberOfRecords());

    // List of keys for JSON files
    private static final JsonKey PSEUDOKEY_RECORD_LIST = new FileKey("record%i", 0); // %i represents record index

    private static final String STRING_SCORE_RECORD = "score";
    private static final String STRING_MONEY_RECORD = "money";

    private static final List<JsonKey> KEY_SCORE_RECORDS = new ArrayList<>(RecordsImpl.getMaxSavedNumberOfRecords());
    private static final List<JsonKey> KEY_MONEY_RECORDS = new ArrayList<>(RecordsImpl.getMaxSavedNumberOfRecords());

    private static final JsonKey KEY_MONEY = new FileKey("savedMoney", 0);
    private static final JsonKey KEY_BURNED = new FileKey("burned", 0);
    private static final JsonKey KEY_ZAPPED = new FileKey("zapped", 0);

    private final Records records;

    // Static initializer
    static {
        // create all Record Score keys using PSEUDOKEY_RECORD_SCORE and an index
        for (Integer i = 0; i < RecordsImpl.getMaxSavedNumberOfRecords(); i++) {
            KEY_SCORE_RECORDS.add(new FileKey(
                    PSEUDOKEY_RECORD_LIST.getKey()
                            .replace("record", STRING_SCORE_RECORD)
                            .replace("%i", i.toString()),
                    PSEUDOKEY_RECORD_LIST.getValue()));
            KEY_MONEY_RECORDS.add(new FileKey(
                    PSEUDOKEY_RECORD_LIST.getKey()
                            .replace("record", STRING_MONEY_RECORD)
                            .replace("%i", i.toString()),
                    PSEUDOKEY_RECORD_LIST.getValue()));
        }
        KEY_LIST.addAll(KEY_SCORE_RECORDS);
        KEY_LIST.addAll(KEY_MONEY_RECORDS);
        KEY_LIST.add(KEY_MONEY);
        KEY_LIST.add(KEY_BURNED);
        KEY_LIST.add(KEY_ZAPPED);
    }

    /**
     * {@link JSONHandler} constructor, called to set internal {@link Records} parameter.
     *
     * @param records {@link Records} Place to get and set statistics &amp; records data
     */
    protected JSONHandler(final Records records) {
        this.records = records;

        // Builds the map
        Stream.concat(KEY_SCORE_RECORDS.stream(), KEY_MONEY_RECORDS.stream())
                .forEach(key -> RECORDS_MAP.put(key, 0));
        //RECORDS_MAP.forEach((x, y) -> System.out.println(x + " - " + y));

        RECORDS_MAP.put(KEY_MONEY, 0);
        RECORDS_MAP.put(KEY_BURNED, 0);
        RECORDS_MAP.put(KEY_ZAPPED, 0);

        //KEY_LIST.forEach(System.out::println);
        //RECORDS_MAP.keySet().forEach(System.out::println);
    }

    /**
     * Refresh recordsMap with information data read from records that have to
     *   be written to file.
     *
     *  This method writes to file.
     */
    protected void upload() {

        // Refreshes the map
        final List<Integer> scoreRecordsList = this.records.getScoreRecords();
        IntStream.range(0, this.records.getScoreRecords().size()).forEach(i -> {
            RECORDS_MAP.replace(KEY_SCORE_RECORDS.get(i), scoreRecordsList.get(i));
        });

        final List<Integer> moneyRecordsList = this.records.getMoneyRecords();
        IntStream.range(0, this.records.getMoneyRecords().size()).forEach(i -> {
            RECORDS_MAP.replace(KEY_MONEY_RECORDS.get(i), moneyRecordsList.get(i));
        });

        RECORDS_MAP.replace(KEY_MONEY, this.records.getSavedMoney());
        RECORDS_MAP.replace(KEY_BURNED, this.records.getBurnedTimes());
        RECORDS_MAP.replace(KEY_ZAPPED, this.records.getZappedTimes());

        //RECORDS_MAP.forEach((x,y) -> System.out.println(x + " - " + y));
    }

    /**
     * Overwrite recordsMap with information data read from file that have to
     *   be loaded as new records.
     *
     * This method reads from file.
     *
     * @param json the {@link JsonObject} read from file
     */
    protected void download(final JsonObject json) {

        final List<Integer> scoreRecordsReadList = new ArrayList<>(RecordsImpl.getMaxSavedNumberOfRecords());
        final List<Integer> moneyRecordsReadList = new ArrayList<>(RecordsImpl.getMaxSavedNumberOfRecords());

        // Overwrites the score map
        IntStream.range(0, RecordsImpl.getMaxSavedNumberOfRecords()).forEach(x -> {
            final int receivedValue = json.getIntegerOrDefault(KEY_SCORE_RECORDS.get(x));
            if (receivedValue != (int) PSEUDOKEY_RECORD_LIST.getValue()) {
                scoreRecordsReadList.add(x, receivedValue);
            }
        });
        // Overwrites the money map
        IntStream.range(0, RecordsImpl.getMaxSavedNumberOfRecords()).forEach(x -> {
            final int receivedValue = json.getIntegerOrDefault(KEY_MONEY_RECORDS.get(x));
            if (receivedValue != (int) PSEUDOKEY_RECORD_LIST.getValue()) {
                moneyRecordsReadList.add(x, receivedValue);
            }
        });

        this.records.setScoreRecords(scoreRecordsReadList);
        this.records.setMoneyRecords(moneyRecordsReadList);

        this.records.setBurnedTimes(json.getInteger(KEY_BURNED));
        this.records.setZappedTimes(json.getInteger(KEY_ZAPPED));
        this.records.setSavedMoney(json.getInteger(KEY_MONEY));

        try {
            json.requireKeys(KEY_BURNED, KEY_ZAPPED, KEY_MONEY);
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
     * This method is used to get record score base string used to form related keys.
     * @return {@link String} JSONHandler.STRING_SCORE_RECORD
     */
    protected static String getStringScoreRecord() {
        return JSONHandler.STRING_SCORE_RECORD;
    }

    /**
     * This method is used to get record money base string used to form related keys.
     * @return {@link String} JSONHandler.STRING_MONEY_RECORD
     */
    protected static String getStringMoneyRecord() {
        return JSONHandler.STRING_MONEY_RECORD;
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

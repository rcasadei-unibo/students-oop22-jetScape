package game.utility.input;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import game.logics.records.Records;

/**
 * This class writes information to a JSON file.
 * 
 * This class is an implementation of {@link JSONWriter}
 */
public class JSONWriterImpl extends JSONHandler implements JSONWriter {

    private final JsonObject json = new JsonObject();

    /**
     * Builds a {@link JSONWriterImpl}.
     *
     * @param records records to get data to write
     */
    public JSONWriterImpl(final Records records) {
        super(records);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        final File file = JSONHandler.getFile();
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void write() {
        super.download();
        try (
            FileWriter fw = new FileWriter(super.getFile());
        ) {
            this.toJson(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
            System.err.println("Error while trying to write to JSON file!");
        }
        return writable.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final Writer writer) throws IOException {

        //RecordsImpl.getKeySet().forEach(key -> json.put(key, recordsMap.get(key)));

        // Creates a copy map to iterate and remove elements in the original map
        final Map<JsonKey, Object> writtenRecordsMap = new HashMap<>(super.getRecordsMap());

        // Prevent all score records associated with 0 to be written into file
        super.getRecordsMap().entrySet().stream()
               // Remove all elements that don't have STRING_SCORE_RECORD string inside key (they don't matter here)
               //.filter(e -> e.getKey().contains(STRING_SCORE_RECORD.replaceAll("[^\\\\]%i", "")))
               //.filter(e -> super.get.stream().anyMatch(x -> x == e.getKey()))
               .filter(e -> e.getKey().getKey().contains(super.getStringScoreRecord())
                       || e.getKey().getKey().contains(super.getStringMoneyRecord()))
               .filter(e -> ((Integer) e.getValue()) == 0)
               //.peek(x -> System.out.println(x.getKey() + " - " + x.getValue()));
               .forEach(x -> writtenRecordsMap.remove(x.getKey()));

        //this.json.putAll(writtenRecordsMap);
        /*writtenRecordsMap.entrySet().forEach(entry -> {
            this.json.put(entry.getKey(), entry.getValue());
        });*/
        writtenRecordsMap.forEach(json::put);

        // Writes the object physically
        json.toJson(writer);
    }
}

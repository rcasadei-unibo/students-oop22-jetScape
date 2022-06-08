package game.utility.input;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import game.logics.records.Records;

/**
 * This class writes information to a JSON file.
 */
public class JSONWriter extends JSONHandler implements Jsonable {

    private final JsonObject json = new JsonObject();

    /**
     * Builds a {@link JSONWriter}.
     *
     * @param records records to get data to write
     */
    public JSONWriter(final Records records) {
        super(records);
    }

    /**
     * Write informations passed by {@link JSONHandler}.
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
    /*
        Records.getKeySet().forEach(key -> {
            json.put(key, recordsMap.get(key));
        });
    */

        // Creates a copy map to iterate and remove elements in the original map
        final Map<JsonKey, Object> writtenRecordsMap = new HashMap<>(super.getRecordsMap());

        // Prevent all score records associated with 0 to be written into file
        super.getRecordsMap().entrySet().stream()
               // Remove all elements that don't have PSEUDOKEY_RECORD_SCORE string inside key (they don't matter here)
               //.filter(e -> e.getKey().contains(PSEUDOKEY_RECORD_SCORE.replaceAll("[^\\\\]%i", "")))
               //.filter(e -> super.get.stream().anyMatch(x -> x == e.getKey()))
               .filter(e -> e.getKey().getKey().contains(super.getStringRecordScore()))

               .filter(e -> ((Integer) e.getValue()) == 0)
                 //.forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
               .forEach(x -> writtenRecordsMap.remove(x.getKey()));

        //json.putAll(writtenRecordsMap);
        /*writtenRecordsMap.entrySet().forEach(entry -> {
            json.put(entry.getKey(), entry.getValue());
        });*/
        writtenRecordsMap.forEach(json::put);

        // Writes the object physically
        json.toJson(writer);
    }
}

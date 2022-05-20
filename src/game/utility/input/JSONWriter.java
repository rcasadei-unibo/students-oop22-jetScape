package game.utility.input;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import game.logics.records.Records;

/**
 * This class writes information to a JSON file.
 */
public class JSONWriter extends JSONHandler implements Jsonable, Serializable {

    private static final long serialVersionUID = 1L;
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
        json.putAll(super.getRecordsMap());
        // json.put("position", this.getPosition());
        // json.put("salary", this.getSalary());

        // Writes the object physically
        json.toJson(writer);
    }
}

package game.utility.input;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import game.logics.records.Records;

public class JSONWriter extends JSONHandler implements Jsonable, Serializable {

    private static final long serialVersionUID = 1L;
    private final JsonObject json = new JsonObject();

    public JSONWriter(final Records records) {
        super(records);
    }

	/**
	 * Get informations that have to be written to file
	 *//*
	//TODO
	public void read() {

	}
*/
    /**
     * Write informations passed by {@linkplain game.utility.input.JsonWriter}
     */
    public void write() {
        super.download();
        try (
            FileWriter fw = new FileWriter(super.getFile());
        ) {
            this.toJson(fw);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
        }
        return writable.toString();
    }

	//TODO
    @Override
    /**
     * {@inheritDoc}
     */
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

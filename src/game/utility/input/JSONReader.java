package game.utility.input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import game.logics.records.Records;

/**
 * This class reads information from a JSON file.
 */
public class JSONReader extends JSONHandler {

    private JsonObject json = new JsonObject();

    /**
     * Builds a {@link JSONReader}.
    *
    * @param records records to send read data
    */
    public JSONReader(final Records records) {
        super(records);
    }

    /**
     * Read informations passed by String parameter.
     *
     * @param string A string, formatted in JSON, that represents the Jsonable.
     */
    public void read(final String string) {

        try (
            Reader reader = new StringReader(string);
        ) {
            this.fromJson(reader);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * Read informations written by {@link JSONWriter}.
     */
    public void read() {

        try (
            Reader reader = new FileReader(super.getFile());
        ) {
            this.fromJson(reader);
            super.download(json);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            System.err.println("File record non esistente...");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    /**
     * Deserialize from a JSON formatted stream.
     *
     * @param reader where the resulting JSON text should be got
     * @throws IOException - when the reader encounters an I/O error
     */
    private void fromJson(final Reader reader) throws IOException {

        try {
            json = (JsonObject) Jsoner.deserialize(reader);
        } catch (JsonException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.download(json);
    }
}

package game.utility.input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Collectors;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import game.logics.records.Records;

/**
 * This class reads information from a JSON file.
 * 
 * This class is an implementation of {@link JSONReader}
 */
public class JSONReaderImpl extends JSONHandler implements JSONReader {

    private static final String IO_EXCEPTION_STRING = "An error occurred while trying to read from JSON file!";
    private static final String JSON_EXCEPTION_STRING = "An error occurred while trying to parse the JSON file!";

    private JsonObject json = new JsonObject();

    /**
     * Builds a {@link JSONReaderImpl}.
    *
    * @param records records to send read data
    */
    public JSONReaderImpl(final Records records) {
        super(records);
    }

    /**
     * {@inheritDoc}
     */
    public void read(final String string) {

        try (
            Reader reader = new StringReader(string);
        ) {
            this.fromJson(reader);
            super.upload(json);
        } catch (IOException e1) {
            System.err.println(IO_EXCEPTION_STRING);
            e1.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void read() {

        try (
            Reader reader = new FileReader(super.getFile());
        ) {
            this.fromJson(reader);
            super.upload(json);
        } catch (FileNotFoundException e1) {
            System.err.println("File \"" + JSONHandler.getFile().toString() + "\" missing.");
        } catch (IOException e1) {
            System.err.println(IO_EXCEPTION_STRING);
            e1.printStackTrace();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        try (
            Reader reader = new FileReader(super.getFile());
        ) {
            this.fromJson(reader);
        } catch (FileNotFoundException e1) {
            System.err.println("File \"" + JSONHandler.getFile().toString() + "\" missing.");
        } catch (IOException e1) {
            System.err.println(IO_EXCEPTION_STRING);
            e1.printStackTrace();
        }

        return json.toString();

        /*return json.values().stream()
                .map(x -> x.toString())
                .peek(System.out::println)
                .collect(Collectors.joining(", "));*/
    }

    /**
     * {@inheritDoc}
     */
    public String toJsonString() {

        try (
            Reader reader = new FileReader(super.getFile());
        ) {
            this.fromJson(reader);
        } catch (FileNotFoundException e1) {
            System.err.println("File \"" + JSONHandler.getFile().toString() + "\" missing.");
        } catch (IOException e1) {
            System.err.println(IO_EXCEPTION_STRING);
            e1.printStackTrace();
        }

        return "{".concat(
                json.entrySet().stream()
                        .map(entry -> "\"" + entry.getKey().toString() + "\":"
                                + entry.getValue().toString())
                .collect(Collectors.joining(","))).concat("}");

        /*return json.values().stream()
                .map(x -> x.toString())
                .peek(System.out::println)
                .collect(Collectors.joining(", "));*/
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
            System.err.println(JSON_EXCEPTION_STRING);
            e.printStackTrace();
        }
    }
}

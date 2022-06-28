package game.utility.input;

/**
 * This interface models how a JSON reader must be made.
 */
public interface JSONReader {

    /**
     * Read informations passed by String parameter.
     *
     * @param string A {@link String}, formatted in JSON, that represents the {@link Jsonable}.
     */
    void read(String string);

    /**
     * Read informations written by {@link JSONWriterImpl}.
     */
    void read();

    /**
     * Read informations written by {@link JSONWriterImpl} and
     * returns the string obtained by the internal map.
     *
     * @return a {@link String} with all JSON data
     */
    @Override
    String toString();

    /**
     * Read informations written by {@link JSONWriterImpl} to a
     * JSON formatted string.
     *
     * @return a {@link String} with all JSON data
     */
    String toJsonString();
}

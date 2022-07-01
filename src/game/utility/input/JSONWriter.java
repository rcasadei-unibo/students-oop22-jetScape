package game.utility.input;

import java.io.IOException;
import java.io.Writer;

import com.github.cliftonlabs.json_simple.Jsonable;

/**
 * This interface models how a JSON writer must be made.
 */
public interface JSONWriter extends Jsonable {

    /**
     * Deletes the records file, so the game has to
     * generate a new one.
     */
    void clear();

    /**
     * Write informations passed by {@link JSONHandler}.
     */
    void write();

    /**
     * {@inheritDoc}
     */
    @Override
    String toJson();

    /**
     * {@inheritDoc}
     */
    @Override
    void toJson(Writer writer) throws IOException;
}

package game.utility.other;

/**
 * The class {@link FormatException} describes an exception occured when
 * a bad formatted file is read and its contents cannot be turned into an object.
 */
public class FormatException  extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@link FormatException} containing the specified message.
     * 
     * @param message a string describing the cause of the error.
     */
    public FormatException(final String message) {
        super(message);
    }
}

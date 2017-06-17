package pl.rasztabiga.haldeserializer.exception;

/**
 * Exception thrown when parser cannot deserialize JSON because of not enough JSON elements or internal error
 */
public class DeserializationError extends Exception {
    public DeserializationError(String s) {
        super(s);
    }
}

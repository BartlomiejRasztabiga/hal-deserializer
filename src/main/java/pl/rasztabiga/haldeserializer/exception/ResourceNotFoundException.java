package pl.rasztabiga.haldeserializer.exception;

/**
 * Resource thrown when HttpClient cannot find resource to deserialize
 */
public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String s) {
        super(s);
    }
}

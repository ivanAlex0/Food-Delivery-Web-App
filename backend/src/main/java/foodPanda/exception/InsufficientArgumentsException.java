package foodPanda.exception;

/**
 * A specific Exception to be thrown whenever a request has insufficient arguments
 */
public class InsufficientArgumentsException extends RuntimeException {

    public InsufficientArgumentsException(String message) {
        super(message);
    }
}
package foodPanda.exception;

/**
 * A specific Exception to be thrown whenever a request has invalid input
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
}

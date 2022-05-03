package foodPanda.exception;

/**
 * A specific Exception to be thrown whenever an authentication fails
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
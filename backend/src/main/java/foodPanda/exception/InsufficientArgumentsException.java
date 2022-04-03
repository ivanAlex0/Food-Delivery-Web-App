package foodPanda.exception;

public class InsufficientArgumentsException extends RuntimeException {

    public InsufficientArgumentsException(String message) {
        super(message);
    }
}
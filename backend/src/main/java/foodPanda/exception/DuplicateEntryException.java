package foodPanda.exception;

public class DuplicateEntryException extends RuntimeException{

    public DuplicateEntryException(String message)
    {
        super(message);
    }
}

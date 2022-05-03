package foodPanda.exception;

/**
 * A specific Exception to be thrown whenever there exist a duplicate entry in the DB
 */
public class DuplicateEntryException extends RuntimeException{

    public DuplicateEntryException(String message)
    {
        super(message);
    }
}

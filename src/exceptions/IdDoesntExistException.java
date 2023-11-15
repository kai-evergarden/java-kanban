package exceptions;

public class IdDoesntExistException extends RuntimeException{
    public IdDoesntExistException(String error)  {
        super(error);
    }
}


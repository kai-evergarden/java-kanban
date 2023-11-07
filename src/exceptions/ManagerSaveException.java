package exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException (String error) {
        super(error);
    }

}

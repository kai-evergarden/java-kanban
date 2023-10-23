package exceptions;

import java.util.concurrent.TimeoutException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException (String error) {
        super(error);
    }
}

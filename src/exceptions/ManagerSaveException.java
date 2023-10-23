package exceptions;

public class ManagerSaveException extends RuntimeException {
    // Обьясните пожалкйста почему run time exception дает такой резултат, заранее спасибо большое
    public ManagerSaveException (String error) {
        super(error);
    }
}

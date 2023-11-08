package exceptions;

public class TimeCrossingException extends RuntimeException{
    public TimeCrossingException (String error){
        super(error);
    }
}

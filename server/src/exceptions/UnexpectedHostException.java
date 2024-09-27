package exceptions;

public class UnexpectedHostException extends RuntimeException {

    public UnexpectedHostException() {
        super("unknown host");
    }
}

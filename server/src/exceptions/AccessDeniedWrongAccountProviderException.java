package exceptions;

public class AccessDeniedWrongAccountProviderException extends Exception {
    public AccessDeniedWrongAccountProviderException() {
        super("access denied: wrong account provider");
    }
}

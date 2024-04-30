package exceptions;

public class AccessDeniedBadCredentialsException extends Exception {
    public AccessDeniedBadCredentialsException() {
        super("access denied: bad credentials");
    }
}

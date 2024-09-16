package exceptions;

public class NoPasswordForThisAccountException extends Exception {
    public NoPasswordForThisAccountException() {
        super("This account has no password");
    }
}

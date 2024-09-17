package exceptions;

public class NoAccountWithSuchUsernameException extends Exception {
    public NoAccountWithSuchUsernameException() {
        super("No account with such username exists.");
    }
}

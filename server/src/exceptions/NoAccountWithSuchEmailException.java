package exceptions;

public class NoAccountWithSuchEmailException extends Exception {
    public NoAccountWithSuchEmailException() {
        super("no account with such email");
    }
}

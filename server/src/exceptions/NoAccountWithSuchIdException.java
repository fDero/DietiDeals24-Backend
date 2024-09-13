package exceptions;

public class NoAccountWithSuchIdException extends Exception {
    public NoAccountWithSuchIdException() {
        super("no account with such id");
    }
}
